package com.bezkoder.springjwt.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Getter @Setter
public class FCMuserTokenMobile {
	@Id
	@Column(name = "agentId")
	private Long agentId;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "created_at")
	private Date created_at;
}
