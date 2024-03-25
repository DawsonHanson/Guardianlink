package com.project.guardianlink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
	@SequenceGenerator(name = "sequence_generator", sequenceName = "sequence_generator", allocationSize= 1, initialValue = 1)
	@Column(name = "user_Id")
	private Integer userId;
	
	@Column(name = "user_name", unique = true)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	@Column(name = "org_name")
	private String orgName;
	
	private String concern;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	private Integer hours;
	
	@Column(name = "background_check")
	private String backgroundCheck;
	
	@Column(name = "linkedin_url")
	private String linkedinUrl;
	
	private String role;
	
}