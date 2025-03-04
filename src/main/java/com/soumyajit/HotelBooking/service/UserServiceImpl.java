package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.UserDTOS;
import com.soumyajit.HotelBooking.dtos.UserProfileUpdateRequestDTOS;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.soumyajit.HotelBooking.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFound("User not found with id :"+id)
                );
    }

    @Override
    public void updateUserProfile(UserProfileUpdateRequestDTOS userProfileUpdateRequestDTOS) {
        User user = getCurrentUser();
        if(userProfileUpdateRequestDTOS.getDateOfBirth() != null){
            user.setDateOfBirth(userProfileUpdateRequestDTOS.getDateOfBirth());
        }
        if(userProfileUpdateRequestDTOS.getGender() != null){
            user.setGender(userProfileUpdateRequestDTOS.getGender());
        }
        if(userProfileUpdateRequestDTOS.getName() != null){
            user.setName(userProfileUpdateRequestDTOS.getName());
        }
        userRepository.save(user);
    }

    @Override
    public UserDTOS getMyProfile() {
        log.info("Getting my profile");
        User user = getCurrentUser();
        return modelMapper.map(user, UserDTOS.class);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Resource isn't matched") {
                });
        return user;
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
