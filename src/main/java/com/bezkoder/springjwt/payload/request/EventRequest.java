package com.bezkoder.springjwt.payload.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class EventRequest {
	private Long id;
	private String titre;
	private String description;
	private int degre_danger;
	private Double longitude;
	private Double latitude;
	private Date date;
	private String zone;
	private String categorie;
	private String statut;
	private String agentMatricule;
	
}