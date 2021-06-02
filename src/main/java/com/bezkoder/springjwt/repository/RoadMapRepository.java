package com.bezkoder.springjwt.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bezkoder.springjwt.models.RoadMap;

@RepositoryRestResource
public interface RoadMapRepository  extends JpaRepository<RoadMap,Long> {

}