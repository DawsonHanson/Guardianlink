package com.project.guardianlink.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.guardianlink.dto.UserDto;
import com.project.guardianlink.entity.User;
import com.project.guardianlink.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email);
	}
	
	@Override
	public List<User> findAllByRole(String role) {
		return (List<User>) repository.findAllByRole(role);
	}
	
	@Override
	public User findFirstByRole(String role) {
		return repository.findFirstByRole(role);
		// used to find first Admin user for forgot password implementation
	}

	@Override
	public User save(UserDto userDto) {
		String role;
		
		// sets role based off of null values from form submission 
		if(userDto.getOrgName() == null && userDto.getFirstName() == null) {
			role = "ROLE_ADMIN";
		} else if(userDto.getFirstName() == null) {
			role = "ROLE_COMPANY";
		} else {
			role = "ROLE_VOLUNTEER";
		}
		
		User user = new User(0, userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getEmail(), userDto.getOrgName(), userDto.getConcern(), userDto.getFirstName(), userDto.getLastName(), userDto.getHours(), userDto.getBackgroundCheck() , userDto.getLinkedinUrl(), role);
		return repository.save(user);
	}
	
	public User save(User user) {
		return repository.save(user);
		// used for updating existing user information through edit profile interface
	}

	@Override
	public void delete(User user) {
		repository.delete(user);
	}
	
	@Override
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
		// used for encoding new password through change password interface
	};
}