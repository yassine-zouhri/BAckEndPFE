package com.bezkoder.springjwt.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.FCMuserTokenMobile;
import com.bezkoder.springjwt.models.Location;
import com.bezkoder.springjwt.payload.request.FCMuserTokenWebRequest;
import com.bezkoder.springjwt.payload.response.AthUser;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponse;
import com.bezkoder.springjwt.payload.response.FCMuserTokenResponseMobile;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.FCMuserTokenService;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@Api(value = "HelloWorld Resource", description = "User's operations")
public class JwtAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private FCMuserTokenService fCMuserTokenService;

	@PostMapping("/auth2")
	public ResponseEntity<Map<String,Object>> createAuthenticationToken(@RequestBody AthUser authenticationRequest) throws Exception {
		
		//System.out.println(authenticationRequest.getPassword() + "     in   "+authenticationRequest.getUsername());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateJwtToken(userDetails.getUsername());
		//final String jwt = jwtTokenUtil.generateJwtToken(userDetails);

		//System.out.println("tt " + jwt);
		//System.out.println("tt " + jwtTokenUtil.extractExpiration(jwt).toString());
		
		HashMap<String, Object> map = new HashMap<>();
	    map.put("accessToken","Bearer "+jwt);
	    map.put("expiresIn",jwtTokenUtil.extractExpiration(jwt));
	    map.put("type","HS256");
		
		return ResponseEntity.ok().body(map);
	}

	@PostMapping("/getTokenApp")
	public void getTokenAPP(@RequestBody FCMuserTokenResponseMobile fCMuserToken) {
		fCMuserTokenService.AddFCMtoken(fCMuserToken);
	}
	
	@PostMapping("/deleteToken")
	public void deleteToken(@RequestBody Long AgentID) {
		fCMuserTokenService.DeleteFCMtokenByAgentID(AgentID);
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			System.out.println(" o o "+ username + "  "+password);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
		throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
		throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@PostMapping("/getTokenAppWeb")
	public void getTokenAPP(@RequestBody FCMuserTokenResponse fCMuserToken) {
		fCMuserTokenService.AddFCMtokenWeb(fCMuserToken);
	}
	
	@GetMapping("/getOnlineAgent")
	public ResponseEntity<List<FCMuserTokenWebRequest>> GetOnlineAgent(){
		return ResponseEntity.status(HttpStatus.OK).body(fCMuserTokenService.GetOnlineAgent());
	}
	
	
	
	

}

