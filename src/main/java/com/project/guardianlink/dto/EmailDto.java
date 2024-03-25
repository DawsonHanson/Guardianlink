package com.project.guardianlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
	
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String orgName;
	
}