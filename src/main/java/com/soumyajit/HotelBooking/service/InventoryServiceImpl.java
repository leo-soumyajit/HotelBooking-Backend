package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.Exception.UnAuthorizedException;
import com.soumyajit.HotelBooking.dtos.HotelPriceDTO;
import com.soumyajit.HotelBooking.dtos.HotelSearchRequest;
import com.soumyajit.HotelBooking.dtos.InventoryDTOS;
import com.soumyajit.HotelBooking.dtos.UpdateInventoryRequestDTO;
import com.soumyajit.HotelBooking.entities.Inventory;
import com.soumyajit.HotelBooking.entities.Room;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.HotelMinPriceRepository;
import com.soumyajit.HotelBooking.repository.InventoryRepository;
import com.soumyajit.HotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.soumyajit.HotelBooking.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;

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
    public Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching Hotels for {} city,from {} to {}",
                hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());

        Pageable pageable = PageRequest
                .of(hotelSearchRequest.getPage() , hotelSearchRequest.getSize());
        long dateCount = ChronoUnit.DAYS
                .between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;
        log.info("Getting info by page form");
        Page<HotelPriceDTO> hotelPage = hotelMinPriceRepository
                .findHotelsWithAvailableInventory(hotelSearchRequest.getCity()
                        , hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(),
                        hotelSearchRequest.getRoomsCount(),dateCount,pageable
                );
        return hotelPage;
    }

    @Override
    public List<InventoryDTOS> getAllInventoryByRoom(Long roomId) {
        log.info("Getting all Inventory of a room with roomId : {}",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFound("Room with this Id : "+roomId+"Not found"));

        User user = getCurrentUser();
        if (!user.getId().equals(room.getHotel().getOwner().getId())){
            throw new UnAuthorizedException("This hotel is not belongs to this user : "+user.getUsername());
        }
        List<Inventory> inventoryList = inventoryRepository.findByRoomOrderByDate(room);
        return inventoryList.stream()
                .map(inventory -> modelMapper.map(inventory,InventoryDTOS.class))
                .collect(Collectors.toList());

    }


    @Transactional
    @Override
    public void updateInventory(Long roomId, UpdateInventoryRequestDTO updateInventoryRequestDTO) {
        log.info("Updating Inventory of a room with roomId in a Given DateRange: {}",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFound("Room with this Id : "+roomId+"Not found"));

        User user = getCurrentUser();
        if (!user.getId().equals(room.getHotel().getOwner().getId())){
            throw new UnAuthorizedException("This hotel is not belongs to this user : "+user.getUsername());
        }

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId,updateInventoryRequestDTO.getStartDate(),
                updateInventoryRequestDTO.getEndDate());


        inventoryRepository.updateInventory(roomId,updateInventoryRequestDTO.getStartDate(),
                updateInventoryRequestDTO.getEndDate(),updateInventoryRequestDTO.getSurgeFactor(),
                updateInventoryRequestDTO.getClosed());

    }
}
