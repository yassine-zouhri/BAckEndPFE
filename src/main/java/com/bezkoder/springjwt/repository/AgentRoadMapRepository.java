package com.bezkoder.springjwt.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bezkoder.springjwt.models.AgentRoadMap;

@RepositoryRestResource
public interface AgentRoadMapRepository extends JpaRepository<AgentRoadMap,Long> {
	Optional<AgentRoadMap> findByIdRoadMap(Long idRoadMap);
}