package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
public class RoadMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "listIdCheckPoint")
	@ElementCollection(targetClass=Long.class)
	private List<Long> listIdCheckPoint;
	
	@Column(name = "dateDebut")
	private Date dateDebut;
	
	@Column(name = "dateFin")
	private Date dateFin;
	
	@Column(name = "geoFenceRadius")
	private Double geoFenceRadius;
	
	@Column(name = "create_at")
	private Date create_at;
	
	@Column(name="listIdUUIDimage")
	@ElementCollection(targetClass=String.class)
	private List<String> listIdUUIDCheckPoint;
}
