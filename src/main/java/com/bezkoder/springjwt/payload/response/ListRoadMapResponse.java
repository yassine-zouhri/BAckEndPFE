package com.bezkoder.springjwt.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class ListRoadMapResponse {
	private Long idRoadMap;
	private Long idAgent;
	private Date dateCreation;
	private Date dateDebut;
	private Date dateFin;
	private Boolean status ;
}
