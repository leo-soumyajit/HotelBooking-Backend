package com.soumyajit.HotelBooking.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpRequestDTOS {
    private String name;
    private String email;
    private String password;
}
