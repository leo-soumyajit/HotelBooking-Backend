package com.soumyajit.HotelBooking.Strategy;

import com.soumyajit.HotelBooking.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy {
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        LocalDate today = LocalDate.now();
        LocalDate inventoryDate = inventory.getDate();
        if (!inventoryDate.isBefore(today) && inventoryDate.isBefore(today.plusDays(7))){
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}



