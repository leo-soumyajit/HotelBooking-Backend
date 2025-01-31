package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDTO {
    private Hotel hotel;
    private Double price;
}
