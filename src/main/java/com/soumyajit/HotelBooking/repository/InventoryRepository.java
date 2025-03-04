package com.soumyajit.HotelBooking.repository;

import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Inventory;
import com.soumyajit.HotelBooking.entities.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteByRoom( Room room);

    @Query("""
            SELECT DISTINCT i.hotel
            FROM Inventory i
            WHERE i.city = :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.closed = false
                AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
            GROUP BY i.hotel,i.room
            HAVING COUNT(i.date) = :dateCount
            """)
    Page<Hotel> findHotelsWithAvailableInventory
            (
                @Param("city") String city,
                @Param("startDate")LocalDate startDate,
                @Param("endDate") LocalDate endDate,
                @Param("roomsCount") Integer roomsCount,
                @Param("dateCount") Long dateCount,
                Pageable pageable
            );


    @Query("""
            SELECT i\s
            FROM Inventory i
            WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND i.closed = false
                  AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
           \s""")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate")LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );


    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);

    List<Inventory> findByRoomOrderByDate(Room room);



    @Modifying
    @Query("""
                UPDATE Inventory i
                SET i.reservedCount = i.reservedCount + :numberOfRooms
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND (i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
                  AND i.closed = false
            """)
    void initBooking(@Param("roomId") Long roomId,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate,
                     @Param("numberOfRooms") int numberOfRooms);




    @Query("""
                SELECT i
                FROM Inventory i
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND (i.totalCount - i.bookedCount) >= :numberOfRooms
                  AND i.closed = false
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockReservedInventory(@Param("roomId") Long roomId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("numberOfRooms") int numberOfRooms);


    @Modifying //modifying means updating query others are select query
    @Query("""
                UPDATE Inventory i
                SET i.reservedCount = i.reservedCount - :numberOfRooms,
                    i.bookedCount = i.bookedCount + :numberOfRooms
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND (i.totalCount - i.bookedCount) >= :numberOfRooms
                  AND i.reservedCount >= :numberOfRooms
                  AND i.closed = false
            """)
    void confirmBooking(@Param("roomId") Long roomId,       //this is confirmed booking query
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("numberOfRooms") int numberOfRooms);




    @Modifying
    @Query("""
                UPDATE Inventory i
                SET i.bookedCount = i.bookedCount - :numberOfRooms
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
                  AND (i.totalCount - i.bookedCount) >= :numberOfRooms
                  AND i.closed = false
            """)
    void cancelBooking(@Param("roomId") Long roomId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate,
                       @Param("numberOfRooms") int numberOfRooms);




    @Query("""
                SELECT i
                FROM Inventory i
                WHERE i.room.id = :roomId
                AND i.date BETWEEN :startDate AND :endDate
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> getInventoryAndLockBeforeUpdate(@Param("roomId") Long roomId,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);



    @Modifying
    @Query("""
                UPDATE Inventory i
                SET i.surgeFactor = :surgeFactor,
                    i.closed = :closed
                WHERE i.room.id = :roomId
                  AND i.date BETWEEN :startDate AND :endDate
            """)
    void updateInventory(@Param("roomId") Long roomId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate,
                       @Param("surgeFactor") BigDecimal surgeFactor,
                       @Param("closed") boolean closed);
}
