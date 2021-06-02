package com.bezkoder.springjwt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class FCMuserTokenResponse {

	private Long userId;

	private String token;

}