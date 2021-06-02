package com.bezkoder.springjwt.payload.request;


import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoadMapsAppWebRequest {
	
	private Long roadMapID;
	private List<List<Double>> listCheckPoint;
	private Date created_at;
	private Boolean status;
	private Boolean assigned;
	private List<String> listdescription;
}
