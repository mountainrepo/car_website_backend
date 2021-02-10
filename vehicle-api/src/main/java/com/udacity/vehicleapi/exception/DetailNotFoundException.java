package com.udacity.vehicleapi.exception;

public class DetailNotFoundException extends RuntimeException {

    public DetailNotFoundException(String message) {
        super(message);
    }
}
