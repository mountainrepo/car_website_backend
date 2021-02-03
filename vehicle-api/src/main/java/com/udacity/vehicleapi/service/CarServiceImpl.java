package com.udacity.vehicleapi.service;

import com.udacity.vehicleapi.client.*;
import com.udacity.vehicleapi.entity.CarEntity;
import com.udacity.vehicleapi.entity.ManufacturerEntity;
import com.udacity.vehicleapi.exception.*;
import com.udacity.vehicleapi.repository.*;
import com.udacity.vehicleapi.web.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ManufacturerRepository manRepository;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private PricingClient pricingClient;

    public Long addCar(Car newCar) {
        CarEntity newInsertedCar = null;

        try {
            // Get Location
            LocationEntity location = locationClient.getLocation(newCar.getLatitude(), newCar.getLongitude());

            // Check Manufacturer table and retrieve id if exists
            Long manufacturerId = manRepository.getId(newCar.getMake(), newCar.getModel(), newCar.getYear());

            if(manufacturerId == null) {
                ManufacturerEntity newRow = manRepository.save(new ManufacturerEntity(null, newCar.getMake(), newCar.getModel(), newCar.getYear()));
                manufacturerId = newRow.getId();
            }

            // Insert into Car table
            newInsertedCar = carRepository.save(new CarEntity(null, manufacturerId, newCar.getLatitude(), newCar.getLongitude(), location.getAddress(), location.getCity(), location.getState(), location.getZip(), null, newCar.getCondition(), newCar.getDetails()));

            // Get price
            double price = pricingClient.getPrice(newInsertedCar.getId());

            // Update price
            carRepository.updatePrice(price, newInsertedCar.getId());
        }
        catch(Exception ex) {
            throw ex;
        }

        return newInsertedCar.getId();
    }

    public Long modifyCarById(long id, Car modifiedCar) {
        CarEntity modifiedCarEntity = null;

        try {
            // Get car by id
            Optional<CarEntity> carEntity = carRepository.findById(id);

            if(!carEntity.isPresent()) {
                // throw NotFound Exception
                throw new CarNotFoundException("Car is not found");
            }

            CarEntity car = carEntity.get();

            // Get Manufacturer details
            Optional<ManufacturerEntity> manEntity = manRepository.findById(car.getManufacturerId());

            if(!manEntity.isPresent()) {
                // throw NotFound Exception
                throw new ManufacturerNotFoundException("Manufacturer is not found");
            }

            ManufacturerEntity mEntity = manEntity.get();

            // If Manufacturer details differ then add new row to Manufacturer table
            long manId = handleManUpdate(car, modifiedCar, mEntity);
            LocationEntity location = handleLocationUpdate(car, modifiedCar);

            // Delete existing car row
            carRepository.deleteById(id);

            // Insert new car row
            modifiedCarEntity = carRepository.save(new CarEntity(id, manId, modifiedCar.getLatitude(), modifiedCar.getLongitude(), location.getAddress(), location.getCity(), location.getState(), location.getZip(), modifiedCar.getPrice(), modifiedCar.getCondition(), modifiedCar.getDetails()));
        }
        catch(CarNotFoundException ex) {
            throw ex;
        }
        catch(ManufacturerNotFoundException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw ex;
        }

        return modifiedCarEntity.getId();
    }

    private LocationEntity handleLocationUpdate(CarEntity carEntity, Car car) {
        if(car.getLatitude() != carEntity.getLatitude() || car.getLongitude() != car.getLongitude()) {
            LocationEntity newLocation = locationClient.getLocation(car.getLatitude(), car.getLongitude());
            return newLocation;
        }
        else {
            return new LocationEntity(carEntity.getAddress(), carEntity.getCity(), carEntity.getState(), carEntity.getZip());
        }
    }

    private long handleManUpdate(CarEntity carEntity, Car car, ManufacturerEntity mEntity) {
        String make = car.getMake(), model = car.getModel();
        int year = car.getYear();

        if(!make.equals(mEntity.getMake()) || !model.equals(mEntity.getModel()) || year != mEntity.getYear()) {
            Long manId = manRepository.getId(make, model, year);

            if(manId != null) {
                return manId;
            }

            ManufacturerEntity newRow = manRepository.save(new ManufacturerEntity(null, car.getMake(), car.getModel(), car.getYear()));
            return newRow.getId();
        }
        else {
            return carEntity.getManufacturerId();
        }
    }

    public List<Car> getAllCars() {
        List<Car> carList = new LinkedList<Car>();

        try {
            // Retrieve all cars
            Iterable<CarEntity> carEntityList = carRepository.findAll();

            for(CarEntity entity : carEntityList) {
                String make = null, model = null;
                Integer year = null;

                // For each car, retrieve make, model, year
                Optional<ManufacturerEntity> manEntity = manRepository.findById(entity.getManufacturerId());

                if(manEntity.isPresent()) {
                    ManufacturerEntity manRow = manEntity.get();
                    make = manRow.getMake();
                    model = manRow.getModel();
                    year = manRow.getYear();
                }

                // Add car to carlist
                carList.add(new Car(entity.getId(), make, model, year, entity.getLatitude(), entity.getLongitude(), entity.getPrice(), entity.getCondition(), entity.getDetails() ));
            }
        }
        catch(Exception ex) {
            throw ex;
        }

        // return carList
        return carList;
    }

    public Car getCarById(long id) {
        CarEntity car = null;
        String make = null, model = null;
        Integer year = null;

        try {
            Optional<CarEntity> carEntity = carRepository.findById(id);

            if(!carEntity.isPresent()) {
                throw new CarNotFoundException("Car is not found");
            }

            car = carEntity.get();

            Optional<ManufacturerEntity> manEntity = manRepository.findById(car.getManufacturerId());

            if(manEntity.isPresent()) {
                ManufacturerEntity manRow = manEntity.get();
                make = manRow.getMake();
                model = manRow.getModel();
                year = manRow.getYear();
            }
        }
        catch(CarNotFoundException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw ex;
        }

        return new Car(car.getId(), make, model, year, car.getLatitude(), car.getLongitude(), car.getPrice(), car.getCondition(), car.getDetails() );
    }

    public boolean deleteCarById(long id) {
        try {
            if(!carRepository.existsById(id)) {
                throw new CarNotFoundException("Car is not found");
            }

            carRepository.deleteById(id);
        }
        catch(CarNotFoundException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw ex;
        }

        return true;
    }
}
