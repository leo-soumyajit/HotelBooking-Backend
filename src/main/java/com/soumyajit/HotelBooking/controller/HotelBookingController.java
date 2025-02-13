package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.BookingRequest;
import com.soumyajit.HotelBooking.dtos.GuestDTOS;
import com.soumyajit.HotelBooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDTOS> initialiseBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDTOS> addGuests(@PathVariable Long bookingId ,
                                                 @RequestBody List<GuestDTOS> guestDTOSList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDTOSList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String , String>> initiatePayment(@PathVariable Long bookingId){//return the session URL
        String sessionURL = bookingService.initiatePayment(bookingId);
        return ResponseEntity.ok(Map.of("sessionURL",sessionURL));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long bookingId){//return the session URL
        bookingService.cancelPayment(bookingId);
        return ResponseEntity.noContent().build();
    }

}
