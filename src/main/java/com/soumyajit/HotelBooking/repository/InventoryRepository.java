package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.Inventory;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
