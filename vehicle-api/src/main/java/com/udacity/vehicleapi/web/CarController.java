package com.udacity.vehicleapi.web;

import com.udacity.vehicleapi.exception.CarNotFoundException;
import com.udacity.vehicleapi.exception.ManufacturerNotFoundException;
import com.udacity.vehicleapi.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.*;

@RestController
@RequestMapping("/vehicles")
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CarResponse>> getAllCars() {
        List<CarResponse> carList = null;

        try {
            carList = carService.getAllCars();
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add links
        carList.stream().map(car -> {
            long id = car.getId();

            car.add(linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
            car.add(linkTo(methodOn(CarController.class).getCarById(id)).withRel("car"));
            car.add(linkTo(methodOn(CarController.class).modifyCar(id, null)).withRel("modify"));
            car.add(linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"));

            return car;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @RequestMapping(path = "/vehicle", method = RequestMethod.GET)
    public ResponseEntity<CarResponse> getCarById(@RequestParam("id") long id) {
        CarResponse car = null;

        try {
            car = carService.getCarById(id);
        }
        catch(CarNotFoundException ex) {
            return new ResponseEntity("Car is not found for the input id. Check id", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add links
        car.add(linkTo(methodOn(CarController.class).getCarById(id)).withSelfRel());
        car.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"));
        car.add(linkTo(methodOn(CarController.class).modifyCar(id, null)).withRel("modify"));
        car.add(linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"));

        return new ResponseEntity(car, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CarResponse> createCar(@RequestBody Car newCar) {
        CarResponse car = null;

        try {
            Long id = carService.addCar(newCar);
            car = carService.getCarById(id);
        }
        catch(Exception ex) {
            System.out.println(ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add links
        car.add(linkTo(methodOn(CarController.class).getCarById(car.getId())).withRel("car"));
        car.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"));
        car.add(linkTo(methodOn(CarController.class).modifyCar(car.getId(), null)).withRel("modify"));
        car.add(linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"));

        return new ResponseEntity(car, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CarResponse> modifyCar(@RequestParam("id") long id, @RequestBody Car modifiedCar) {
        //boolean isModified = false;
        Long modifiedCarId = null;
        CarResponse car = null;

        try {
            modifiedCarId = carService.modifyCarById(id, modifiedCar);
            car = carService.getCarById(modifiedCarId);
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

        // Add links
        car.add(linkTo(methodOn(CarController.class).modifyCar(car.getId(), null)).withSelfRel());
        car.add(linkTo(methodOn(CarController.class).getCarById(car.getId())).withRel("car"));
        car.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"));
        car.add(linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"));

        return new ResponseEntity(car, HttpStatus.OK);
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
