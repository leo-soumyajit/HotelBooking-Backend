package com.soumyajit.HotelBooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumyajit.HotelBooking.entities.*;
import com.soumyajit.HotelBooking.entities.Enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
public class BookingDTOS {

    private Long id;

//    private Hotel hotel;
//
//    private Room room;

    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer roomsCount;

    private LocalDate checkedInDate;

    private LocalDate checkedOutDate;

    private BookingStatus bookingStatus;

    private Set<GuestDTOS> guest;









}
