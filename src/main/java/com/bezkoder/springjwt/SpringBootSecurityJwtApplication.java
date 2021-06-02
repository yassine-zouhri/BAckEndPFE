package com.bezkoder.springjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.repository.RoleRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SpringBootSecurityJwtApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(roleRepository.findAll().size()==0) {
			roleRepository.save(new Role(ERole.ROLE_ADMINISTRATEUR));
			roleRepository.save(new Role(ERole.ROLE_AGENT));
			roleRepository.save(new Role(ERole.ROLE_CHEF_POSTE));
			roleRepository.save(new Role(ERole.ROLE_MANAGER));
			roleRepository.save(new Role(ERole.ROLE_SUPERVISEUR));
		}
	}

}
