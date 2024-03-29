package com.udacity.vehicleapi.repository;

import com.udacity.vehicleapi.entity.CarEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends CrudRepository<CarEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE CAR_ENTITY c SET c.currency = ?1, c.price = ?2 WHERE c.id = ?3", nativeQuery = true)
    public void updatePrice(String currency, Double price, Long id);
}
