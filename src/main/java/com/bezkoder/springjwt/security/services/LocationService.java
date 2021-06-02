package com.bezkoder.springjwt.security.services;

import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.models.Location;



public interface LocationService {

	Optional<Location> getLocationById(Long id);
	
	void AddLocation(Location location);
	
	void AddLocations(List<Location> locations);
	
	List<Location> getLocationsByAgentId(Long id);
	
	Long GetAgentIdFromURN(Location location);
}

