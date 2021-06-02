package com.bezkoder.springjwt.security.services;

import java.util.List;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.payload.request.EventRequest;
import com.bezkoder.springjwt.payload.response.EventResponse;

public interface EventService {

	Event AddEventToBDD(EventResponse event);
	
	List<EventRequest> GetAllEvents();
	
	void UdpateEventOnFB(Long idEvent);
	
	void UpdateStatutEvent(Boolean statut,Long idEvent);
	
	String AddEventToFB(EventResponse event,Long AventId);
	
	void UpdateEventOnFBRT(Long idEvent);
}
