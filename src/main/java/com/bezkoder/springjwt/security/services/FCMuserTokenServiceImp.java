package com.bezkoder.springjwt.security.services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.DocImage;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.FCMuserTokenMobile;
import com.bezkoder.springjwt.models.FCMuserTokenWeb;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.FCMuserTokenWebRequest;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponse;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponseMobile;
import com.bezkoder.springjwt.repository.DocImageRepository;
import com.bezkoder.springjwt.repository.FCMuserTokenMobileRepository;
import com.bezkoder.springjwt.repository.FCMuserTokenWebRepository;
import com.bezkoder.springjwt.repository.UserRepository;

@Service
public class FCMuserTokenServiceImp implements FCMuserTokenService {

	@Autowired
	private FCMuserTokenMobileRepository fCMuserTokenMobileRepository;
	
	@Autowired
	private FCMuserTokenWebRepository fCMuserTokenWebRepository ;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DocImageRepository docImageRepository ;
	
	@Override
	public void AddFCMtoken(FCMuserTokenResponseMobile fCMuserToken) {
		System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooo "+fCMuserToken.getToken());
		FCMuserTokenMobile value =new FCMuserTokenMobile(fCMuserToken.getAgentId(), fCMuserToken.getToken(),new Date());
		fCMuserTokenMobileRepository.save(value);
	}

	@Override
	public void DeleteFCMtokenByAgentID(Long ByAgentID) {
		try {
			fCMuserTokenMobileRepository.deleteById(ByAgentID);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public void AddFCMtokenWeb(FCMuserTokenResponse fCMuserTokenWeb) {
		if(fCMuserTokenWeb.getToken() != null && fCMuserTokenWeb.getUserId() != null) {
			FCMuserTokenWeb fCMuserToken  =new FCMuserTokenWeb(fCMuserTokenWeb.getUserId(), fCMuserTokenWeb.getToken(),new Date());
			fCMuserTokenWebRepository.save(fCMuserToken);
		}
	}

	@Override
	public List<FCMuserTokenWeb> GetTokenUserByRole() {
		List<FCMuserTokenWeb> listFCMuserToken = new ArrayList<FCMuserTokenWeb>();
		List<User> listUser = userRepository.findAll();
		listUser.forEach((user) ->{
			Set<Role> roles = user.getRoles();
			roles.forEach(value ->{
				if(value.getName().equals(ERole.ROLE_SUPERVISEUR)) {
					listFCMuserToken.add(fCMuserTokenWebRepository.findById(user.getId()).get());
				}	
			});
		});
		return listFCMuserToken;
	}

	@Override
	public List<FCMuserTokenWebRequest> GetOnlineAgent() {
		List<FCMuserTokenWebRequest> listFCMuserTokenWebRequest = new ArrayList<FCMuserTokenWebRequest>();
		List<FCMuserTokenMobile> listAllAgentTokenMobile = fCMuserTokenMobileRepository.findAll();
		listAllAgentTokenMobile.forEach((agent) ->{
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date currentDate = new Date();
			if(findDifference(formatter.format(agent.getCreated_at()),formatter.format(currentDate))) {
				User user = userRepository.findById(agent.getAgentId()).get();
				DocImage docImage = docImageRepository.findByUuidImage(user.getUUIDimage()).get();
				FCMuserTokenWebRequest fCMuserTokenWebRequest = new FCMuserTokenWebRequest(agent.getAgentId(), agent.getToken(),agent.getCreated_at(), user.getUsername(), user.getEmail(), user.getMatricule(), docImage.getURLimage());
				listFCMuserTokenWebRequest.add(fCMuserTokenWebRequest);
			}
		});
		return listFCMuserTokenWebRequest;
	}
	
	public Boolean findDifference(String start_date,String end_date) {
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		 System.out.println(start_date);System.out.println(end_date);
		 Date d1;
		 Date d2;
		try {
			d1 = sdf.parse(start_date);
			d2 = sdf.parse(end_date);
			long seconds = (d2.getTime()-d1.getTime())/1000;
			System.out.println("seconds      ===   "+seconds);
			
			if(seconds <= 60*60*8) {
				 return true;
			 }
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	}

}