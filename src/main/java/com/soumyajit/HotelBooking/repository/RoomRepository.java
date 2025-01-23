package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

}
