package com.bezkoder.springjwt.payload.request;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class UpdateProfileRequest {

	@NotNull
	private String username;
	
	@NotEmpty
	private String role;
	
	@NotBlank
    @Size(min = 6, max = 40)
    private String password;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = JsonFormat.DEFAULT_TIMEZONE)
	private Date birthDate;
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String country;
	
	@NotBlank
	private String company;
	
	@NotBlank
	private String jobPosition;
	
	@NotBlank
	private String mobile;
	
	private Boolean actif;
}
