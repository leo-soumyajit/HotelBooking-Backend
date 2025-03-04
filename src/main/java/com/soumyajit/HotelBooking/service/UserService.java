package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.dtos.UserDTOS;
import com.soumyajit.HotelBooking.dtos.UserProfileUpdateRequestDTOS;
import com.soumyajit.HotelBooking.entities.User;

public interface UserService {

    User getUserById(Long id);

    void updateUserProfile(UserProfileUpdateRequestDTOS userProfileUpdateRequestDTOS);

    UserDTOS getMyProfile();
}
