package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.Exception.UnAuthorizedException;
import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelInfoDTOS;
import com.soumyajit.HotelBooking.dtos.RoomDTOS;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.entities.Room;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.HotelRepository;
import com.soumyajit.HotelBooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.soumyajit.HotelBooking.util.AppUtils.getCurrentUser;

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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel if we don't put this any admin can access this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+id);
        }

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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Hotel hotel = modelMapper.map(hotelDTOS, Hotel.class);

        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel if we don't put this any admin can access this and can update this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+id);
        }
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


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel, if we don't put this any admin can access this and can delete this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+hotelId);
        }

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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel, if we don't put this any admin can access this and can activate this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+hotelId);
        }

        hotel.setIsActive(true);

        if(hotel.getIsActive()){
            for(Room room: hotel.getRoom()) {
                inventoryService.initializeRoomForAYear(room);
            }
        }
        // assuming only do it once

    }

    @Override
    public HotelInfoDTOS getHotelInfoById(Long hotelId) { //public route
        log.info("Getting the hotel information with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFound("Hotel not found with ID: "+hotelId));

        List<RoomDTOS> roomDTOS = hotel.getRoom().stream().map(room ->
                modelMapper.map(room, RoomDTOS.class))
                .toList();
        return new HotelInfoDTOS(modelMapper.map(hotel, HotelDTOS.class),roomDTOS);
    }

    @Override
    public List<HotelDTOS> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with Id {}: ",user.getId());
        List<Hotel> hotel = hotelRepository.findByOwner(user);
        return hotel.stream()
                .map(hotel1 -> modelMapper.map(hotel1,HotelDTOS.class))
                .collect(Collectors.toList());


    }

}
