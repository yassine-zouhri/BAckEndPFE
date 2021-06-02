package com.bezkoder.springjwt.models;

import java.util.Date;

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
public class AgentCheckPointStatut {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "idAgentRoadMap")
	private Long idAgentRoadMap;
	
	@Column(name = "idCHeckPoint")
	private Long idCHeckPoint;
	
	@Column(name = "statutCHeckPoint")
	private Boolean statutCHeckPoint;
	
	@Column(name = "create_at")
	private Date create_at;
	
}
