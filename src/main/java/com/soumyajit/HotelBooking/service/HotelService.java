package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelInfoDTOS;

import java.util.List;

public interface HotelService {

    HotelDTOS createNewHotel(HotelDTOS hotelDTOS);
    HotelDTOS getHotelById(Long id);
    HotelDTOS updateHotelById(Long id , HotelDTOS hotelDTOS);
    Boolean deleteHotelById(Long hotelId);
    void activateHotel(Long hotelId);
    HotelInfoDTOS getHotelInfoById(Long hotelId);

    List<HotelDTOS> getAllHotels();
}
