package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bezkoder.springjwt.payload.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})

@Getter @Setter
@Data @RequiredArgsConstructor @NoArgsConstructor @ToString
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	@NonNull
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@NonNull
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NonNull
	private String password;
 
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@NonNull
	private String firstName;
	
	@NonNull
	private String lastName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = JsonFormat.DEFAULT_TIMEZONE)
	@NonNull
	private Date birthDate;
	
	@NonNull
	private String city;
	
	@NonNull
	private String country;
	
	@NonNull
	private String company;
	
	@NonNull
	private String jobPosition;
	
	@NonNull
	private String mobile;

	@NonNull
	private String UUIDimage;
	
	@NonNull
	private Boolean actif;
	
	@NonNull
	private String matricule;
	
	public UserResponse toUserResponse() {
		//System.out.println("this.getRoles().stream().findAny().get().getName().name()   ="+this.getRoles().stream().findAny().get().getName().name());
		return new UserResponse(this.id, this.username, this.email,this.getRoles().stream().findAny().get().getName().name(), this.firstName,
				this.lastName, this.birthDate.toString(), this.city, this.country, this.company, 
				this.jobPosition, this.mobile, this.UUIDimage, this.actif);
	}
	
	
}
