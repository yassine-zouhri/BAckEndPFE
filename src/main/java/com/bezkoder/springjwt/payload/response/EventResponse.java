package com.bezkoder.springjwt.payload.response;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class EventResponse {

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
	
}
