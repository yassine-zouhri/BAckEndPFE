package com.bezkoder.springjwt.security.services;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.DocImage;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.EventRequest;
import com.bezkoder.springjwt.payload.request.EventRequestFB;
import com.bezkoder.springjwt.payload.response.EventResponse;
import com.bezkoder.springjwt.repository.DocImageRepository;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Service
public class EventServiceImp implements EventService {
	
	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@Autowired
	private DocImageRepository docImageRepository;
	
	/*@Override
	public void AddEvent(Event event) {
		try{
			event.setDate(new Date().getTime());
			String MyUUID = UUID.randomUUID().toString();
			event.setId(MyUUID);		
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(MyUUID);
			ref.setValueAsync(event);
		}catch (Exception e) {}
	}*/

	@Override
	public Event AddEventToBDD(EventResponse event) {
		Event newEvent = new Event(event.getId(), event.getTitre(), event.getDescription(), event.getDegre_danger(),
				event.getLongitude(), event.getLatitude(), event.getImageBytes(), event.getDejavue(), event.getDate(), 
				event.getZone(), event.getCategorie(), false, null,false);
		return eventRepository.save(newEvent);
	}

	@Override
	public List<EventRequest> GetAllEvents() {
		List<EventRequest> listEventRequest = new ArrayList<EventRequest>();
		List<Event> listEvent = eventRepository.findAll();
		listEvent.forEach(event ->{
			String agentMatricule = "";
			if(event.getAssignedToAgent() != null) {
				agentMatricule = userRepository.findById(event.getAssignedToAgent())
						.orElseThrow(() -> new RuntimeException("Error: User is not found.")).getMatricule();
			}
			String statut = null;
			if(event.getStatut()==true) {
				statut = "Terminé";
			}else if(event.getStatut()==false && event.getAssignedToAgent()==null) {
				statut = "Non traité";
			}else if(event.getStatut()==false && event.getAssignedToAgent()!=null) {
				statut = "En cours de traitement";
			}
			EventRequest eventRequest = new EventRequest(event.getId(), event.getTitre(), event.getDescription(), event.getDegre_danger(),
					event.getLongitude(), event.getLatitude(),new Date(event.getDate()) , event.getZone(), event.getCategorie(),
					statut, agentMatricule);
			listEventRequest.add(eventRequest);
		});
		return listEventRequest;
	}

	@Override
	public void UdpateEventOnFB(Long idEvent) {
		List<User> listUser = userRepository.findAll();
		listUser.forEach((user) ->{
			Set<Role> roles = user.getRoles();
			roles.forEach(value ->{
				if(value.getName().equals(ERole.ROLE_SUPERVISEUR)) {
					UdpateEventOnFBRT(idEvent, user.getId());
				}	
			});
		});
	}
		
	public void UdpateEventOnFBRT(Long idEvent, Long idAgent) {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("eventsAppWeb").child(idAgent.toString()).child(idEvent.toString());
		ref.addValueEventListener(new ValueEventListener() {
		  @Override
		  public void onDataChange(DataSnapshot dataSnapshot) {
			  EventRequestFB eventRequest = dataSnapshot.getValue(EventRequestFB.class);
			  eventRequest.setAssigned(true);
			  Map<String,Object> pushData = new HashMap<>();
			  pushData.put("id", eventRequest.getId());
			  pushData.put("titre",eventRequest.getTitre());
			  pushData.put("description",eventRequest.getDescription());
			  pushData.put("degre_danger",eventRequest.getDegre_danger());
			  pushData.put("longitude",eventRequest.getLongitude());
			  pushData.put("latitude",eventRequest.getLatitude());
			  pushData.put("imageURL",eventRequest.getImageURL());
			  pushData.put("dejavue",true);
			  pushData.put("date",eventRequest.getDate());
			  pushData.put("zone",eventRequest.getZone());
			  pushData.put("categorie",eventRequest.getCategorie());
			  pushData.put("assigned",eventRequest.getAssigned());	  
			  ref.updateChildrenAsync(pushData);
		  }
		  @Override
		  public void onCancelled(DatabaseError databaseError) {
		    System.out.println("The read failed: " + databaseError.getCode());
		  }
		});
	}

	@Override
	public void UpdateStatutEvent(Boolean statut, Long idEvent) {
		Event event= eventRepository.findById(idEvent)
				.orElseThrow(() -> new RuntimeException("Error: Event is not found."));
		event.setStatut(statut);
		eventRepository.save(event);
	}

	@Override
	public String AddEventToFB(EventResponse event,Long AventId) {
		System.out.println(event.toString());
		System.out.println("AventId  AventId   "+AventId);
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("allEvents").child(AventId.toString());
		Map<String,Object> pushData = new HashMap<>();
		String imageURL = pushNotificationService.saveImage(event.getImageBytes());
		pushData.put("id", AventId);
		pushData.put("titre",event.getTitre());
		pushData.put("description",event.getDescription());
		pushData.put("degre_danger",event.getDegre_danger());
		pushData.put("longitude",event.getLongitude());
		pushData.put("latitude",event.getLatitude());
		pushData.put("imageURL",pushNotificationService.saveImage(event.getImageBytes()));
		pushData.put("dejavue",false);
		pushData.put("date",event.getDate());
		pushData.put("zone",event.getZone());
		pushData.put("categorie",event.getCategorie());
		pushData.put("assigned",false);	  
		ref.setValueAsync(pushData);
		/*try {
			//Thread.sleep(500);
			pushData.put("id", AventId);
			pushData.put("titre",event.getTitre());
			pushData.put("description",event.getDescription());
			pushData.put("degre_danger",event.getDegre_danger());
			pushData.put("longitude",event.getLongitude());
			pushData.put("latitude",event.getLatitude());
			pushData.put("imageURL",pushNotificationService.saveImage(event.getImageBytes()));
			pushData.put("dejavue",false);
			pushData.put("date",event.getDate());
			pushData.put("zone",event.getZone());
			pushData.put("categorie",event.getCategorie());
			pushData.put("assigned",false);	  
			ref.setValueAsync(pushData);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}*/
		return imageURL;
	}
	
	@Override
	public void UpdateEventOnFBRT(Long idEvent) {
		System.out.println("innnnnnnnnnnnnnnnnnnnnnnnnn");
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("allEvents").child(idEvent.toString());
		System.out.println(ref);
		Event event= eventRepository.findById(idEvent)
				.orElseThrow(() -> new RuntimeException("Error: Event is not found."));
		User user = userRepository.findById(event.getAssignedToAgent())
				.orElseThrow(() -> new RuntimeException("Error: User is not found."));
		DocImage docImage = docImageRepository.findByUuidImage(user.getUUIDimage())
				.orElseThrow(() -> new RuntimeException("Error: DocImage is not found."));
		ref.addValueEventListener(new ValueEventListener() {
		  @Override
		  public void onDataChange(DataSnapshot dataSnapshot) {
			  EventRequestFB eventRequest = dataSnapshot.getValue(EventRequestFB.class);
			  eventRequest.setAssigned(true);
			  Map<String,Object> pushData = new HashMap<>();
			  pushData.put("id", eventRequest.getId());
			  pushData.put("titre",eventRequest.getTitre());
			  pushData.put("description",eventRequest.getDescription());
			  pushData.put("degre_danger",eventRequest.getDegre_danger());
			  pushData.put("longitude",eventRequest.getLongitude());
			  pushData.put("latitude",eventRequest.getLatitude());
			  pushData.put("imageURL",eventRequest.getImageURL());
			  pushData.put("dejavue",true);
			  pushData.put("date",eventRequest.getDate());
			  pushData.put("zone",eventRequest.getZone());
			  pushData.put("categorie",eventRequest.getCategorie());
			  pushData.put("assigned",eventRequest.getAssigned());	  
			  pushData.put("statut",event.getStatut());
			  pushData.put("userFullName",user.getLastName()+" "+user.getFirstName());
			  pushData.put("userMatricule",user.getMatricule());
			  pushData.put("userMobile",user.getMobile());
			  pushData.put("userEmail",user.getEmail());
			  pushData.put("userImage",docImage.getURLimage());
			  System.out.println(pushData);
			  ref.updateChildrenAsync(pushData);
		  }
		  @Override
		  public void onCancelled(DatabaseError databaseError) {
		    System.out.println("The read failed: " + databaseError.getCode());
		  }
		});
	}

}
