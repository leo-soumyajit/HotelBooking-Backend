package com.soumyajit.HotelBooking.Strategy;

import com.soumyajit.HotelBooking.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isTodayHoliday = true ; //TODO : Create a Constant array or ArrayList to store Holidays
        if (isTodayHoliday){
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
