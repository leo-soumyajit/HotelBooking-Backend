package com.soumyajit.HotelBooking.service;

import com.soumyajit.HotelBooking.entities.Booking;
import com.soumyajit.HotelBooking.entities.User;
import com.soumyajit.HotelBooking.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService{

    private final BookingRepository bookingRepository;


    @Override
    public String getCheckOutSession(Booking booking, String successURL, String failureURL) {
        log.info("Creating session for booking with ID {} ",booking.getId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder() //get customer details
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();

        try {
            Customer customer = Customer.create(customerCreateParams);

            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId()) //pass customer details
                    .setSuccessUrl(successURL)
                    .setCancelUrl(failureURL)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr") //set currency to inr
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) //set amount to paisa or rupees
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder() //set Hotel details(Product)
                                                                    .setName(booking.getHotel().getName() +" : "+ booking.getRoom().getType()) //hotel name and Type
                                                                    .setDescription("Booking Id : "+booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(sessionParams);
            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking); //save the booking

            log.info("Session created successfully for booking with ID {} ",booking.getId());
            return session.getUrl(); //using this url user can proceed to Stripe checkout page

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
