package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelSearchRequest;
import com.soumyajit.HotelBooking.entities.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelDTOS> searchHotels(HotelSearchRequest hotelSearchRequest);
}
