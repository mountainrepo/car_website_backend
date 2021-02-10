package com.udacity.vehicleapi.repository;

import com.udacity.vehicleapi.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;

@Repository
public interface ManufacturerRepository extends CrudRepository<ManufacturerEntity, Long> {

    @Query(value = "SELECT id FROM MANUFACTURER_ENTITY m WHERE m.code = ?1 AND m.name = ?2", nativeQuery = true)
    public Long getId(int code, String name);
}
