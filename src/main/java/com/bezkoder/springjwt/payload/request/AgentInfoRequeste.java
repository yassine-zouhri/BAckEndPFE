package com.bezkoder.springjwt.payload.request;

import com.bezkoder.springjwt.payload.response.AgentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AgentInfoRequeste {
	
	private Long id;
	private String username;
	private String role;
	private String firstName;
	private String lastName;
	private String email;
	private String birthDate;
	private String city;
	private String country;
	private String company;
	private String jobPosition;
	private String mobile;
	private String avatar;

}
