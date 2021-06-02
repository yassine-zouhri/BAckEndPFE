package com.bezkoder.springjwt.security.services;

import java.util.List;

import com.bezkoder.springjwt.models.FCMuserTokenMobile;
import com.bezkoder.springjwt.models.FCMuserTokenWeb;
import com.bezkoder.springjwt.payload.request.FCMuserTokenWebRequest;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponse;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponseMobile;

public interface FCMuserTokenService {

	void AddFCMtoken(FCMuserTokenResponseMobile fCMuserToken);
	
	void DeleteFCMtokenByAgentID(Long AgentID);
	
	void AddFCMtokenWeb(FCMuserTokenResponse fCMuserToken);
	
	List<FCMuserTokenWeb> GetTokenUserByRole();
	
	List<FCMuserTokenWebRequest> GetOnlineAgent();
}