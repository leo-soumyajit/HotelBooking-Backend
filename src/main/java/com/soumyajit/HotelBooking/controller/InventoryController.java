package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.dtos.InventoryDTOS;
import com.soumyajit.HotelBooking.dtos.UpdateInventoryRequestDTO;
import com.soumyajit.HotelBooking.entities.Inventory;
import com.soumyajit.HotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<InventoryDTOS>> getAllInventoryByRoom(@PathVariable Long roomId){
        return ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }

    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<Void> updateInventory(@PathVariable Long roomId,
                                                @RequestBody UpdateInventoryRequestDTO updateInventoryRequestDTO){
        inventoryService.updateInventory(roomId,updateInventoryRequestDTO);
        return ResponseEntity.noContent().build();
    }


}
