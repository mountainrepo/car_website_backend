package com.udacity.vehicleapi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class LocationClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public LocationEntity getLocation(double latitude, double longitude) {
        // get all service instances
        List<ServiceInstance> serviceInstanceList = null;
        try {
            serviceInstanceList = discoveryClient.getInstances("boogle-maps");
        }
        catch(Exception ex) {
            System.out.println("Problem while retrieving Service Instances");
            throw ex;
        }

        // prepare url
        String url = String.format("%s/maps?lat=%s&lon=%s", serviceInstanceList.get(0).getUri(), latitude, longitude);

        // retrieve location
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LocationEntity> response = null;

        try {
            response = restTemplate.getForEntity(url, LocationEntity.class);
        }
        catch(Exception ex) {
            System.out.println("Problem while retrieving response from Location service");
            throw ex;
        }

        // return location
        return response.getBody();
    }
}
