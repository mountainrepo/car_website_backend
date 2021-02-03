package com.udacity.vehicleapi.exception;

public class ManufacturerNotFoundException extends RuntimeException {

    public ManufacturerNotFoundException(String message) {
        super(message);
    }
}
