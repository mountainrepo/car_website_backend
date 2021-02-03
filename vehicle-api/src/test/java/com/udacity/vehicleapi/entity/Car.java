package com.udacity.vehicleapi.entity;

public class Car {

    private Long id;
    private String make;
    private String model;
    private Integer year;

    private Double latitude;
    private Double longitude;

    private Double price;
    private String condition;
    private String details;

    public Car(Long id, String make, String model, Integer year, Double latitude, Double longitude, Double price, String condition, String details) {
        this.id = id;
        this.model = model;
        this.make = make;
        this.year = year;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.condition = condition;
        this.details = details;
    }

    public Car() {

    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
