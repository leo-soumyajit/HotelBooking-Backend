package com.soumyajit.HotelBooking.Security.OTPServiceAndValidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTPDetails {
    private String otp;
    private long timestamp;

}
