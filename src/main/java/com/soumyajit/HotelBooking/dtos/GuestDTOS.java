package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Booking;
import com.soumyajit.HotelBooking.entities.Enums.Gender;
import com.soumyajit.HotelBooking.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
public class GuestDTOS {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
    private Set<Booking> bookings;
}
