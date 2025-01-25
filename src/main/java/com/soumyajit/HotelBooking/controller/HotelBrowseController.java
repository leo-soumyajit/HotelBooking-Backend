package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.HotelDTOS;
import com.soumyajit.HotelBooking.dtos.HotelInfoDTOS;
import com.soumyajit.HotelBooking.dtos.HotelSearchRequest;
import com.soumyajit.HotelBooking.service.HotelService;
import com.soumyajit.HotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping(value = "/search")
    public ResponseEntity<Page<HotelDTOS>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelDTOS> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDTOS> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
