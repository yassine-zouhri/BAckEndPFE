package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

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
@Table(name = "DocImage")

@Getter @Setter
@Data @NoArgsConstructor @AllArgsConstructor  @ToString
public class DocImage {

	@Id
	@NonNull
	private String uuidImage;
	
	private String docType;
	
	private String docName;

	@Lob
	//@Type(type = "org.hibernate.type.ImageType")
	private byte[] data;

	private String URLimage;
	
}
