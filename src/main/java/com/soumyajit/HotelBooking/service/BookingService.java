package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.dtos.GuestDTOS;
import com.soumyajit.HotelBooking.dtos.HotelReportsDTOS;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingDTOS initialiseBooking(BookingRequest bookingRequest);

    BookingDTOS addGuests(Long bookingId, List<GuestDTOS> guestDTOS);


    String initiatePayment(Long bookingId);

    void capturePayment(Event event);

    void cancelPayment(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDTOS> getAllBookings(Long hotelId);

    HotelReportsDTOS getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDTOS> getAllMyBookings();
}
