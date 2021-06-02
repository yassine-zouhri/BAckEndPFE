package com.bezkoder.springjwt.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.Location;
import com.bezkoder.springjwt.security.services.LocationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/agent")
@Api(value = "Location api", description = "User's operations")
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	@GetMapping("/{agentId}/positions")
	public ResponseEntity<List<Location>> GetPositionsByAgentId(@PathVariable Long agentId) {
		List<Location> locations = locationService.getLocationsByAgentId(agentId);
		return ResponseEntity.status(HttpStatus.OK).body(locations);
	}
	
	@PostMapping("/me/positions")
	public ResponseEntity<List<Location>> SaveLocationsToBDD(@RequestBody List<Location> listLocations){
		System.out.println("locations   ="+listLocations.size());
		locationService.AddLocations(listLocations);
		return ResponseEntity.status(HttpStatus.OK).body(listLocations);
	}
	

	
}
