package com.bezkoder.springjwt.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bezkoder.springjwt.models.EventRoadMap;

@RepositoryRestResource
public interface EventRoadMapRepository extends JpaRepository< EventRoadMap,Long>{
	List<EventRoadMap> findByIdAgentRoadMap(Long idAgentRoadMap);
}
