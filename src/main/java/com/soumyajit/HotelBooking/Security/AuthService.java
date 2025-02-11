package com.soumyajit.HotelBooking.Security;

import com.soumyajit.HotelBooking.EmailService.EmailService;
import com.soumyajit.HotelBooking.Exception.ResourceNotFound;
import com.soumyajit.HotelBooking.dtos.LoginDTOS;
import com.soumyajit.HotelBooking.dtos.SignUpRequestDTOS;
import com.soumyajit.HotelBooking.dtos.UserDTOS;
import com.soumyajit.HotelBooking.entities.Enums.Roles;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final com.soumyajit.HotelBooking.Security.OTPServiceAndValidation.OtpService otpService;



    //signup function with otp validation

    public UserDTOS signUp(SignUpRequestDTOS signUpRequestDTOS) {
        Optional<User> user = userRepository.findByEmail(signUpRequestDTOS.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with this Email is already present");
        }

        // Check if OTP was verified
        if (!otpService.isOTPVerified(signUpRequestDTOS.getEmail())) {
            throw new BadCredentialsException("OTP not verified");
        }

        User newUser = modelMapper.map(signUpRequestDTOS, User.class);
        newUser.setRoles(Set.of(Roles.GUEST)); // by default all users are guests
        newUser.setPassword(passwordEncoder.encode(signUpRequestDTOS.getPassword())); // bcrypt the password
        User savedUser = userRepository.save(newUser); // save the user

        // Clear OTP verification status after successful signup
        otpService.clearOTPVerified(signUpRequestDTOS.getEmail());

        return modelMapper.map(savedUser, UserDTOS.class);
    }



    public String[] login(LoginDTOS loginDTOS){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTOS.getEmail(),loginDTOS.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String[] arr = new String[2];
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        arr[0] = accessToken; //1st one is accessToken
        arr[1] = refreshToken; // 2nd one is refreshToken
        return arr;

    }
    public String refreshToken(String refreshToken) { //refreshToken method
        Long uerId = jwtService.getUserIdFromToken(refreshToken);  //refresh token is valid
        User user = userRepository.findById(uerId)
                .orElseThrow(()->
                        new ResourceNotFound("User not found with id : "+uerId));
        return jwtService.generateAccessToken(user);


    }





//        public UserDTOS signUp(SignUpRequestDTOS signUpRequestDTOS){  // signUp method for user normal no otp validation
//        Optional<User> user = userRepository.findByEmail(signUpRequestDTOS.getEmail());
//        if(user.isPresent()){
//            throw new BadCredentialsException("User with this Email is already present");
//        }
//        User newUser = modelMapper.map(signUpRequestDTOS,User.class);
//        newUser.setRoles(Set.of(Roles.GUEST)); // by default all user are guests
//        newUser.setPassword(passwordEncoder.encode(signUpRequestDTOS.getPassword())); // bcrypt the pass
//        User savedUser = userRepository.save(newUser); // save the user
//        return modelMapper.map(savedUser, UserDTOS.class);
//    }


}
