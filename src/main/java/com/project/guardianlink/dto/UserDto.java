package com.project.guardianlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private String username;
	private String password;
	private String email;
	private String orgName;
	private String concern;
	private String firstName;
	private String lastName;
	private Integer hours;
	private String backgroundCheck;
	private String linkedinUrl;
	
}