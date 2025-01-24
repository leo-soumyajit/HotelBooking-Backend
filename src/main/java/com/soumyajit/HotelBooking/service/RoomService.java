package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.RoomDTOS;

import java.util.List;

public interface RoomService {
        RoomDTOS createNewRoom(Long hotelId , RoomDTOS roomDTOS);
        List<RoomDTOS> getAllRoomsInAHotel(Long hotelId);
        RoomDTOS getRoomById(Long roomId);
        boolean deleteRoomById(Long roomId);
}
