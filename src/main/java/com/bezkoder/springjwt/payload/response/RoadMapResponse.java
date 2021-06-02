package com.bezkoder.springjwt.payload.response;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class RoadMapResponse {
	private Date datedebut;
	private Date datefin;
	private Double geofenceradius;
	private List<String> listUUIDimage;
	private List<List<Double>> listCheckPoint;
	private List<String> listdescription;
}
