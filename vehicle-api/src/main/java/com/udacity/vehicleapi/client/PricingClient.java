package com.udacity.vehicleapi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class PricingClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public PriceEntity getPrice(long vehicleId) {
        // Get service instances
        List<ServiceInstance> serviceInstanceList = null;

        try {
            serviceInstanceList = discoveryClient.getInstances("pricing-service");
        }
        catch(Exception ex) {
            System.out.println("Problem occurred while obtaining Pricing service instance");
            throw ex;
        }

        // Prepare url
        //String url = String.format("%s/services/price?vehicleId=%s", serviceInstanceList.get(0).getUri(), vehicleId);
        String url = String.format("%s/prices/%s", serviceInstanceList.get(0).getUri(), vehicleId);

        // Retrieve price
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PriceEntity> response = null;

        try {
            response = restTemplate.getForEntity(url, PriceEntity.class);
        }
        catch(Exception ex) {
            System.out.println("Problem occurred while retrieving response from Pricing service");
            throw ex;
        }

        // Return price
        return response.getBody();
    }
}
