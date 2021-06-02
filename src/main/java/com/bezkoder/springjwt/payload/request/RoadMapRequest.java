package com.bezkoder.springjwt.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoadMapRequest {

	private Long IdAgentRoadMap;
	private List<Long> ListIdCheckPoint;
	private List<List<Double>> ListCheckPoint;
	private List<Boolean> StatutCheckPoint;
	
}
