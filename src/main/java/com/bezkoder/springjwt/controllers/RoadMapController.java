package com.bezkoder.springjwt.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.AgentCheckPointStatut;
import com.bezkoder.springjwt.models.AgentRoadMap;
import com.bezkoder.springjwt.models.CheckPoint;
import com.bezkoder.springjwt.models.EventRoadMap;
import com.bezkoder.springjwt.models.RoadMap;
import com.bezkoder.springjwt.payload.request.RoadMapRequest;
import com.bezkoder.springjwt.payload.request.RoadMapStatusRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsAppWebRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsImagesRequest;
import com.bezkoder.springjwt.payload.response.ListRoadMapResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.RoadMapResponse;
import com.bezkoder.springjwt.repository.AgentCheckPointStatutRepository;
import com.bezkoder.springjwt.security.services.PushNotificationService;
import com.bezkoder.springjwt.security.services.RoadMapService;
import com.bezkoder.springjwt.security.services.UserService;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/agent")
@Api(value = "Location api", description = "User's operations")
public class RoadMapController {
	
	@Autowired
	private RoadMapService roadMapService;
	
	
	@Autowired
	private AgentCheckPointStatutRepository agentCheckPointStatutRepository;
	
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@PostMapping("/addCheckPoint")
	public ResponseEntity<CheckPoint> AddCheckPoint(CheckPoint checkPoint){
		return ResponseEntity.ok().body(roadMapService.AddCheckPoint(checkPoint));
	}
	
	@GetMapping("/me/roadMap/{agentId}")
	public ResponseEntity<List<RoadMapRequest>> SendRoadMapAgent(@PathVariable("agentId") Long agentId ){
		
		/*Long IdRoadMap = (long) 5;
		List<Long> ListIdCheckPoint = new ArrayList<Long>();
		ListIdCheckPoint.add((long) 1);ListIdCheckPoint.add((long) 2);ListIdCheckPoint.add((long) 3);
		ListIdCheckPoint.add((long) 4);ListIdCheckPoint.add((long) 5);
		List<List<Double>> ListCheckPoint  = new ArrayList<List<Double>>();
		List<Double> a = new ArrayList<Double>();
		a.add(35.79038);a.add(-5.80470);
		ListCheckPoint.add(a);
		a = new ArrayList<Double>();a.add(0, 35.79008);a.add(1, -5.80583);ListCheckPoint.add(a);
		a = new ArrayList<Double>();a.add(0, 35.78987);a.add(1, -5.80731);ListCheckPoint.add(a);
		a = new ArrayList<Double>();a.add(0, 35.79055);a.add(1, -5.80850);ListCheckPoint.add(a);
		a = new ArrayList<Double>();a.add(0, 35.79048);a.add(1, -5.80934);ListCheckPoint.add(a);
		RoadMapResponse b= new RoadMapResponse(IdRoadMap,ListIdCheckPoint,ListCheckPoint);*/
		return ResponseEntity.ok().body(roadMapService.GetRoadsMapAgent(agentId));
	}
	
	@PostMapping("/me/EventRoadMap")
	public void GetEventRoadMap(@RequestBody EventRoadMap event) {
		roadMapService.ReceiveEventRoadMap(event);
		System.out.println(event.toString());
	}
	
	/*@GetMapping("test")
	public void test11() {
		List<Long> b = new ArrayList<Long>();
		b.add((long) 1);b.add((long) 2);b.add((long) 3);b.add((long) 4);b.add((long) 5);
		RoadMap a = new RoadMap(null,b, null,  null, 50.0,  new Date());
		RoadMap c= roadMapService.AddRoadMap(a);
		
		AgentRoadMap d = new AgentRoadMap(null, (long) 2, c.getId(), false, new Date());
		AgentRoadMap e = roadMapService.AddAgentRoadMap(d);
		
		AgentCheckPointStatut f1 = new AgentCheckPointStatut(null, e.getId(),(long) 1,false,new Date());
		AgentCheckPointStatut f2 = new AgentCheckPointStatut(null, e.getId(),(long) 2,false,new Date());
		AgentCheckPointStatut f3 = new AgentCheckPointStatut(null, e.getId(),(long) 3,false,new Date());
		AgentCheckPointStatut f4 = new AgentCheckPointStatut(null, e.getId(),(long) 4,false,new Date());
		AgentCheckPointStatut f5 = new AgentCheckPointStatut(null, e.getId(),(long) 5,false,new Date());
		agentCheckPointStatutRepository.save(f1);agentCheckPointStatutRepository.save(f2);
		agentCheckPointStatutRepository.save(f3);agentCheckPointStatutRepository.save(f4);
		agentCheckPointStatutRepository.save(f5);
		
		List<Long> b1 = new ArrayList<Long>();
		b1.add((long) 6);b1.add((long) 7);b1.add((long) 8);b1.add((long) 9);b1.add((long) 10);;b1.add((long) 11);
		RoadMap a1 = new RoadMap(null,b, null,  null, 50.0,  new Date());
		RoadMap c1= roadMapService.AddRoadMap(a);
		
		AgentRoadMap d1 = new AgentRoadMap(null, (long) 1, c.getId(), false, new Date());
		AgentRoadMap e1 = roadMapService.AddAgentRoadMap(d);
		
		AgentCheckPointStatut f11 = new AgentCheckPointStatut(null, e1.getId(),(long) 6,false,new Date());
		AgentCheckPointStatut f21 = new AgentCheckPointStatut(null, e1.getId(),(long) 7,false,new Date());
		AgentCheckPointStatut f31 = new AgentCheckPointStatut(null, e1.getId(),(long) 8,false,new Date());
		AgentCheckPointStatut f41 = new AgentCheckPointStatut(null, e1.getId(),(long) 9,false,new Date());
		AgentCheckPointStatut f51 = new AgentCheckPointStatut(null, e1.getId(),(long) 11,false,new Date());
		AgentCheckPointStatut f61 = new AgentCheckPointStatut(null, e1.getId(),(long) 11,false,new Date());
		agentCheckPointStatutRepository.save(f11);agentCheckPointStatutRepository.save(f21);
		agentCheckPointStatutRepository.save(f31);agentCheckPointStatutRepository.save(f41);
		agentCheckPointStatutRepository.save(f5);agentCheckPointStatutRepository.save(f61);
	}*/


	
	@PostMapping(value="/uploadImageCheckPoint/{uuidImage}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,"multipart/form-data","application/json"})
	public ResponseEntity<?> UploadUserImage(@RequestParam(value ="imagePControle",required=false) MultipartFile imageFile,@PathVariable(value="uuidImage") String uuidImage) throws IOException{
		roadMapService.UpdatePointControleImage(imageFile, uuidImage);
		return ResponseEntity.ok(new MessageResponse("Vous avez bien stocké votre image!"));
	}
	
	@PostMapping("/addRoadMap")
	public ResponseEntity<RoadMap> AddRoadMap(@RequestBody RoadMapResponse roadMapResponse){
		System.out.println(roadMapResponse);
		RoadMap roadMap = roadMapService.AddRoadMap(roadMapResponse);
		System.out.println(roadMap.toString());
		return ResponseEntity.ok().body(roadMap);
	}
	
	@GetMapping("/getAllRoadMap")
	public ResponseEntity<List<RoadMapsAppWebRequest>> GetAllRoadMap(){
		return ResponseEntity.ok().body(roadMapService.GetAllRoadMaps());
	}
	

	
	@GetMapping("/getUUIDimageRoadMap/{id}")
	public ResponseEntity<List<String>> getUUIDimageRoadMap(@PathVariable(value="id") Long idRoadMap){
		return ResponseEntity.ok().body(roadMapService.getUUIDimageRoadMap(idRoadMap));
	}
	
	@GetMapping("/getImageByUUID/{uudi}")
	public ResponseEntity<RoadMapsImagesRequest> getUUIDimageRoadMap(@PathVariable(value="uudi") String uudiImage){
		return ResponseEntity.ok().body(roadMapService.getImageByUUID(uudiImage));
	}
	
	
	@GetMapping("/getListRaodsMap")
	public ResponseEntity<List<ListRoadMapResponse>> getListRaodsMap(){
		return ResponseEntity.ok().body(roadMapService.getRoadsMap());
	}
	
	@PostMapping("/assignAgentToRoadMap/{matricule}/{idRoadMap}")
	public ResponseEntity<?> AssignRoadMapToAgent(@PathVariable(value="matricule") String matricule,@PathVariable(value="idRoadMap") Long idRoadMap){
		roadMapService.AssignAgentToRoadMap(matricule, idRoadMap);
		return ResponseEntity.ok(new MessageResponse("your request was successfully executed!"));
	}
	
	@GetMapping("/registerRoadmapToFB")
	public ResponseEntity<?> RegisterRoadmapToFB(Long idRoadMap, Long idAgent){
		pushNotificationService.RegisterRoadMapInBF(idRoadMap,idAgent);
		pushNotificationService.sendRoadMapMyNotificationToToken(idAgent);
		return ResponseEntity.ok(new MessageResponse("your request was successfully executed!"));
	}
	
	@GetMapping("/getRoadMapStatus/{idRoadMap}")
	public ResponseEntity<List<RoadMapStatusRequest>> GetRoadMapStatus(@PathVariable(value="idRoadMap") Long IdRoadMap){
		return ResponseEntity.ok().body(roadMapService.GetRoadMapStatus(IdRoadMap));
	}
	

}
