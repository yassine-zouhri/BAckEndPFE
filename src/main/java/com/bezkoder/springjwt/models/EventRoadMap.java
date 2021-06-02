package com.bezkoder.springjwt.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class EventRoadMap {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id ;	
	private Long idAgentRoadMap;
	private Long CheckPointId ;	
	private Date EventDate;
	private String EventType;
	private Boolean Statut;
	private Double longitude;
	private Double latitude;

}
