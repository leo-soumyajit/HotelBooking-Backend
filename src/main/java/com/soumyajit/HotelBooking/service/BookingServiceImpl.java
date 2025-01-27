package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Room;
import com.soumyajit.HotelBooking.repository.BookingRepository;
import com.soumyajit.HotelBooking.repository.HotelRepository;
import com.soumyajit.HotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Override
    public BookingDTOS initialiseBooking(BookingRequest bookingRequest) {
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(()->new ResourceNotFound("Hotel not found with id "+bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(()->new ResourceNotFound("Room not found with id "+bookingRequest.getRoomId()));
    }
}
