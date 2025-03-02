package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.RoomDTOS;
import com.soumyajit.HotelBooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels/{hotelId}/rooms")
public class RoomAdminController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTOS> createNewRoom(
            @RequestBody RoomDTOS roomDTOS , @PathVariable Long hotelId){
        return new ResponseEntity<>(roomService.createNewRoom(hotelId,roomDTOS), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDTOS>> getAllRooms(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsInAHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTOS> getRoomById(@PathVariable Long roomId,@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Boolean> deleteRoomById(@PathVariable Long roomId,@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.deleteRoomById(roomId));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTOS> updateRoom(@PathVariable Long roomId,@PathVariable Long hotelId,
                                              @RequestBody RoomDTOS roomDTOS){
        return ResponseEntity.ok(roomService.updateRoom(hotelId,roomId,roomDTOS));
    }

}
