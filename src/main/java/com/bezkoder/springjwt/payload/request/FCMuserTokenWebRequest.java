package com.bezkoder.springjwt.payload.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class FCMuserTokenWebRequest {
	
	private Long agentId;
	private String token;
	private Date created_at;
	private String username;
	private String email;
	private String matricule;
	private String urlImage;
	
}
