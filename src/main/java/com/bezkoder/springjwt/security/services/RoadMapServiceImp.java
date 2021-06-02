package com.bezkoder.springjwt.security.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.AgentCheckPointStatut;
import com.bezkoder.springjwt.models.AgentRoadMap;
import com.bezkoder.springjwt.models.CheckPoint;
import com.bezkoder.springjwt.models.DocImage;
import com.bezkoder.springjwt.models.EventRoadMap;
import com.bezkoder.springjwt.models.RoadMap;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RoadMapRequest;
import com.bezkoder.springjwt.payload.request.RoadMapStatusRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsAppWebRequest;
import com.bezkoder.springjwt.payload.request.RoadMapsImagesRequest;
import com.bezkoder.springjwt.payload.response.ListRoadMapResponse;
import com.bezkoder.springjwt.payload.response.RoadMapResponse;
import com.bezkoder.springjwt.repository.AgentCheckPointStatutRepository;
import com.bezkoder.springjwt.repository.AgentRoadMapRepository;
import com.bezkoder.springjwt.repository.CheckPointReporitory;
import com.bezkoder.springjwt.repository.DocImageRepository;
import com.bezkoder.springjwt.repository.EventRoadMapRepository;
import com.bezkoder.springjwt.repository.RoadMapRepository;
import com.bezkoder.springjwt.repository.UserRepository;

@Service
@Transactional
public class RoadMapServiceImp implements RoadMapService {

	@Autowired
	private CheckPointReporitory checkPointReporitory ;
	
	@Autowired
	private RoadMapRepository roadMapRepository ;
	
	@Autowired
	private AgentRoadMapRepository agentRoadMapRepository ;
	
	@Autowired
	private EventRoadMapRepository eventRoadMapRepository;
	
	@Autowired
	private AgentCheckPointStatutRepository agentCheckPointStatutRepository;
	
	@Autowired
	DocImageRepository docImageRepository;
	
	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@Override
	public CheckPoint AddCheckPoint(CheckPoint checkPoint) {
		checkPoint.setCreate_at(new Date());
		return checkPointReporitory.save(checkPoint) ;
	}

	@Override
	public RoadMap AddRoadMap(RoadMap roadMap) {
		roadMap.setCreate_at(new Date());
		return roadMapRepository.save(roadMap);
	}

	@Override
	public AgentRoadMap AddAgentRoadMap(AgentRoadMap agentRoadMap) {
		return agentRoadMapRepository.save(agentRoadMap);
	}

	@Override
	public List<RoadMapRequest> GetRoadsMapAgent(Long AgentId) {
		List<RoadMapRequest> roadMapResponses = new ArrayList<RoadMapRequest>();
		List<AgentRoadMap> agentRoadsMap = GetAgentRoadMapByAgentID(AgentId);
		List<AgentRoadMap> agentRoadsMapNotfinish = new ArrayList<AgentRoadMap>();
		agentRoadsMap.forEach((n) -> {
			if(!n.getStatut()) {agentRoadsMapNotfinish.add(n);}
		});
		agentRoadsMapNotfinish.forEach((e) -> {
			Long IdAgentRoadMap = e.getId();
			List<Long> ListIdCheckPoint = roadMapRepository.findById(e.getIdRoadMap()).get().getListIdCheckPoint();
			List<List<Double>> ListCheckPoint =new ArrayList<List<Double>>();
			List<Boolean> StatutCheckPoint = new ArrayList<Boolean>();
			ListIdCheckPoint.forEach((f)  -> {
				List<Double> point = new ArrayList<Double>();
				Double longitude = checkPointReporitory.findById(f).get().getLongitude();
				Double latitude = checkPointReporitory.findById(f).get().getLatitude();
				point.add(longitude);point.add(latitude);
				ListCheckPoint.add(point);
				StatutCheckPoint.add(GetAgentCheckPointStatutByIDs(IdAgentRoadMap,f));
			});
			roadMapResponses.add(new RoadMapRequest(IdAgentRoadMap, ListIdCheckPoint, ListCheckPoint, StatutCheckPoint));
		});
		return roadMapResponses;
	}

	@Override
	public List<AgentRoadMap> GetAgentRoadMapByAgentID(Long AgentId) {
		List<AgentRoadMap> listAgentRoadMap = agentRoadMapRepository.findAll();
		List<AgentRoadMap> listAgentRoadMapByAgentId = new ArrayList<AgentRoadMap>();
		listAgentRoadMap.forEach((n) -> {
			if(n.getIdAgent() == AgentId) {listAgentRoadMapByAgentId.add(n);}
		});
		return listAgentRoadMapByAgentId;
	}

	@Override
	public Boolean GetAgentCheckPointStatutByIDs(Long IdAgentRoadMap, Long IdCheckPoint) {
		List<AgentCheckPointStatut> listAgentCheckPointStatutAll = agentCheckPointStatutRepository.findAll();
		for (int mi=0; mi<listAgentCheckPointStatutAll.size(); mi++) {
			if(listAgentCheckPointStatutAll.get(mi).getIdAgentRoadMap()== IdAgentRoadMap && 
					listAgentCheckPointStatutAll.get(mi).getIdCHeckPoint()==IdCheckPoint) {
				return listAgentCheckPointStatutAll.get(mi).getStatutCHeckPoint();
			}			
		}
		return false;
	}

	@Override
	public RoadMap AddRoadMap(RoadMapResponse roadMapResponse) {
		Date datedebut = roadMapResponse.getDatedebut();
		Date datefin = roadMapResponse.getDatefin();
		Double geofenceradius = roadMapResponse.getGeofenceradius();
		List<Long> listIdCheckPoint = new ArrayList<Long>();
		List<String> listDescription = roadMapResponse.getListdescription();
		for(int i=0;i<roadMapResponse.getListCheckPoint().size();i++) {
			CheckPoint checkPoint =AddCheckPoint(new CheckPoint(null, roadMapResponse.getListCheckPoint().get(i).get(1), roadMapResponse.getListCheckPoint().get(i).get(0),new Date(),listDescription.get(i))) ;
			listIdCheckPoint.add(checkPoint.getId());
		}
		List<String> listIdUUIDCheckPoint = new ArrayList<String>();
		roadMapResponse.getListUUIDimage().forEach(uuidImage -> {
			listIdUUIDCheckPoint.add(uuidImage);
		});
		RoadMap roadMap = roadMapRepository.save(new RoadMap(null, listIdCheckPoint, datedebut , datefin , geofenceradius , new Date(), listIdUUIDCheckPoint));
		return roadMap;
	}

	@Override
	public void UpdatePointControleImage(MultipartFile imagePControle, String UUIDimage) throws IOException {
		if (imagePControle != null) {
			
			DocImage image = new DocImage(UUIDimage, imagePControle.getContentType(), imagePControle.getOriginalFilename(),
					imagePControle.getBytes(),pushNotificationService.saveImage(imagePControle.getBytes()));
			try{
			    Thread.sleep(500);
			    docImageRepository.save(image);
			}catch(InterruptedException e){
			    e.printStackTrace();
			}
		}else {
			docImageRepository.save(new DocImage(UUIDimage, null, null, null,null));
		}
	}

	@Override
	public List<RoadMapsAppWebRequest> GetAllRoadMaps() {
		List<RoadMapsAppWebRequest> list = new ArrayList<RoadMapsAppWebRequest>();
		
		List<RoadMap> listRoadMap = roadMapRepository.findAll();
		listRoadMap.forEach((roadmap) ->{
			List<List<Double>> listCheckPoint = new ArrayList<List<Double>>();
			roadmap.getListIdCheckPoint().forEach((Idcheckpoint) ->{
				CheckPoint checkpoint = checkPointReporitory.findById(Idcheckpoint)
						.orElseThrow(() -> new RuntimeException("Error: CheckPoint is not found."));
				List<Double> point = new ArrayList<Double>();
				point.add(checkpoint.getLatitude());point.add(checkpoint.getLongitude());
				listCheckPoint.add(point);
			});
			Boolean status = false;Boolean assigned = false;
			if(agentRoadMapRepository.findByIdRoadMap(roadmap.getId()).isPresent()) {
				AgentRoadMap agentRoadMap = agentRoadMapRepository.findByIdRoadMap(roadmap.getId()).get();
				status = agentRoadMap.getStatut();assigned = true;
			}
			List<String> listdescription = new ArrayList<String>();
			roadmap.getListIdCheckPoint().forEach((IDcheckpoint) ->{
				listdescription.add(checkPointReporitory.findById(IDcheckpoint).get().getDescription());
			});
			list.add(new RoadMapsAppWebRequest(roadmap.getId(),listCheckPoint, roadmap.getCreate_at(), status,assigned,listdescription));
		});
		return list;
	}

	@Override
	public List<RoadMapsImagesRequest> GetRoadMapImagesById(Long idRoadMap) {
		RoadMap roadmap = roadMapRepository.findById(idRoadMap)
				.orElseThrow(() -> new RuntimeException("Error: RoadMap is not found."));
		List<RoadMapsImagesRequest> listImage = new ArrayList<RoadMapsImagesRequest>();
		roadmap.getListIdUUIDCheckPoint().forEach((value) ->{
			
			listImage.add(new RoadMapsImagesRequest(docImageRepository.findByUuidImage(value).get().getDocType(), 
					docImageRepository.findByUuidImage(value).get().getDocName(),
					docImageRepository.findByUuidImage(value).get().getDocName().getBytes()));
		});
		return listImage;
	}

	@Override
	public List<String> getUUIDimageRoadMap(Long idRoadMap) {
		return roadMapRepository.findById(idRoadMap).get().getListIdUUIDCheckPoint();
	}

	@Override
	public RoadMapsImagesRequest getImageByUUID(String uuid) {
		DocImage image1 =docImageRepository.findByUuidImage(uuid)
				.orElseThrow(() -> new RuntimeException("Error: Image is not found."));
		RoadMapsImagesRequest image = new RoadMapsImagesRequest(image1.getDocType(), image1.getDocName(), image1.getData());
		return image;
	}

	@Override
	public List<ListRoadMapResponse> getRoadsMap() {
		List<ListRoadMapResponse> listroadmapresponse = new ArrayList<ListRoadMapResponse>();
		List<RoadMap> listroatmap = roadMapRepository.findAll();
		listroatmap.forEach((roadmap) ->{
			if(agentRoadMapRepository.findByIdRoadMap(roadmap.getId()).isPresent()) {
				AgentRoadMap agentroadmap = agentRoadMapRepository.findByIdRoadMap(roadmap.getId()).get();
				listroadmapresponse.add(new ListRoadMapResponse(agentroadmap.getIdRoadMap(), agentroadmap.getIdAgent(), roadmap.getCreate_at(), roadmap.getDateDebut(), roadmap.getDateFin(),agentroadmap.getStatut()));
			}else {
				listroadmapresponse.add(new ListRoadMapResponse(roadmap.getId(), null,roadmap.getCreate_at(), roadmap.getDateDebut(), roadmap.getDateFin(),false));
			}
		});
		return listroadmapresponse;
	}

	@Override
	public void AssignAgentToRoadMap(String matricule, Long idRoadMap) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Error: User is not found."));
		agentRoadMapRepository.save(new AgentRoadMap(null, user.getId(), idRoadMap, false, new Date()));
		pushNotificationService.RegisterRoadMapInBF(idRoadMap,user.getId());
		pushNotificationService.sendRoadMapMyNotificationToToken(user.getId());
	}

	@Override
	public void ReceiveEventRoadMap(EventRoadMap event) {
		event.setId(null);
		System.out.println("fffffffffffffff     "+event.toString());
		EventRoadMap a = eventRoadMapRepository.save(event);
		System.out.println("fggggggggg     "+a.toString());
		AgentRoadMap agentRoadMap = agentRoadMapRepository.findById(event.getIdAgentRoadMap())
				.orElseThrow(() -> new RuntimeException("Error: AgentRoadMap is not found."));
		RoadMap roadmap = roadMapRepository.findById( agentRoadMap.getIdRoadMap())
				.orElseThrow(() -> new RuntimeException("Error: RoadMap is not found."));
		List<Long> listIdCheckPoint = roadmap.getListIdCheckPoint();
		if(listIdCheckPoint.get(listIdCheckPoint.size()-1)==event.getCheckPointId() && event.getEventType().equals("Exit")) {
			agentRoadMap.setStatut(true);
			agentRoadMapRepository.save(agentRoadMap);
		}
	}

	@Override
	public List<RoadMapStatusRequest> GetRoadMapStatus(Long IdRoadMap) {
		List<RoadMapStatusRequest> listRoadMapStatusRequest = new ArrayList<RoadMapStatusRequest>();
		RoadMap roadmap = roadMapRepository.findById( IdRoadMap)
				.orElseThrow(() -> new RuntimeException("Error: RoadMap is not found."));
		roadmap.getListIdCheckPoint().forEach((id) ->{
			CheckPoint checkPoint = checkPointReporitory.findById(id)
					.orElseThrow(() -> new RuntimeException("Error: CheckPoint is not found."));
			listRoadMapStatusRequest.add(new RoadMapStatusRequest(id, "Enter", null, checkPoint.getLongitude(), checkPoint.getLatitude()));
			listRoadMapStatusRequest.add(new RoadMapStatusRequest(id, "Exit", null, checkPoint.getLongitude(), checkPoint.getLatitude()));
		});
		if(agentRoadMapRepository.findByIdRoadMap(IdRoadMap).isPresent()) {
			AgentRoadMap agentRoadMap = agentRoadMapRepository.findByIdRoadMap(IdRoadMap)
					.orElseThrow(() -> new RuntimeException("Error: AgentRoadMap is not found."));
	
			List<EventRoadMap> eventRoadMap = eventRoadMapRepository.findByIdAgentRoadMap(agentRoadMap.getId());
			eventRoadMap.forEach((value) ->{
				for(int i=0;i<listRoadMapStatusRequest.size();i++) {
					if(listRoadMapStatusRequest.get(i).getIdCheckPoint()==value.getCheckPointId() && listRoadMapStatusRequest.get(i).getTypeEvent().equals(value.getEventType())) {
						listRoadMapStatusRequest.get(i).setDateEvent(value.getEventDate());
						}
				}
			});
		}
		return listRoadMapStatusRequest;
	}
	

	
	
	
	


}

