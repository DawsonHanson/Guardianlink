package com.project.guardianlink;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.guardianlink.entity.User;
import com.project.guardianlink.repository.UserRepository;

@SpringBootApplication
public class GuardianlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardianlinkApplication.class, args);
	}
	
	// adds users for testing;
	@Bean
	CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		
		return args ->{
			
			for(int x = 1; x <= 2; x++) {
				userRepository.save(new User(0, ("admin"+x), passwordEncoder.encode(("admin"+x)), ("admin"+x+"@test.com"), null, null, null, null, null, null, null, "ROLE_ADMIN"));
			}
			
			for(int x = 1; x <= 12; x++) {
				userRepository.save(new User(0, ("company"+x), passwordEncoder.encode(("company"+x)), ("company"+x+"@test.com"), ("TestCompany"+x), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras pellentesque nisi id elit vestibulum faucibus. Sed rutrum quam ac tincidunt dignissim. Vestibulum sed auctor magna. Sed ullamcorper nulla.", null, null, null, null, null, "ROLE_COMPANY"));
			}
			
			for(int x = 1; x <= 12; x++) {
				userRepository.save(new User(0, ("volunteer"+x), passwordEncoder.encode(("volunteer"+x)), ("volunteer"+x+"@test.com"), null, null, ("Firstname"+x), ("Lastname"+x), x, "Yes", ("http://www.fakeurl"+x+".com"), "ROLE_VOLUNTEER"));
			}
		};
	}
}