package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelSearchRequest;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Inventory;
import com.soumyajit.HotelBooking.entities.Room;
import com.soumyajit.HotelBooking.repository.InventoryRepository;
import com.soumyajit.HotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today=today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .reservedCount(0)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting inventories of the room with id {} ",room.getId());
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelDTOS> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching Hotels for {} city,from {} to {}",
                hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());

        Pageable pageable = PageRequest
                .of(hotelSearchRequest.getPage() , hotelSearchRequest.getSize());
        long dateCount = ChronoUnit.DAYS
                .between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;
        log.info("Getting info by page form");
        Page<Hotel> hotelPage = inventoryRepository
                .findHotelsWithAvailableInventory(hotelSearchRequest.getCity()
                        , hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(),
                        hotelSearchRequest.getRoomsCount(),dateCount,pageable
                );
        return hotelPage.map(hotel -> modelMapper.map(hotel, HotelDTOS.class));
    }
}
