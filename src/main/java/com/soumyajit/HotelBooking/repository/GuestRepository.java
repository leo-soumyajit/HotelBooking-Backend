package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}