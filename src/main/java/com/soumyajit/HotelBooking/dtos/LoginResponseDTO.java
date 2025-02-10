package com.soumyajit.HotelBooking.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {
    private String accessToken;
}
