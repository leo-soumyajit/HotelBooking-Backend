package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Room;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryDTOS {
    private Long id;
    private LocalDate date;
    private Integer bookedCount;
    private Integer reservedCount;
    private Integer totalCount;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private Boolean closed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
