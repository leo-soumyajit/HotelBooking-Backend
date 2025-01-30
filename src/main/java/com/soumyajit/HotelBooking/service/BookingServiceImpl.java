package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.dtos.GuestDTOS;
import com.soumyajit.HotelBooking.entities.*;
import com.soumyajit.HotelBooking.entities.Enums.BookingStatus;
import com.soumyajit.HotelBooking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

        for(Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);

        //create the Booking

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .checkedInDate(bookingRequest.getCheckInDate())
                .checkedOutDate(bookingRequest.getCheckOutDate())
                .roomsCount(bookingRequest.getRoomsCount())
                .hotel(hotel)
                .room(room)
                .user(getCurrentUser())
                .amount(BigDecimal.TEN)
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
        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state , cannot add guests");
        }
        for(GuestDTOS guestDTOS : guestDTOSList){
            Guest guest = modelMapper.map(guestDTOS, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepository.save(guest);
            booking.getGuest().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        return modelMapper.map(bookingRepository.save(booking), BookingDTOS.class);
    }
    private boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    private User getCurrentUser(){
        User user = new User();
        user.setId(1L); // TODO Remove this Dummy User
        //TODO Calculate Dynamic Price
        return user;
    }

}
