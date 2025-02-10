package com.soumyajit.HotelBooking.controller;

import com.soumyajit.HotelBooking.Security.AuthService;
import com.soumyajit.HotelBooking.dtos.LoginDTOS;
import com.soumyajit.HotelBooking.dtos.LoginResponseDTO;
import com.soumyajit.HotelBooking.dtos.SignUpRequestDTOS;
import com.soumyajit.HotelBooking.dtos.UserDTOS;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${deploy.env}")
    private String deployEnv;
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDTOS> signUp(@RequestBody SignUpRequestDTOS signUpRequestDTOS){
        return new ResponseEntity<>(authService.signUp(signUpRequestDTOS), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTOS loginDTOS ,
                                                  HttpServletRequest request ,
                                                  HttpServletResponse response){
        String[] tokens = authService.login(loginDTOS); //get the tokens
        Cookie cookie = new Cookie("refreshToken",tokens[1]); // add the refreshToken inside the cookie only
        cookie.setHttpOnly(true);

        cookie.setSecure("production".equals(deployEnv)); // when its on production mode it is true else development mode its false
        response.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDTO(tokens[0])); // return the access token which is String[0]
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("refreshToken Not Found inside the Cookies"));

        String newAccessToken =  authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken));
    }



}
