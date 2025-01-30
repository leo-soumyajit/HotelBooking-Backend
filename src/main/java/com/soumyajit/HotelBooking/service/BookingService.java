package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.dtos.GuestDTOS;

import java.util.List;

public interface BookingService {
    BookingDTOS initialiseBooking(BookingRequest bookingRequest);

    BookingDTOS addGuests(Long bookingId, List<GuestDTOS> guestDTOS);
}
