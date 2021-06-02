package com.bezkoder.springjwt.payload.response;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Lob;

import com.bezkoder.springjwt.payload.request.UpdateProfileRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class ImageResponse {

	private String docType;

	private String docName;

	private byte[] data;
}
