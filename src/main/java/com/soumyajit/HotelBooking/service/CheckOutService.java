package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.entities.Booking;

public interface CheckOutService {

    String getCheckOutSession(Booking booking , String successURL , String failureURL);


}
