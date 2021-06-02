package com.bezkoder.springjwt.payload.request;

import java.util.Date;
import java.util.List;

import com.bezkoder.springjwt.payload.response.ImageResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class RoadMapsImagesRequest {
	
	private String docType;

	private String docName;

	private byte[] data;
	
}
