package com.project.guardianlink.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.guardianlink.dto.UserDto;
import com.project.guardianlink.entity.User;

@Service
public interface UserService {
	User findByEmail(String email);
	User findByUsername(String username);
	User save(UserDto userDto);
	User save(User user);
	void delete(User user);
	List<User> findAllByRole(String role);
	User findFirstByRole(String role);
	String encodePassword(String password);
}