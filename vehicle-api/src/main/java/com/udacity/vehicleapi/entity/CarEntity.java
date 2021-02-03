package com.udacity.vehicleapi.entity;

import javax.persistence.*;

@Entity
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long manufacturerId;

    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String state;
    private String zip;

    private Double price;
    private String condition;
    private String details;

    public CarEntity() {

    }

    public CarEntity(Long id, Long manufacturerId, double lat, double lon, String address, String city, String state, String zip, Double price, String condition, String details) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.latitude = lat;
        this.longitude = lon;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.price = price;
        this.condition = condition;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
