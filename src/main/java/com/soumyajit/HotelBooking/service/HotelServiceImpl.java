package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.entities.Hotel;
import com.soumyajit.HotelBooking.repository.HotelRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

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
    public Boolean deleteHotelById(Long hotelId) {
        log.info("Deleting Hotel with this : {}",hotelId);
        if(isExists(hotelId)){
            hotelRepository.deleteById(hotelId);
            //TODO: delete the future inventory
            return true;
        }else{
            throw new ResourceNotFound("Hotel not found with ID :"+hotelId);
        }
    }

}
