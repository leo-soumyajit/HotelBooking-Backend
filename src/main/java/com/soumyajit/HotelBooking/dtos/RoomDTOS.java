package com.soumyajit.HotelBooking.dtos;

import com.soumyajit.HotelBooking.entities.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class RoomDTOS {
    private Long id;
    //private Hotel hotel;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    //private LocalDateTime createdAt;
    //  private LocalDateTime updatedAt;
    private Integer totalCount;
    private Integer capacity;

}
