package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.Inventory;
import com.soumyajit.HotelBooking.entities.Room;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    void deleteByDateAfterAndRoom(LocalDate date, Room room);


}
