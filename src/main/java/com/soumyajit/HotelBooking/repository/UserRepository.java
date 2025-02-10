package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
