package com.bezkoder.springjwt.payload.request;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.*;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Component
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private String role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
  
    private String firstName;
	private String lastName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = JsonFormat.DEFAULT_TIMEZONE)
	private Date birthDate;
	
	private String city;
	
	private String country;
	
	private String company;
	
	private String jobPosition;
	
	@NotBlank
	private String mobile;
	
	private String uuidImage;
	
	private Boolean actif;
}
