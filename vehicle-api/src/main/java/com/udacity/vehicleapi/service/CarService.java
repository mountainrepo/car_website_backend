package com.udacity.vehicleapi.service;

import com.udacity.vehicleapi.web.Car;
import com.udacity.vehicleapi.web.CarResponse;

import java.util.List;

public interface CarService {

    public Long addCar(Car newCar);

    public List<CarResponse> getAllCars();

    public CarResponse getCarById(long id);

    public Long modifyCarById(long id, Car modifiedCar);

    public boolean deleteCarById(long id);
}
