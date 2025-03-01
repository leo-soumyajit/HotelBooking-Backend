package com.soumyajit.HotelBooking.util;

import com.soumyajit.HotelBooking.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //this way we can get the authenticated user everyTime
    }

}
