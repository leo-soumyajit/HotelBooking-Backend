package com.soumyajit.HotelBooking.entities;

import com.soumyajit.HotelBooking.entities.Enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Integer roomsCount;

    @Column(nullable = false)
    private LocalDateTime checkedInDate;

    @Column(nullable = false)
    private LocalDateTime checkedOutDate;

    @JoinColumn(name = "payment_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "booking_guest",
    joinColumns = @JoinColumn(name = "booking_id")
    ,inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private Set<Guest> guest;

}
