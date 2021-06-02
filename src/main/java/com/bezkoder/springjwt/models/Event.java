package com.bezkoder.springjwt.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @ToString
@Getter @Setter
@Component
@Entity
@Data
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titre;
	private String description;
	private int degre_danger;
	private Double longitude;
	private Double latitude;
	@Column(name = "picByte", length = 1000)
	private byte[] imageBytes;
	private Boolean dejavue;
	private Long date;
	private String zone;
	private String categorie;
	private Boolean assigned;
	private Long assignedToAgent;
	private Boolean statut;
	
}
