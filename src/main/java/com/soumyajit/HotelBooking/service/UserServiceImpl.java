package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFound("User not found with id :"+id)
                );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()->new AuthenticationException("Resource isn't matched") {
                });
    }
}
