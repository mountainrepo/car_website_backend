package com.udacity.vehicleapi.web;

import com.udacity.vehicleapi.exception.CarNotFoundException;
import com.udacity.vehicleapi.exception.ManufacturerNotFoundException;
import com.udacity.vehicleapi.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> carList = null;

        try {
            carList = carService.getAllCars();
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @RequestMapping(path = "/vehicle", method = RequestMethod.GET)
    public ResponseEntity<Car> getCarById(@RequestParam("id") long id) {
        Car car = null;

        try {
            car = carService.getCarById(id);
        }
        catch(CarNotFoundException ex) {
            return new ResponseEntity("Car is not found for the input id. Check id", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(car, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewCar(@RequestBody Car newCar) {
        try {
            Long id = carService.addCar(newCar);

            return new ResponseEntity(id, HttpStatus.CREATED);
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity modifyCar(@RequestParam("id") long id, @RequestBody Car modifiedCar) {
        //boolean isModified = false;
        Long modifiedCarId = null;

        try {
            modifiedCarId = carService.modifyCarById(id, modifiedCar);
        }
        catch(ManufacturerNotFoundException ex) {
            return new ResponseEntity("Manufacturer is not found for the input car id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(CarNotFoundException ex) {
            return new ResponseEntity("Car is not found for the input id. Check id", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(modifiedCarId, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteCar(@RequestParam("id") long id) {
        try {
            carService.deleteCarById(id);

            return new ResponseEntity(HttpStatus.OK);
        }
        catch(CarNotFoundException ex) {
            return new ResponseEntity("Car is not found for the input id. Check id", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
