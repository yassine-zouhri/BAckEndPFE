package com.bezkoder.springjwt.security.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.DocImage;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AgentInfoRequeste;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UpdateProfileRequest;
import com.bezkoder.springjwt.payload.response.AgentResponse;
import com.bezkoder.springjwt.payload.response.ImageResponse;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.UserResponse;

public interface UserService {
	
	public JwtResponse checkUser(LoginRequest loginRequest) ;
	public User AddUser(SignupRequest signUpRequest);
	public void UploadImageUser(MultipartFile imageUser , String UUIDimage) throws IOException;
	public void UpdateUserProfile(UpdateProfileRequest updateProfileRequest );
	public void UpdateUserImage(MultipartFile imageUser,String UUIDimage) throws IOException;
	public void deleteUserById(Long id);
	public List<UserResponse> GetAllUser();
	public UserResponse GetUserByID(Long id);
	public ImageResponse GetImageByUserID(Long id);
	public List<AgentResponse> GetAgentByRoleAgent();
	public AgentInfoRequeste  generateAgentInfoResponse(String username);
	User getuserByIdAgent(Long id);
	public List<AgentInfoRequeste> GetAllAgent();
}
