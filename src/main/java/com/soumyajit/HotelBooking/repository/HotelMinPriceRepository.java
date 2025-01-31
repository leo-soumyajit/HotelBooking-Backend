package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.dtos.HotelPriceDTO;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {



    @Query("""
            SELECT com.soumyajit.HotelBooking.dtos.HotelPriceDTO(i.hotel , AVG(i.price))
            FROM  HotelMinPrice i
            WHERE i.hotel.city = :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.hotel.isActive = true
            GROUP BY i.hotel
            """)
    Page<HotelPriceDTO> findHotelsWithAvailableInventory
            (
                    @Param("city") String city,
                    @Param("startDate") LocalDate startDate,
                    @Param("endDate") LocalDate endDate,
                    @Param("roomsCount") Integer roomsCount,
                    @Param("dateCount") Long dateCount,
                    Pageable pageable
            );

}
