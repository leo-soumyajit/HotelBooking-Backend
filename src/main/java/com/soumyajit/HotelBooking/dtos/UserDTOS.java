package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class UserDTOS {
    private Long id;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
