package com.soumyajit.HotelBooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumyajit.HotelBooking.entities.HotelContactInfo;
import com.soumyajit.HotelBooking.entities.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class HotelDTOS {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    private HotelContactInfo hotelContactInfo;
    //private List<Room> room;
    private Boolean isActive;

}
