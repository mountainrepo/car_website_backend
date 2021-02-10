package com.udacity.vehicleapi.web;

import com.udacity.vehicleapi.entity.DetailEntity;
import org.springframework.hateoas.RepresentationModel;

public class CarResponse extends RepresentationModel {
    private Long id;

    private String condition;

    private DetailResponse details;

    private String currency;
    private Double price;

    private Location location;

    private String address;
    private String city;
    private String state;
    private String zip;

    public CarResponse() {

    }

    public CarResponse(Long id, String condition, DetailResponse details, String currency, Double price, Location location, String address, String city, String state, String zip) {
        this.id = id;
        this.condition = condition;
        this.details = details;
        this.currency = currency;
        this.price = price;
        this.location = location;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public DetailResponse getDetails() {
        return details;
    }

    public void setDetails(DetailResponse details) {
        this.details = details;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
