package com.soumyajit.HotelBooking.Exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
      super(message);
    }
}
