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
public class RoadMapStatusRequest {

	private Long idCheckPoint ;
	private String typeEvent;
	private Date dateEvent;
	private Double longitude;
	private Double latitude;
}
