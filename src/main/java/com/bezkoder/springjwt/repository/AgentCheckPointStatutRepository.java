package com.bezkoder.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bezkoder.springjwt.models.AgentCheckPointStatut;
import java.lang.Long;
import java.util.List;

@RepositoryRestResource
public interface AgentCheckPointStatutRepository extends JpaRepository<AgentCheckPointStatut,Long> {
	List<AgentCheckPointStatut> findByIdAgentRoadMap(Long idagentroadmap);
}