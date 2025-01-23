package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.service.HotelService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels")
@Slf4j
public class HotelController {
    private final HotelService hotelService;

    @PostMapping()
    public ResponseEntity<HotelDTOS> createNewHotel(@RequestBody HotelDTOS hotelDTOS){
        log.info("Attempting to create new Hotel with this name : {}",hotelDTOS.getName());
        return new ResponseEntity<>
                (hotelService.createNewHotel(hotelDTOS), HttpStatus.CREATED);
    }
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTOS> getHotelById(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDTOS> updateHotelById(@PathVariable Long hotelId , @RequestBody HotelDTOS hotelDTOS){
        return ResponseEntity.ok(hotelService.updateHotelById(hotelId,hotelDTOS));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Boolean> deleteHotelById(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.deleteHotelById(hotelId));
    }





















}
