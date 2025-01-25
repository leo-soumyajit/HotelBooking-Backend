package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.entities.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
