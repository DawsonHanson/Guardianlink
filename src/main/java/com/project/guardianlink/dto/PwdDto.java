package com.project.guardianlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PwdDto {
	
	private String oldPwd;
	private String newPwd;
	
}