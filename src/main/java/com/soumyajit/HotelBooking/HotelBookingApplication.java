package com.soumyajit.HotelBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApplication.class, args);

	}

}
