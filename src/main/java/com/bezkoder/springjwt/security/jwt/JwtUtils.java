package com.bezkoder.springjwt.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bezkoder.springjwt.payload.request.AgentInfoRequeste;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.UserService;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;

	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private UserService userService;

	/*public String generateJwtToken(Authentication authentication) {

		Map<String, Object> claims = new HashMap<>();
    	claims.put("user",userRepository.findByUsername(authentication.getUsername()));
    	claims.put("type",SignatureAlgorithm.HS256);
		System.out.println(" jwtSecretjwtSecret   "+jwtSecret);
		System.out.println(" jwtSecretjwtSecret   "+userRepository.findByUsername(authentication.getUsername()).toString());
		return Jwts.builder().setClaims(claims)
				.setSubject((authentication.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}*/
	/*public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
    	claims.put("user",userRepository.findByUsername(userPrincipal.getUsername()));
    	claims.put("type",SignatureAlgorithm.HS256);
		
		return Jwts.builder().setClaims(claims)
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}*/
	
	public String generateJwtToken(String username) {

		Map<String, Object> claims = new HashMap<>();
    	AgentInfoRequeste agent = userService.generateAgentInfoResponse(username);
    	claims.put("user",agent);
    	claims.put("type",SignatureAlgorithm.HS256);
		
		return Jwts.builder().setClaims(claims)
				.setSubject((username))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}


	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	
	 public long extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration).getTime();
	    }
	 
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	 
	 private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	    }
	 
}
