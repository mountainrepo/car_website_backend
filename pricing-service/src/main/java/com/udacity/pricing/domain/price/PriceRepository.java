package com.udacity.pricing.domain.price;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {

    @Override
    @Query(value = "SELECT * FROM PRICE p WHERE p.id = CASEWHEN(?1 % 100 = 0, 100, ?1 % 100)", nativeQuery = true)
    public Optional<Price> findById(Long vehicleId);
}
