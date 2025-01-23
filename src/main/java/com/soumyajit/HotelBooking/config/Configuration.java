package com.soumyajit.HotelBooking.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    ModelMapper getBean(){
        return new ModelMapper();
    }
}
