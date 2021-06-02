package com.bezkoder.springjwt.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Location {
	
	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonIgnore
    private Long id;
    
    @Id
	@Column(name = "urn", unique = true)
	private String urn;
	
	@Column(name = "longitude")
	private Double geo_longitude;
	
	@Column(name = "latitude")
	private Double geo_latitude;
	
	@Column(name = "altitude")
	private Double geo_altitude;
	
	@Column(name = "provider")
	private String provider;
	
	@Column(name = "accuracy")
	private Double accuracy;
	
	@Column(name = "created_at")
	private Long created_at;
	
}
