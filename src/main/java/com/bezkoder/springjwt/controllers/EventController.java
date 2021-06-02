package com.bezkoder.springjwt.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.FCMuserTokenWeb;
import com.bezkoder.springjwt.payload.request.EventRequest;
import com.bezkoder.springjwt.payload.request.PushNotificationRequest;
import com.bezkoder.springjwt.payload.response.EventResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.PushNotificationResponse;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.security.services.EventService;
import com.bezkoder.springjwt.security.services.FCMuserTokenService;
import com.bezkoder.springjwt.security.services.PushNotificationService;

import io.swagger.annotations.Api;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/agent")
@Api(value = "Location api", description = "User's operations")
public class EventController {

	@Autowired
	EventService eventService;
	
	@Autowired
	private EventRepository eventRepository ;
	
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@Autowired
	private FCMuserTokenService fCMuserTokenService;
	
	/*@PostMapping("/AddEvent")
	public void AddEventToFireBase(Event event)  throws InterruptedException, ExecutionException {
		eventService.AddEvent(event);
	}*/
	
	@PostMapping("/AddEvent")
	public void AddEventToBDD(@RequestBody EventResponse event) {
		Event newEvent = eventService.AddEventToBDD(event);
		String imageURL = eventService.AddEventToFB(event,newEvent.getId());
		eventService.AddEventToFB(event,newEvent.getId());
		List<FCMuserTokenWeb> listFCMuserToken = fCMuserTokenService.GetTokenUserByRole();
		listFCMuserToken.forEach((user) ->{
			PushNotificationRequest request = new PushNotificationRequest("Nouvel événement", "Vous venez de recevoir un nouvel événement", "New_Event", user.getToken());
			pushNotificationService.sendPushNotificationToAppWeb(request,imageURL);
			//pushNotificationService.sendPushNotificationToToken(request);
			pushNotificationService.AddEventToFirebaseAppWeb(newEvent.getId(),user.getAgentId());
		});
		System.out.println(listFCMuserToken.size());
	}
	
	@GetMapping("/AddEvent1/{idevent}")
	public void AddEventToBDD1(@PathVariable(value="idevent") Long idevent) {
		Event event = eventRepository.findById(idevent).get();
		List<FCMuserTokenWeb> listFCMuserToken = fCMuserTokenService.GetTokenUserByRole();
		listFCMuserToken.forEach((user) ->{
			System.out.println(user.toString());
			PushNotificationRequest request = new PushNotificationRequest("Nouvel événement", "Vous venez de recevoir un nouvel événement", "New_Event", user.getToken());
			pushNotificationService.sendPushNotificationToToken(request);
			pushNotificationService.AddEventToFirebaseAppWeb(event.getId(),user.getAgentId());
		});
		System.out.println(listFCMuserToken.size());
	}
	
	@GetMapping("/getEvents")
	public ResponseEntity<List<EventRequest>> GetAllEvents( ){
		return ResponseEntity.ok().body(eventService.GetAllEvents());
	}
	
	@GetMapping("/sendEventtoAgent/{agentId}/{eventId}")
	public ResponseEntity<?> sendEventtoAgent(@PathVariable(value="agentId") Long agentId,@PathVariable(value="eventId") Long eventId){
		System.out.println(agentId+"  "+eventId);
		pushNotificationService.sendEventToAgentMobile(agentId,eventId);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}
	
	@PostMapping("/updateStatutEvent/{statut}/{idEvent}")
	public ResponseEntity<?> UpdateStatutEvent(@PathVariable(value="idEvent") Long idEvent ,@PathVariable(value="statut") Boolean statut ){
		eventService.UpdateStatutEvent(statut, idEvent);
		eventService.UpdateEventOnFBRT(idEvent);
		return ResponseEntity.ok(new MessageResponse("your request was successfully executed!"));
	}
	
	


}