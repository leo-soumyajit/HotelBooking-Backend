package com.soumyajit.HotelBooking.Advices;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String message;
}
