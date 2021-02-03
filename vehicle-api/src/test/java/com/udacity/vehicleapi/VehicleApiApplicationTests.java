package com.udacity.vehicleapi;

import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import com.udacity.vehicleapi.entity.*;
import org.junit.jupiter.api.*;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class VehicleApiApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String localhost = "http://localhost:";
    private String vehiclesPath = "/vehicles";

    @Test
    public void testPricingAPI() {
        int pricingAPIPort = 8082;
        String pricingPath = "/prices/2";

        String url = localhost + pricingAPIPort + pricingPath;

        ResponseEntity<PriceEntity> response = restTemplate.getForEntity(url, PriceEntity.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testBoogleMapsAPI() throws ClassNotFoundException {
        int boogleMapsPort = 9191;
        String boogleMapsPath = "/maps?lat=13.09&lon=80.19";

        String url = localhost + boogleMapsPort + boogleMapsPath;

        ResponseEntity<LocationEntity> response = restTemplate.getForEntity(url, LocationEntity.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetAll() {
        String url = localhost + port + vehiclesPath;

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, List.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private ResponseEntity<Car> get(Long id) {
        String url = localhost + port + vehiclesPath + "/vehicle?id=" + id;

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Car> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Car.class);

        return response;
        //Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testPost() throws InterruptedException {
        Long id = post();

        Thread.sleep(10000);

        ResponseEntity<Car> response = get(id);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private Long post() {
        String url = localhost + port + vehiclesPath;

        Car newCar = new Car(0L, "Honda", "CRV", 2013, 20.5, 55.5, 0.0, "NEW", "");

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<Car> requestEntity = new HttpEntity<Car>(newCar, headers);

        //ResponseEntity<String> response = restTemplate.postForEntity(url, newCar, String.class);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        return Long.parseLong(response.getBody());
    }

    @Test
    public void testDelete() {
        // Post
        Long id = post();

        // Delete
        String url = localhost + port + vehiclesPath + "?id=" + id;

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<Car> requestEntity = new HttpEntity<Car>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testModify() {
        int newYear = 2015;

        // Post
        Long id = post();

        // Get
        ResponseEntity<Car> getResponse = get(id);

        // Modify
        String url = localhost + port + vehiclesPath + "?id=" + id;

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        Car car = getResponse.getBody();
        car.setYear(newYear);

        HttpEntity<Car> requestEntity = new HttpEntity<Car>(car, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        // Check Modify
        ResponseEntity<Car> getResponse2 = get(Long.parseLong(response.getBody()));
        Car modifiedCar = getResponse2.getBody();

        Assertions.assertEquals(newYear, modifiedCar.getYear());
    }
}
