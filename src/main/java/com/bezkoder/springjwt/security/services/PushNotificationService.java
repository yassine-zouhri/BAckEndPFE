package com.bezkoder.springjwt.security.services;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.AgentRoadMap;
import com.bezkoder.springjwt.models.CheckPoint;
import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.FCMuserTokenWeb;
import com.bezkoder.springjwt.models.RoadMap;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.PushNotificationRequest;
import com.bezkoder.springjwt.repository.AgentRoadMapRepository;
import com.bezkoder.springjwt.repository.CheckPointReporitory;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.repository.FCMuserTokenMobileRepository;
import com.bezkoder.springjwt.repository.FCMuserTokenWebRepository;
import com.bezkoder.springjwt.repository.RoadMapRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Service
public class PushNotificationService {
	
	@Autowired
	private EventRepository eventRepository ;
	
	@Autowired
	private FCMuserTokenMobileRepository fCMuserTokenMobileRepository;
	
	@Autowired
	private FCMuserTokenWebRepository fCMuserTokenWebRepository ;
	
	@Autowired
	private RoadMapRepository roadMapRepository ;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CheckPointReporitory checkPointReporitory;
	
	@Autowired
	private AgentRoadMapRepository agentRoadMapRepository ;
	
	@Autowired
	private EventService eventService;
	
	private Storage storage;
	
	@EventListener
    public void init(ApplicationReadyEvent event) {
        try {
        	FileInputStream serviceAccount = serviceAccount = new FileInputStream("./geo-app1-firebase-adminsdk-ynhl4-f49a8fb6fc.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount)).
                    setProjectId("geo-app1.appspot.com").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    private FCMService fcmService;
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    public void sendPushNotificationToDevice(Long AgentID,Long eventID) {
    	try {
            fcmService.sendMessagetoDevice(getPayloadDataEvent(eventID),GetTokenFCM(AgentID));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", "msgid");
        pushData.put("text", "txt");
        pushData.put("user", "yassine");
        pushData.put("user", "ouio");
        return pushData;
    }
    
    private Map<String,String> getPayloadDataEvent(Long eventID){
    	Map<String, String> pushData = new HashMap<>();
    	Event event = eventRepository.findById(eventID).get();
    	try {
    		pushData.put("id", String.valueOf(event.getId()));
    		pushData.put("titre",event.getTitre());
    		pushData.put("description",event.getDescription());
    		pushData.put("degre_danger",String.valueOf(event.getDegre_danger()));
    		pushData.put("longitude",String.valueOf(event.getLongitude()));
    		pushData.put("latitude",String.valueOf(event.getLatitude()));
    		pushData.put("imageBytes",saveImage( event.getImageBytes()));
    		pushData.put("dejavue",String.valueOf(event.getDejavue()));
    		pushData.put("date",String.valueOf(event.getDate()));
    		pushData.put("zone",event.getZone());
    		pushData.put("categorie",event.getCategorie());
    	}catch (Exception e) {}
		return pushData;
    }
    
    private String GetTokenFCM(Long AgentID) {
    	String token = "";
    	if(fCMuserTokenWebRepository.findById(AgentID).isPresent()) {
    		token = fCMuserTokenWebRepository.findById(AgentID).get().getToken();
    	}else if(fCMuserTokenMobileRepository.findById(AgentID).isPresent()) {
    		token = fCMuserTokenMobileRepository.findById(AgentID).get().getToken();
    	}    	
    	return token;
    }
    
   
    
    public String saveImage(byte[] file) {
    	String imageURL="";
    	try {
    		System.out.println("saveImagesaveImagesaveImage");
        	String imageName = UUID.randomUUID().toString()+".jpeg";
    		String ProjectId = "geo-app1.appspot.com";
    		Map<String, String> map = new HashMap<>();
    		String imageToken =UUID.randomUUID().toString();
    		map.put("firebaseStorageDownloadTokens", imageToken);
    		BlobId blobId = BlobId.of("geo-app1.appspot.com", imageName);
    		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
    		        .setMetadata(map)
    		        .setContentType("image/jpeg")
    		        .build();
    		InputStream targetStream = new ByteArrayInputStream(file);
    		Blob blob = storage.create(blobInfo, targetStream);
    		URL url = new URL("https://firebasestorage.googleapis.com/v0/b/"+ProjectId+"/o/"+imageName+"?alt=media&token="+ imageToken);
    		imageURL = url.toString();
    		System.out.println("imageURL    ="+imageURL);
    	}catch (Exception e) {
			System.out.println("erreur   !:"+e.getMessage());
		}
    	
		return imageURL;
     
    }
    
    
    public String saveTest(MultipartFile file) {
    	try {
    		String imageName = generateFileName(file.getOriginalFilename());
    		String ProjectId = "geo-app1.appspot.com";
            Map<String, String> map = new HashMap<>();
            String imageToken =UUID.randomUUID().toString();
            map.put("firebaseStorageDownloadTokens", imageToken);
            BlobId blobId = BlobId.of("geo-app1.appspot.com", imageName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setMetadata(map)
                    .setContentType(file.getContentType())
                    .build();
            Blob blob = storage.create(blobInfo, file.getInputStream());
            String imageURL= "https://firebasestorage.googleapis.com/v0/b/"+ProjectId+"/o/"+imageName+"?alt=media&token="+ imageToken;
            System.out.println(" imageURL=  "+ imageURL);
            return imageURL;
    	}catch (Exception e) {
    		System.out.println("IOExceptionIOExceptionIOException  ="+e.getMessage());
		}
		return null;
        
    }
    
    private String generateFileName(String originalFileName) {   	
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
    
    public void  AddEventToFirebaseRT(Long eventID,Long AgentID)  {
    	Map<String,Object> pushData = new HashMap<>();
    	Event event = eventRepository.findById(eventID).get();

    	try {
    		pushData.put("id", event.getId());
    		pushData.put("titre",event.getTitre());
    		pushData.put("description",event.getDescription());
    		pushData.put("degre_danger",event.getDegre_danger());
    		pushData.put("longitude",event.getLongitude());
    		pushData.put("latitude",event.getLatitude());
    		pushData.put("imageURL",saveImage( event.getImageBytes()));
    		pushData.put("dejavue",event.getDejavue());
    		pushData.put("date",event.getDate());
    		pushData.put("zone",event.getZone());
    		pushData.put("categorie",event.getCategorie());
    		pushData.put("token",GetTokenFCM(AgentID));
    		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(UUID.randomUUID().toString());
            ref.setValueAsync(pushData);
    	}catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    public void sendPushMyNotificationToToken(Long AgentID,Long eventID) {

    	Event event = eventRepository.findById(eventID).get();
        try {
            fcmService.sendMyMessageToToken(GetTokenFCM(AgentID),event.getTitre(),event.getDescription());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    

	public void RegisterRoadMapInBF(Long idRoadMap, Long idAgent) {
		Map<String,Object> pushData = new HashMap<>();
		RoadMap roadmap = roadMapRepository.findById(idRoadMap)
				.orElseThrow(() -> new RuntimeException("Error: RoadMap is not found."));
		AgentRoadMap agentRoadMap = agentRoadMapRepository.findByIdRoadMap(idRoadMap)
				.orElseThrow(() -> new RuntimeException("Error: AgentRoadMap is not found."));
		User user = userRepository.findById(idAgent)
				.orElseThrow(() -> new RuntimeException("Error: RoadMap is not found."));

		List<List<Double>> listCheckPoints = new ArrayList<List<Double>>();
		List<Boolean> statutCheckPoint = new ArrayList<Boolean>();
		roadmap.getListIdCheckPoint().forEach((id) -> {
			CheckPoint checkpoint = checkPointReporitory.findById(id)
					.orElseThrow(() -> new RuntimeException("Error: CheckPoint is not found."));
			List<Double> list = new ArrayList<Double>();
			checkpoint.getLatitude();
			list.add(checkpoint.getLongitude());list.add(checkpoint.getLatitude());
			listCheckPoints.add(list);
			statutCheckPoint.add(false);
		});
		List<Long> listIdCheckPoint = roadmap.getListIdCheckPoint();
		
		pushData.put("username", user.getUsername());
		pushData.put("idAgentRoadMap", agentRoadMap.getId());
		pushData.put("listCheckPoints", listCheckPoints);
		pushData.put("statutCheckPoint", statutCheckPoint);
		pushData.put("listIdCheckPoint", listIdCheckPoint);
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("roadsMap").child(UUID.randomUUID().toString());
        ref.setValueAsync(pushData);
	}
	
	public void sendRoadMapMyNotificationToToken(Long AgentID) {
    	
        try {
            fcmService.sendMyMessageToToken(GetTokenFCM(AgentID),"Une nouvelle ronde.","vous venez de recevoir une nouvelle ronde.");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
	
	public void  AddEventToFirebaseAppWeb(Long eventID,Long agentID)  {
    	Map<String,Object> pushData = new HashMap<>();
    	Event event = eventRepository.findById(eventID).get();
    	try {
    		//UUID.randomUUID().toString()
    		pushData.put("id", event.getId());
    		pushData.put("titre",event.getTitre());
    		pushData.put("description",event.getDescription());
    		pushData.put("degre_danger",event.getDegre_danger());
    		pushData.put("longitude",event.getLongitude());
    		pushData.put("latitude",event.getLatitude());
    		pushData.put("imageURL",saveImage( event.getImageBytes()));
    		pushData.put("dejavue",event.getDejavue());
    		pushData.put("date",event.getDate());
    		pushData.put("zone",event.getZone());
    		pushData.put("categorie",event.getCategorie());
    		pushData.put("assigned",event.getAssigned());
    		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventsAppWeb").child(agentID.toString()).child(eventID.toString());
            ref.setValueAsync(pushData);
    	}catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
	
	public void sendEventToAgentMobile(Long AgentID,Long eventID) {
    	try {
    		Event event = eventRepository.findById(eventID).get();
    		event.setAssigned(true);
    		event.setAssignedToAgent(AgentID);
    		eventService.UdpateEventOnFB(eventID);
    		eventRepository.save(event);
            fcmService.sendMessagetoDevice(getPayloadDataEvent(eventID),GetTokenFCM(AgentID));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
	
	public void sendPushNotificationToAppWeb(PushNotificationRequest request, String imageURL) {
        try {
        	Map<String, String> pushData = new HashMap<>();
            pushData.put("imageURL", imageURL);
            fcmService.sendMessage(pushData, request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    

}