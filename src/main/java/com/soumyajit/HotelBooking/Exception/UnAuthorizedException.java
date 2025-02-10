package com.soumyajit.HotelBooking.Exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message) {
        super(message);
    }
}
