package com.udacity.vehicleapi.service;

import com.udacity.vehicleapi.client.*;
import com.udacity.vehicleapi.entity.CarEntity;
import com.udacity.vehicleapi.entity.DetailEntity;
import com.udacity.vehicleapi.entity.ManufacturerEntity;
import com.udacity.vehicleapi.exception.*;
import com.udacity.vehicleapi.repository.*;
import com.udacity.vehicleapi.web.*;
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
    private DetailRepository detailRepository;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private PricingClient pricingClient;

    public Long addCar(Car newCar) {
        CarEntity newInsertedCar = null;

        // Get Location
        // Get ManufacturerId, Save
        // Save Details
        // Insert Car
        // Get price
        // Update price

        try {
            // Get Location
            Location carLocation = newCar.getLocation();
            LocationEntity location = locationClient.getLocation(carLocation.getLat(), carLocation.getLon());

            // Check Manufacturer table and retrieve id if exists
            long manufacturerId = handleManufacturer(newCar.getDetails().getManufacturer());

            // Save Car Details
            long detailId = handleDetail(newCar.getDetails(), manufacturerId);

            // Insert into Car table
            // Long id, String condition, long detailId, String currency, double price, String address, String city, String state, String zip
            newInsertedCar = carRepository.save(new CarEntity(null, newCar.getCondition(), detailId, null, null, carLocation.getLat(), carLocation.getLon(), location.getAddress(), location.getCity(), location.getState(), location.getZip()));

            // Get price
            PriceEntity priceEntity = pricingClient.getPrice(newInsertedCar.getId());

            // Update price
            carRepository.updatePrice(priceEntity.getCurrency(), priceEntity.getPrice(), newInsertedCar.getId());
        }
        catch(Exception ex) {
            System.out.println(ex);
            throw ex;
        }

        return newInsertedCar.getId();
    }

    private long handleDetail(Detail detail, long manId) {
        // long id, String body, String model, long manufacturerId, int numberOfDoors, String fuelType, String engine, int mileage, int modelYear, int productionYear, String externalColor)
        DetailEntity newDetail = detailRepository.save(new DetailEntity(null, detail.getBody(), detail.getModel(), manId, detail.getNumberOfDoors(), detail.getFuelType(), detail.getEngine(), detail.getMileage(), detail.getModelYear(), detail.getProductionYear(), detail.getExternalColor()));

        return newDetail.getId();
    }

    private long handleManufacturer(Manufacturer manufacturer) {
        Long manufacturerId = manRepository.getId(manufacturer.getCode(), manufacturer.getName());

        if(manufacturerId == null) {
            ManufacturerEntity newRow = manRepository.save(new ManufacturerEntity(null, manufacturer.getCode(), manufacturer.getName()));
            manufacturerId = newRow.getId();
        }

        return manufacturerId;
    }

    public Long modifyCarById(long id, Car modifiedCar) {
        Long carId = null;

        // Delete detail, car by id
        // Add car
        try {
            deleteCarById(id);

            carId = addCar(modifiedCar);
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

        return carId;
    }

    public List<CarResponse> getAllCars() {
        List<CarResponse> carList = new LinkedList<CarResponse>();

        try {
            // Retrieve all cars
            Iterable<CarEntity> carEntityList = carRepository.findAll();

            for(CarEntity carEntity : carEntityList) {
                DetailEntity detailEntity = getDetailEntity(carEntity.getDetailId());
                ManufacturerEntity manEntity = getManEntity(detailEntity.getManufacturerId());

                DetailResponse detailResponse = computeDetailResponse(detailEntity, manEntity);
                CarResponse carResponse = computeCarResponse(carEntity, detailResponse);

                carList.add(carResponse);
            }
        }
        catch(Exception ex) {
            throw ex;
        }

        // return carList
        return carList;
    }

    public CarResponse getCarById(long id) {
        // Find car by id
        // Find Detail by id
        // Find Manufacturer by id
        // Compute Response
        CarResponse carResponse = null;

        try {
            CarEntity carEntity = getCarEntity(id);
            DetailEntity detailEntity = getDetailEntity(carEntity.getDetailId());
            ManufacturerEntity manEntity = getManEntity(detailEntity.getManufacturerId());

            DetailResponse detailResponse = computeDetailResponse(detailEntity, manEntity);
            carResponse = computeCarResponse(carEntity, detailResponse);
        }
        catch(CarNotFoundException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw ex;
        }

        return carResponse;
    }

    private CarEntity getCarEntity(long id) throws CarNotFoundException {
        Optional<CarEntity> carEntity = carRepository.findById(id);

        if(!carEntity.isPresent()) {
            throw new CarNotFoundException("Car is not found");
        }

        return carEntity.get();
    }

    private DetailEntity getDetailEntity(long id) throws DetailNotFoundException {
        Optional<DetailEntity> detail = detailRepository.findById(id);

        if(!detail.isPresent()) {
            throw new DetailNotFoundException("Detail is not found");
        }

        return detail.get();
    }

    private ManufacturerEntity getManEntity(long id) throws ManufacturerNotFoundException {
        Optional<ManufacturerEntity> manEntity = manRepository.findById(id);

        if(!manEntity.isPresent()) {
            throw new ManufacturerNotFoundException("Manufacturer is not found");
        }

        return manEntity.get();
    }

    private DetailResponse computeDetailResponse(DetailEntity detail, ManufacturerEntity manEntity) {
        // Long id, String body, String model, ManufacturerEntity manufacturer, int numberOfDoors, String fuelType, String engine, int mileage, int modelYear, int productionYear, String externalColor
        return new DetailResponse(detail.getId(), detail.getBody(), detail.getModel(), manEntity, detail.getNumberOfDoors(), detail.getFuelType(), detail.getEngine(), detail.getMileage(), detail.getModelYear(), detail.getProductionYear(), detail.getExternalColor());
    }

    private CarResponse computeCarResponse(CarEntity car, DetailResponse detailResponse) {
        // Long id, String condition, DetailResponse details, String currency, Double price, String address, String city, String state, String zip
        Location location = new Location(car.getLatitude(), car.getLongitude());
        return new CarResponse(car.getId(), car.getCondition(), detailResponse, car.getCurrency(), car.getPrice(), location, car.getAddress(), car.getCity(), car.getState(), car.getZip());
    }

    public boolean deleteCarById(long id) {
        try {
            if(!carRepository.existsById(id)) {
                throw new CarNotFoundException("Car is not found");
            }

            long detailId = carRepository.findById(id).get().getDetailId();

            detailRepository.deleteById(detailId);
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
