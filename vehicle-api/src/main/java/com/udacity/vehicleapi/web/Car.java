package com.udacity.vehicleapi.web;

public class Car {

    private String condition;

    private Detail details;

    private Location location;

    public Car(String condition, Detail details, Location location) {
        this.condition = condition;
        this.details = details;
        this.location = location;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Detail getDetails() {
        return details;
    }

    public void setDetails(Detail details) {
        this.details = details;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
