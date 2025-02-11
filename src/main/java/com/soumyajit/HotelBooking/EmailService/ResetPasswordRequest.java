package com.soumyajit.HotelBooking.EmailService;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@RequiredArgsConstructor
public class ResetPasswordRequest {
    private String code;
    private String newPassword;
}
