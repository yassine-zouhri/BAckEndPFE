package com.bezkoder.springjwt.security.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Location;
import com.bezkoder.springjwt.repository.LocationRepository;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
	
	@Autowired
	private LocationRepository localisationRepository;
	


	@Override
	public Optional<Location> getLocationById(Long id) {
		return localisationRepository.findById(id);
	}

	@Override
	public void AddLocation(Location location) {
		localisationRepository.save(location);
	}

	public List<Location> getLocationsByAgentId(Long id) {
		List<Location> AllLocations = localisationRepository.findAll();
		List<Location> locations=new ArrayList<Location>();
		for(Location location : AllLocations) {
			if(GetAgentIdFromURN(location) == id) {
				locations.add(location);
			}
		}
		return locations;
	}

	@Override
	public void AddLocations(List<Location> locations) {
		localisationRepository.saveAll(locations);
	}

	@Override
	public Long GetAgentIdFromURN(Location location) {
		String[] urn = location.getUrn().split(":");
		Long AgentId = Long.parseLong(urn[3]);
		return AgentId;
	}

}

