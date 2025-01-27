package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;

public interface BookingService {
    BookingDTOS initialiseBooking(BookingRequest bookingRequest);
}
