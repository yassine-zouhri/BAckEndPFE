package com.bezkoder.springjwt.security.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.exeption.UserNotFoundException;
import com.bezkoder.springjwt.models.DocImage;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AgentInfoRequeste;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UpdateProfileRequest;
import com.bezkoder.springjwt.payload.response.AgentResponse;
import com.bezkoder.springjwt.payload.response.ImageResponse;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.UserResponse;
import com.bezkoder.springjwt.repository.DocImageRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;

@Service
@Transactional
public class UserServiceImp implements UserService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DocImageRepository docImageRepository;
	
	@Autowired
	private PushNotificationService pushNotificationService;

	@Override
	public JwtResponse checkUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		String jwt = jwtUtils.generateJwtToken( userPrincipal.getUsername());

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles,jwtUtils.extractExpiration(jwt));
	}

	@Override
	public User AddUser(SignupRequest signUpRequest) {
		System.out.println("signUpRequest  =" + signUpRequest);

		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getFirstName(), signUpRequest.getLastName(),
				signUpRequest.getBirthDate(), signUpRequest.getCity(), signUpRequest.getCountry(),
				signUpRequest.getCompany(), signUpRequest.getJobPosition(), signUpRequest.getMobile(),
				signUpRequest.getUuidImage(), signUpRequest.getActif(),this.GenerateMatricule());

		String strRole = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRole == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_AGENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			switch (strRole) {
			case "administrateur":
				Role agentRole = roleRepository.findByName(ERole.ROLE_ADMINISTRATEUR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(agentRole);
				break;
			case "superviseur":
				Role superviseurRole = roleRepository.findByName(ERole.ROLE_SUPERVISEUR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(superviseurRole);

				break;
			case "chef de poste":
				Role chefPosteRole = roleRepository.findByName(ERole.ROLE_CHEF_POSTE)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(chefPosteRole);
				break;
			case "manager":
				Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(managerRole);
				break;
			default:
				Role adminRole = roleRepository.findByName(ERole.ROLE_AGENT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(adminRole);
			}
		}
		user.setRoles(roles);
		userRepository.save(user);
		return null;
	}

	@Override
	public void UploadImageUser(MultipartFile imageUser, String UUIDimage) throws IOException {
		if (imageUser != null) {
			DocImage image = new DocImage(UUIDimage, imageUser.getContentType(), imageUser.getOriginalFilename(),
					imageUser.getBytes(),pushNotificationService.saveImage(imageUser.getBytes()));
			try{
			    Thread.sleep(500);
			    docImageRepository.save(image);
			}catch(InterruptedException e){
			    e.printStackTrace();
			}
		}
	}

	@Override
	public void UpdateUserProfile(UpdateProfileRequest updateProfileRequest) {
		User user = userRepository.findByUsername(updateProfileRequest.getUsername()).get();
		String strRoles = updateProfileRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_AGENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			
			switch (strRoles) {
			case "administrateur":
				Role agentRole = roleRepository.findByName(ERole.ROLE_ADMINISTRATEUR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(agentRole);
				break;
			case "superviseur":
				Role superviseurRole = roleRepository.findByName(ERole.ROLE_SUPERVISEUR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(superviseurRole);

				break;
			case "chef de poste":
				Role chefPosteRole = roleRepository.findByName(ERole.ROLE_CHEF_POSTE)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(chefPosteRole);
				break;
			case "manager":
				Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(managerRole);
				break;
			default:
				Role adminRole = roleRepository.findByName(ERole.ROLE_AGENT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(adminRole);
			}

		}
		user.setPassword(encoder.encode(updateProfileRequest.getPassword()));
		user.setRoles(roles);
		user.setFirstName(updateProfileRequest.getFirstName());
		user.setLastName(updateProfileRequest.getLastName());
		user.setBirthDate(updateProfileRequest.getBirthDate());
		user.setCity(updateProfileRequest.getCity());
		user.setCompany(updateProfileRequest.getCompany());
		user.setCountry(updateProfileRequest.getCountry());
		user.setJobPosition(updateProfileRequest.getJobPosition());
		user.setMobile(updateProfileRequest.getMobile());
		userRepository.save(user);
	}

	@Override
	public void deleteUserById(Long id) {
		String UUIDimage = userRepository.findById(id).get().getUUIDimage();
		userRepository.deleteById(id);
		docImageRepository.delete(docImageRepository.findByUuidImage(UUIDimage).get());
	}

	@Override
	public void UpdateUserImage(MultipartFile imageUser, String UUIDimage) throws IOException {
		DocImage myImage = docImageRepository.findByUuidImage(UUIDimage).get();
		if (imageUser != null) {
			myImage.setData(imageUser.getBytes());
			myImage.setDocName(imageUser.getOriginalFilename());
			myImage.setDocType(imageUser.getContentType());
			docImageRepository.save(myImage);
		}
	}

	@Override
	public List<UserResponse> GetAllUser() {
		List<UserResponse> listuserResponse = new ArrayList<UserResponse>();
		if (userRepository.findAll().size() > 0) {
			userRepository.findAll().forEach((user) -> {
				listuserResponse.add(user.toUserResponse());
			});
		}
		return listuserResponse;
	}

	@Override
	public UserResponse GetUserByID(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
		return user.toUserResponse();
	}

	@Override
	public ImageResponse GetImageByUserID(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
		DocImage image =docImageRepository.findByUuidImage(user.getUUIDimage())
				.orElseThrow(() -> new UserNotFoundException("Image by id " + id + " was not found"));
		ImageResponse imageResponse = new ImageResponse(image.getDocType(),image.getDocName(),image.getData());
		return imageResponse;
	}
	
	public String GenerateMatricule() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String numbers = "0123456789";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    int length1 = 2;int length2 = 4;

	    for(int i = 0; i < length1; i++) {
	      int index = random.nextInt(alphabet.length());
	      char randomChar = alphabet.charAt(index);
	      sb.append(randomChar);
	    }
	    for(int i = 0; i < length2; i++) {
	    	int index = random.nextInt(numbers.length());
	    	char randomChar = numbers.charAt(index);
	    	sb.append(randomChar);
	    }

	    String randomString = sb.toString();
	    System.out.println("Random String is: " + randomString);
	    return randomString;
	}

	@Override
	public List<AgentResponse> GetAgentByRoleAgent() {
		Set<Role> roles = new HashSet<>();roles.add(roleRepository.findByName(ERole.ROLE_AGENT).get());
		List<User> users = userRepository.findByRoles(roles);
		List<AgentResponse> listAgent = new ArrayList<AgentResponse>();
		if(users.size()>0) {
			users.forEach((user) -> {
				byte[] data = docImageRepository.findByUuidImage(user.getUUIDimage()).get().getData();
				listAgent.add(new AgentResponse(user.getUsername(), user.getMatricule(), data,user.getId(),user.getLastName()+" "+user.getFirstName()));
			});
		}
		return listAgent;
	}

	@Override
	public AgentInfoRequeste generateAgentInfoResponse(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User by username " + username + " was not found"));
		String URLimageAgent = docImageRepository.findByUuidImage(user.getUUIDimage()).get().getURLimage();
		Set<Role> roles = user.getRoles();
		String agentRole = null;
		for (Role role : roles) {
			agentRole = role.getName().name();
		 }
		AgentInfoRequeste agent = new AgentInfoRequeste(user.getId(), user.getUsername(), agentRole, user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate().toString(), user.getCity(), user.getCountry(), user.getCompany(), user.getJobPosition(), user.getMobile(),URLimageAgent);
		return agent;
	}

	@Override
	public User getuserByIdAgent(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
		return user;
	}

	@Override
	public List<AgentInfoRequeste> GetAllAgent() {
		Set<Role> roles = new HashSet<>();roles.add(roleRepository.findByName(ERole.ROLE_AGENT).get());
		List<User> users = userRepository.findByRoles(roles);
		List<AgentInfoRequeste> listAgent = new ArrayList<AgentInfoRequeste>();
		if(users.size()>0) {
			users.forEach((user) -> {
				String agentImage = docImageRepository.findByUuidImage(user.getUUIDimage()).get().getURLimage();
				listAgent.add(new AgentInfoRequeste(user.getId(), user.getUsername(),user.getRoles().toString() , user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate().toString(), user.getCity(), user.getCountry(), user.getCompany(), user.getJobPosition(), user.getMobile(),agentImage ));
			});
		}
		return listAgent;
	}
	


}
