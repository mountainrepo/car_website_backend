package com.udacity.vehicleapi.repository;

import com.udacity.vehicleapi.entity.DetailEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends CrudRepository<DetailEntity, Long> {
}
