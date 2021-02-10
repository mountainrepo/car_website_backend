package com.udacity.vehicleapi;

import com.udacity.vehicleapi.web.*;

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

    private ResponseEntity<CarResponse> get(Long id) {
        String url = localhost + port + vehiclesPath + "/vehicle?id=" + id;

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<CarResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarResponse.class);

        return response;
        //Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testPost() throws InterruptedException {
        Long id = post();

        Thread.sleep(10000);

        ResponseEntity<CarResponse> response = get(id);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private Long post() {
        String url = localhost + port + vehiclesPath;

        //Car newCar = new Car(0L, "Honda", "CRV", 2013, 20.5, 55.5, 0.0, "NEW", "");
        Manufacturer manufacturer = new Manufacturer(105, "BMW");
        Detail detail = new Detail("SUV", "x5 M50i", manufacturer, 4, "Gasoline", "4.4L v8", 0, 2021, 2021, "Ametrin Metallic");
        Location location = new Location(37.7f, -121.93f);
        Car newCar = new Car("NEW", detail, location);

        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        HttpEntity<Car> requestEntity = new HttpEntity<Car>(newCar, headers);

        //ResponseEntity<String> response = restTemplate.postForEntity(url, newCar, String.class);
        ResponseEntity<CarResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CarResponse.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        return response.getBody().getId();
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
        String externalColor = "Tanzanite Blue Metallic";

        // Post
        Long id = post();

        // Get
        ResponseEntity<CarResponse> getResponse = get(id);
        CarResponse carResponse = getResponse.getBody();
        DetailResponse detail = carResponse.getDetails();
        ManufacturerEntity manEntity = detail.getManufacturer();

        // Modify
        String url = localhost + port + vehiclesPath + "?id=" + id;

        //      Add Headers
        HttpHeaders headers = new HttpHeaders();
        String credential = "newuser:pass123";
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()));
        headers.add("Authorization", "Basic " + encodedCredential);

        //      Perform modification
        Manufacturer manufacturer = new Manufacturer(manEntity.getCode(), manEntity.getName());
        Detail details = new Detail(detail.getBody(), detail.getModel(), manufacturer, detail.getNumberOfDoors(), detail.getFuelType(), detail.getEngine(), detail.getMileage(), detail.getModelYear(), detail.getProductionYear(), detail.getExternalColor());
        details.setExternalColor(externalColor);
        //Location location = new Location(carResponse.getLatitude(), carResponse.getLongitude());
        Car modifiedCar = new Car(carResponse.getCondition(), details, carResponse.getLocation());

        // Put modified car
        HttpEntity<Car> requestEntity = new HttpEntity<Car>(modifiedCar, headers);

        ResponseEntity<CarResponse> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CarResponse.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        // Check Modify
        ResponseEntity<CarResponse> getResponse2 = get(response.getBody().getId());
        CarResponse verifyCar = getResponse2.getBody();

        Assertions.assertEquals(externalColor, verifyCar.getDetails().getExternalColor());
    }
}
