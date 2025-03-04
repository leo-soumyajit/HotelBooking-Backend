package com.soumyajit.HotelBooking.dtos;

import lombok.Data;
import org.modelmapper.internal.bytebuddy.asm.Advice;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateInventoryRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal surgeFactor;
    private Boolean closed;
}
