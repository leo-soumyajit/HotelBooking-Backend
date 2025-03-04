package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequestDTOS {

    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
