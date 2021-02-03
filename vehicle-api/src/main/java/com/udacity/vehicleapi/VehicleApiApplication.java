package com.udacity.vehicleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VehicleApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleApiApplication.class, args);
	}

}
