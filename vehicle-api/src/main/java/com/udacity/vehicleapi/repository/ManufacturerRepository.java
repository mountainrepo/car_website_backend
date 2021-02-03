package com.udacity.vehicleapi.repository;

import com.udacity.vehicleapi.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;

@Repository
public interface ManufacturerRepository extends CrudRepository<ManufacturerEntity, Long> {

    @Query(value = "SELECT id FROM MANUFACTURER_ENTITY m WHERE m.make = ?1 AND m.model = ?2 AND m.year = ?3", nativeQuery = true)
    public Long getId(String make, String model, int year);
}
