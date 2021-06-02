package com.bezkoder.springjwt.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.FCMuserTokenMobile;
import com.bezkoder.springjwt.models.FCMuserTokenWeb;
import com.bezkoder.springjwt.payload.request.PushNotificationRequest;
import com.bezkoder.springjwt.payload.response.PushNotificationResponse;
import com.bezkoder.springjwt.repository.FCMuserTokenMobileRepository;
import com.bezkoder.springjwt.repository.FCMuserTokenWebRepository;
import com.bezkoder.springjwt.security.services.PushNotificationService;

@RestController
public class PushNotificationController {
	
	@Autowired
	private FCMuserTokenWebRepository fCMuserTokenWebRepository;
	
	@Autowired
	private FCMuserTokenMobileRepository fCMuserTokenMobileRepository;


	private PushNotificationService pushNotificationService;

	public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }
    @PostMapping("/notification/topic")
    public ResponseEntity sendNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationWithoutData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
    /*@PostMapping("/notification/data")
    public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotification(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }*/
    

    
    @PostMapping("/1")
    public ResponseEntity<String> create(@RequestParam(name = "file") MultipartFile file) {
    	String fileName = null;
        try {
            fileName =  pushNotificationService.saveTest(file);
            // do whatever you want with that
        } catch (Exception e) {
        	System.out.println("fffffff            +" +e.getMessage());
        }
        return ResponseEntity.ok().body(fileName);
    }
    
	@PostMapping("/AddEventToRTDB")
	public ResponseEntity AddEventToBDD(@RequestParam(value = "agentID", required = true) Long AgentID,@RequestParam(value = "eventID", required = true) Long eventID) throws InterruptedException, ExecutionException {
		pushNotificationService.AddEventToFirebaseRT(eventID, AgentID);
		pushNotificationService.sendPushMyNotificationToToken(AgentID, eventID);
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}
	
	@GetMapping("/testo/{idAgent}")
	public void AddEventToBDD1(@PathVariable(value="idAgent") Long idAgent) {
		//FCMuserTokenWeb fCMuserTokenWeb = fCMuserTokenWebRepository.findById(idAgent).get();
		FCMuserTokenMobile fCMuserTokenMobile = fCMuserTokenMobileRepository.findById(idAgent).get();
		PushNotificationRequest request = new PushNotificationRequest("Nouvel événement", "Vous venez de recevoir un nouvel événement", "New_Event", fCMuserTokenMobile.getToken());
		pushNotificationService.sendPushNotificationToToken(request);
	}
	
    
    
    
}