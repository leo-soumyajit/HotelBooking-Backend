package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoDTOS {
    private HotelDTOS hotelInfo;
    private List<RoomDTOS> roomInfo;
}
