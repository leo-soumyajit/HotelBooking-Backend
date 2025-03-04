package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.BookingDTOS;
import com.soumyajit.HotelBooking.dtos.UserDTOS;
import com.soumyajit.HotelBooking.dtos.UserProfileUpdateRequestDTOS;
import com.soumyajit.HotelBooking.service.BookingService;
import com.soumyajit.HotelBooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
            @RequestBody UserProfileUpdateRequestDTOS userProfileUpdateRequestDTOS){
        userService.updateUserProfile(userProfileUpdateRequestDTOS);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTOS> getMyProfile(){
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDTOS>> getAllMyBookings(){
        return ResponseEntity.ok(bookingService.getAllMyBookings());
    }

}
