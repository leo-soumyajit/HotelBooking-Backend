package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.dtos.HotelInfoDTOS;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByOwner(User user);
}
