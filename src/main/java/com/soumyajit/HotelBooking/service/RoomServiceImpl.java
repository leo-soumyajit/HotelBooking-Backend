package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.Exception.UnAuthorizedException;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;


    @Override
    @org.springframework.transaction.annotation.Transactional
    public RoomDTOS createNewRoom(Long hotelId , RoomDTOS roomDTOS) {
        log.info("Creating new room in Hotel with Id {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFound("Hotel not found with ID :"+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel, if we don't put this any admin can access this and can create Room this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+hotelId);
        }

        Room room = modelMapper.map(roomDTOS,Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        if(hotel.getIsActive()){    //initialize room when hotel getActivate
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room,RoomDTOS.class);
    }

    @Override
    public List<RoomDTOS> getAllRoomsInAHotel(Long hotelId) {
        log.info("Getting all rooms in Hotel with Id {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFound("Hotel not found with ID :"+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){     //if the Admin isn't won the hotel, if we don't put this any admin can access this
            throw new UnAuthorizedException("This user doesn't won this hotel with id: "+hotelId);
        }

        return hotel.getRoom().stream().map((elements)->
                modelMapper.map(elements,RoomDTOS.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTOS getRoomById(Long roomId) {
        log.info("Getting the room with Id {}",roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(()->new ResourceNotFound("Room not found with ID :"+roomId));
        return modelMapper.map(room,RoomDTOS.class);
    }


    @Override
    @Transactional
    public boolean deleteRoomById(Long roomId) {
        log.info("Deleting the room with Id {}",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFound("Room not found with ID :"+roomId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){     //if the Admin isn't won the hotel, if we don't put this any admin can access this and can delete this
            throw new UnAuthorizedException("This user doesn't won this room with id: "+roomId);
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);
        return true;
    }


    boolean isHotelExists(Long hotelId){
        boolean isExists = hotelRepository.existsById(hotelId);
        if(isExists){
            return true;
        }
        return false;
    }


    boolean isExists(Long roomId){
        boolean isExists = roomRepository.existsById(roomId);
        if(!isExists){
            return false;
        }
        return true;
    }

}
