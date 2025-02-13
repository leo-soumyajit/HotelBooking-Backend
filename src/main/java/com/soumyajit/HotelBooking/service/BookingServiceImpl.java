package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.Exception.UnAuthorizedException;
import com.soumyajit.HotelBooking.Strategy.PricingService;
import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.dtos.GuestDTOS;
import com.soumyajit.HotelBooking.entities.*;
import com.soumyajit.HotelBooking.entities.Enums.BookingStatus;
import com.soumyajit.HotelBooking.repository.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.TODO;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final CheckOutService checkOutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingDTOS initialiseBooking(BookingRequest bookingRequest) {

        log.info("Initialising booking for hotel : {} , room: {}, date {}-{}",
                bookingRequest.getHotelId(),bookingRequest.getRoomId()
                ,bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(()->new ResourceNotFound("Hotel not found with id "+bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(()->new ResourceNotFound("Room not found with id "+bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(),bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount()
        );
        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;
        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Rooms are not available for anymore");
        }

        // Reverse the room / update the booked count of inventories

        inventoryRepository.initBooking(room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());

        //create the Booking

        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .checkedInDate(bookingRequest.getCheckInDate())
                .checkedOutDate(bookingRequest.getCheckOutDate())
                .roomsCount(bookingRequest.getRoomsCount())
                .hotel(hotel)
                .room(room)
                .user(getCurrentUser())
                .amount(totalPrice)
                //.guest() //TODO Guests will be added later
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTOS.class);
    }

    @Override
    @Transactional
    public BookingDTOS addGuests(Long bookingId, List<GuestDTOS> guestDTOSList) {

        log.info("Adding Guests for Booking with Id {} ",bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFound("Booking not found with id "+bookingId));


        User user = getCurrentUser();
        if (!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorizedException("Booking does not belong to this user with id: "+user.getId());
        }


        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired");
        }


        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state , cannot add guests");
        }

        for(GuestDTOS guestDTOS : guestDTOSList){
            Guest guest = modelMapper.map(guestDTOS, Guest.class);
            guest.setUser(getCurrentUser()); //set the Authenticated User
            guest = guestRepository.save(guest);
            booking.getGuest().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        return modelMapper.map(bookingRepository.save(booking), BookingDTOS.class);
    }

    @Override
    @Transactional
    public String initiatePayment(Long bookingId) {
        log.info("Initialise Payments for Booking with Id {} ",bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFound("Booking not found with id "+bookingId));

        User user = getCurrentUser();
        if (user.getId().equals(booking.getUser().getId())) {
            if(hasBookingExpired(booking)){ //check the booking hasn't expired
                throw new IllegalStateException("Booking has already expired");
            }

            //success url and failureUrl are comes from frontend part
            String sessionURL = checkOutService.getCheckOutSession(booking,
                    frontendUrl+"/payments/successURL",
                    frontendUrl+"/payments/failureURL");

            booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
            bookingRepository.save(booking);

            return sessionURL;
        }

        throw new UnAuthorizedException("Booking does not belong to this user with id: "+user.getId());
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;

            String sessionId = session.getId();

            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFound("Booking not found for session ID: "+sessionId)); //find the session id

            booking.setBookingStatus(BookingStatus.CONFIRMED); //now booking is confirmed
            bookingRepository.save(booking); //save the booking

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckedInDate(),
                    booking.getCheckedOutDate(), booking.getRoomsCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckedInDate(),
                    booking.getCheckedOutDate(), booking.getRoomsCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }


    @Override //cancel payment
    @Transactional
    public void cancelPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFound("Booking not found with id: "+bookingId)
        );

        User user = getCurrentUser();
        if (!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorizedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckedInDate(),
                booking.getCheckedOutDate(), booking.getRoomsCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckedInDate(),
                booking.getCheckedOutDate(), booking.getRoomsCount());

        // handle the refund

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId()); //get session Id
            RefundCreateParams refundParams = RefundCreateParams.builder()  //for refund
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams); //create Refund
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    //get booking status
    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFound("Booking not found with id: "+bookingId)
        );

        User user = getCurrentUser();
        if (!user.getId().equals(booking.getUser().getId())) {
            throw new UnAuthorizedException("Booking does not belong to this user with id: "+user.getId());
        }
        return booking.getBookingStatus().name();
    }


    private boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }


    //get Authenticated User
    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //this way we can get the authenticated user everyTime
    }

}
