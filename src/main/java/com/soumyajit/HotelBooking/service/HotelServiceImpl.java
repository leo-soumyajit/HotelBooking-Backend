package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelInfoDTOS;
import com.soumyajit.HotelBooking.dtos.RoomDTOS;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Room;
import com.soumyajit.HotelBooking.repository.HotelRepository;
import com.soumyajit.HotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDTOS createNewHotel(HotelDTOS hotelDTOS) {
        log.info("Creating new Hotel with name:{}" ,hotelDTOS.getName());
        Hotel hotel = modelMapper.map(hotelDTOS,Hotel.class);
        hotel.setIsActive(false);
        Hotel savedHotel  = hotelRepository.save(hotel);
        log.info("Created a new Hotel with this : {}",hotelDTOS.getId());
        return modelMapper.map(savedHotel,HotelDTOS.class);
    }

    @Override
    public HotelDTOS getHotelById(Long id) {
        log.info("Getting a new Hotel with this : {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFound("Hotel not found with ID :"+id));
        return modelMapper.map(hotel,HotelDTOS.class);
    }

    private Boolean isExists(Long hotelId){
        boolean isExists = hotelRepository.existsById(hotelId);
        if(!isExists){
            return false;
        }else {
            return true;
        }
    }


    @Override
    public HotelDTOS updateHotelById(Long id, HotelDTOS hotelDTOS) {
        log.info("Updating Hotel with this : {}",id);
        if(!isExists(id)){
            throw new ResourceNotFound("Hotel not found with ID :"+id);
        }
        Hotel hotel = modelMapper.map(hotelDTOS, Hotel.class);
        hotel.setId(id);
        Hotel savedHotel = hotelRepository.save(hotel);
        return modelMapper.map(savedHotel, HotelDTOS.class);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Boolean deleteHotelById(Long hotelId) {
        log.info("Deleting Hotel with this : {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFound("Hotel not found with ID :"+hotelId));

        for(Room room : hotel.getRoom()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(hotelId);
        return true;
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFound("Hotel not found with ID: "+hotelId));

        hotel.setIsActive(true);

        if(hotel.getIsActive()){
            for(Room room: hotel.getRoom()) {
                inventoryService.initializeRoomForAYear(room);
            }
        }
        // assuming only do it once

    }

    @Override
    public HotelInfoDTOS getHotelInfoById(Long hotelId) {
        log.info("Getting the hotel information with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFound("Hotel not found with ID: "+hotelId));
        List<RoomDTOS> roomDTOS = hotel.getRoom().stream().map(room ->
                modelMapper.map(room, RoomDTOS.class))
                .toList();
        return new HotelInfoDTOS(modelMapper.map(hotel, HotelDTOS.class),roomDTOS);
    }

}
