package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.*;
import com.soumyajit.HotelBooking.entities.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDTOS> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDTO updateInventoryRequestDTO);
}
