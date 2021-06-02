package com.bezkoder.springjwt.security.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.AgentRoadMap;
import com.bezkoder.springjwt.models.CheckPoint;
import com.bezkoder.springjwt.models.EventRoadMap;
import com.bezkoder.springjwt.models.RoadMap;
import com.bezkoder.springjwt.payload.request.RoadMapRequest;
import com.bezkoder.springjwt.payload.request.RoadMapStatusRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsAppWebRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsImagesRequest;
import com.bezkoder.springjwt.payload.response.ListRoadMapResponse;
import com.bezkoder.springjwt.payload.response.RoadMapResponse;


public interface RoadMapService {

	CheckPoint AddCheckPoint(CheckPoint checkPoint);
	
	RoadMap AddRoadMap(RoadMap roadMap);
	
	AgentRoadMap AddAgentRoadMap(AgentRoadMap agentRoadMap);
	
	List<RoadMapRequest> GetRoadsMapAgent(Long AgentId); 
	
	List<AgentRoadMap> GetAgentRoadMapByAgentID(Long AgentId);
	
	Boolean GetAgentCheckPointStatutByIDs(Long IdAgentRoadMap,Long IdCheckPoint);
	
	RoadMap AddRoadMap(RoadMapResponse roadMapResponse);
	
	void UpdatePointControleImage(MultipartFile imagePControle,String UUIDimage) throws IOException;
	
	List<RoadMapsAppWebRequest> GetAllRoadMaps();
	
	List<RoadMapsImagesRequest> GetRoadMapImagesById(Long idRoadMap);
	
	List<String> getUUIDimageRoadMap(Long idRoadMap);
	
	RoadMapsImagesRequest getImageByUUID(String uuid);
	
	List<ListRoadMapResponse> getRoadsMap();
	
	void AssignAgentToRoadMap(String matricule,Long idRoadMap);
	
	void ReceiveEventRoadMap(EventRoadMap event);
	
	List<RoadMapStatusRequest> GetRoadMapStatus(Long IdRoadMap);
}
