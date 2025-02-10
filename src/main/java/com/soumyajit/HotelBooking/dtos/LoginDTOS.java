package com.soumyajit.HotelBooking.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDTOS {
    private String email;
    private String password;
}
