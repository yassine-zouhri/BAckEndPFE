package com.bezkoder.springjwt.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AgentInfoRequeste;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UpdateProfileRequest;
import com.bezkoder.springjwt.payload.response.AgentResponse;
import com.bezkoder.springjwt.payload.response.ImageResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.UserResponse;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserService;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/user")
@Api(value = "HelloWorld Resource", description = "User's operations")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService ;
	
	@PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("innnnnnnnnnnnnn");
		System.out.println("signUpRequest.toString()  "+signUpRequest.toString());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		System.out.println(signUpRequest.toString());
		userService.AddUser(signUpRequest);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping(value="/UploadImage/{uuidImage}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,"multipart/form-data"})
	public ResponseEntity<?> UploadUserImage(@RequestParam(value ="imageFile", required = true) MultipartFile imageFile,@PathVariable(value="uuidImage") String uuidImage) throws IOException{
		System.out.println("aaaaaa          +"+uuidImage);
		System.out.println("aaaaaaa       ="+imageFile.getResource());
		System.out.println("innnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn +"+imageFile.getOriginalFilename());
		userService.UploadImageUser(imageFile,uuidImage);
		return ResponseEntity.ok(new MessageResponse("Image Uploaded successfully!"));
	}
	
	@PostMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestBody Map<String, Object> inputData) {
        String email = (String)inputData.get("email");
        Boolean bool = userRepository.existsByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(bool); 
    }
	
	@PostMapping("/usernamecheck")
    public ResponseEntity<?> usernameCheck(@RequestBody Map<String, Object> inputData) {
        String email = (String)inputData.get("username");
        Boolean bool = userRepository.existsByUsername(email);
        return ResponseEntity.status(HttpStatus.OK).body(bool); 
    }
	
	
	@PutMapping("/updateProfileUser")
	public ResponseEntity<?> UpdateUserProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
		System.out.println("updateProfileRequest "+updateProfileRequest);
		userService.UpdateUserProfile(updateProfileRequest);
		return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
	}
	
	@PutMapping(value="/updateImageUser/{uuidImage}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,"multipart/form-data"})
	public ResponseEntity<?> UpdateImageUser(@RequestParam(value ="imageFile", required = true) MultipartFile imageFile,@PathVariable(value="uuidImage") String uuidImage) throws IOException{
		System.out.println("imageFileimageFileimageFile +="+imageFile.getOriginalFilename());
		System.out.println("uuidImageuuidImageuuidImage +="+uuidImage);
		userService.UpdateUserImage( imageFile,uuidImage);
		return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
	}
	
	
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id){
		userService.deleteUserById(id);
		return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	}
	
	@GetMapping("/allUsers")
	public ResponseEntity<List<UserResponse>> GetAllUser(){
		return ResponseEntity.ok().body(userService.GetAllUser());
	}
	
	@GetMapping("/userById/{id}")
	public ResponseEntity<UserResponse> GetUserByID(@PathVariable Long id){
		return ResponseEntity.ok().body(userService.GetUserByID(id));
	}
	
	@GetMapping("/getImage/{id}")
	public ResponseEntity<ImageResponse> GetImageByUserID(@PathVariable Long id){	
		return ResponseEntity.ok().body(userService.GetImageByUserID(id));
	}
	
	@GetMapping("/getAgenttoAssign")
	public ResponseEntity<List<AgentResponse>> GetAgents(){
		List<AgentResponse> a= userService.GetAgentByRoleAgent();
		System.out.println(a.size());
		return ResponseEntity.ok().body(userService.GetAgentByRoleAgent());
	}
	
	@GetMapping("/agentById/{id}")
	public ResponseEntity<User> GetAgentByID(@PathVariable Long id){
		return ResponseEntity.ok().body(userService.getuserByIdAgent(id));
	}
	
	@GetMapping("/allAgent")
	public ResponseEntity<List<AgentInfoRequeste>> GetAllAgent(){
		return ResponseEntity.ok().body(userService.GetAllAgent());
	}
}
