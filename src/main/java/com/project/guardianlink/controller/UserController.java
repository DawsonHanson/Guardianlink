package com.project.guardianlink.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.Context;
import com.project.guardianlink.dto.EmailDto;
import com.project.guardianlink.dto.SignInDto;
import com.project.guardianlink.dto.PwdDto;
import com.project.guardianlink.dto.UserDto;
import com.project.guardianlink.entity.User;
import com.project.guardianlink.service.EmailService;
import com.project.guardianlink.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private UserService userService;

	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/signin")
	public String signin(@ModelAttribute("signInDto") SignInDto signInDto) {
		return "signin";
	}
	
	@GetMapping("/volunteersignup")
	public String registerVolunteer(@ModelAttribute("userDto") UserDto userDto) {
		return "volunteersignup";
	}
	
	@PostMapping("/volunteersignup")
	public String registerVolunteerSave(UserDto userDto) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 
		
		if(checkUsername != null) {
			return "redirect:/volunteersignup?usernameexists";
		}
		if(checkEmail != null) {
			return "redirect:/volunteersignup?emailexists";
		}
		userService.save(userDto);
		return "redirect:/volunteersignup?success";
	}
	
	@GetMapping("/companysignup")
	public String registerCompany(@ModelAttribute("userDto") UserDto userDto) {
		return "companysignup";
	}
	
	@PostMapping("/companysignup")
	public String registerCompanySave(UserDto userDto) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 
		
		if(checkUsername != null) {
			return "redirect:/companysignup?usernameexists";
		}
		if(checkEmail != null) {
			return "redirect:/companysignup?emailexists";
		}
		userService.save(userDto);
		return "redirect:/companysignup?success";
	}
	
	@GetMapping("/forgotpassword")
	public String forgotPassword(@ModelAttribute("emailDto") EmailDto emailDto) {
		return "forgotpassword";
	}
	
	@PostMapping("/forgotpassword")
	public String sendEmailToAdmin(EmailDto emailDto) throws IOException {
		
		User admin = userService.findFirstByRole("ROLE_ADMIN");
		
		String toEmail = admin.getEmail();
		String username = emailDto.getUsername();
		String fromEmail = emailDto.getEmail();
		
		User checkUsername = userService.findByUsername(emailDto.getUsername());
		User checkEmail = userService.findByEmail(emailDto.getEmail()); 
		
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("fromEmail", fromEmail);
		
		if(checkUsername != null && checkEmail != null) {
			if(checkUsername == checkEmail) {
				try {
					emailService.sendHtmlEmail(toEmail, "User forgot password", "emailtemplatetoadmin", context);
				} catch (Exception e) {
					return "redirect:/forgotpassword?emailerror";
				}
				return "redirect:/forgotpassword?emailsuccess";
			}
		} 
		return "redirect:/forgotpassword?nouser";
	}
	
	@GetMapping("/changepassword")
	public String changePassword(@ModelAttribute("pwdDto") PwdDto pwdDto) {
		return "changepassword";
	}
	
	@PostMapping("/changepassword")
	public String changePasswordSave(PwdDto pwdDto, Principal principal) {
		
		User existingUser = userService.findByUsername(principal.getName());
		
		if(!BCrypt.checkpw(pwdDto.getOldPwd(), existingUser.getPassword())) {
			return "redirect:/changepassword?error";
		}
		
		existingUser.setPassword(userService.encodePassword(pwdDto.getNewPwd()));
		userService.save(existingUser);
		
		return "redirect:/changepassword?success";
	}
	
	@GetMapping("/userdeleted")
	public String userdeleted(Principal principal, HttpSession session) {
		User user = userService.findByUsername(principal.getName());
		userService.delete(user);
		session.invalidate();
		return "userdeleted";
	}
	
	@GetMapping("/accessdenied")
	public String accessdenied() {
		return "accessdenied";
	}
	
	
	//----------------------------------------------------------------------------------------------------
	
	
	@GetMapping("/admin")
	public String admin(@ModelAttribute("userDto") UserDto userDto, Model model, Principal principal) {
		
		List<User> usersAdmin = userService.findAllByRole("ROLE_ADMIN");
		model.addAttribute("usersAdmin", usersAdmin);
		
		List<User> usersCompany = userService.findAllByRole("ROLE_COMPANY");
		model.addAttribute("usersCompany", usersCompany);
		
		List<User> usersVolunteer = userService.findAllByRole("ROLE_VOLUNTEER");
		model.addAttribute("usersVolunteer", usersVolunteer);
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("currentuser", user);
		
		return "admin";
	}
	
	@PostMapping("/admin")
	public String adminEditUserPassword(UserDto userDto) {
		try {
			User existingUser = userService.findByUsername(userDto.getUsername());
			existingUser.setPassword(userService.encodePassword(userDto.getPassword()));
			userService.save(existingUser);
		} catch(Exception e) { 
			return "redirect:/admin?errornulluser";
		}
		return "redirect:/admin?success";
	}
	
	@PostMapping("/admindeleteuser")
	public String adminDeleteUser(@ModelAttribute("userDto") UserDto userDto) {
		try {
			List<User> usersAdmin = userService.findAllByRole("ROLE_ADMIN");
			User user = userService.findByUsername(userDto.getUsername());
			
			if(usersAdmin.size() == 1 && user.getRole().equals("ROLE_ADMIN")) {
				return "redirect:/admin?error";
			} else {
				userService.delete(user);
				return "redirect:/admin";
			}
		} catch(Exception e) {
			return "redirect:/admin?errornulluser";
		}
	}
		
	@GetMapping("/admincreateuser")
	public String test(@ModelAttribute("userDto") UserDto userDto) {
		return "admincreateuser";
	}
	
	@PostMapping("/admincreateuser")
	public String adminCreateUser(UserDto userDto) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 
		
		if(checkUsername != null) {
			return "redirect:/admincreateuser?usernameexists";
		}
		if(checkEmail != null) {
			return "redirect:/admincreateuser?emailexists";
		}
		userService.save(userDto);
		return "redirect:/admincreateuser?success";
	}
	
	@GetMapping("/editadmin")
	public String editAdmin(@ModelAttribute("userDto") UserDto userDto, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "editadmin";
	}
	
	@PostMapping("/editadmin")
	public String editAdminSave(UserDto userDto, Principal principal) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 

		User existingUser = userService.findByUsername(principal.getName());
		
		if(checkUsername != null && checkUsername.getUsername() != existingUser.getUsername()) {
			return "redirect:/editadmin?usernameexists";
		}
		if(checkEmail != null && checkEmail.getEmail() != existingUser.getEmail()) {
			return "redirect:/editadmin?emailexists";
		}
		
		existingUser.setUsername(userDto.getUsername());
		existingUser.setEmail(userDto.getEmail());
		userService.save(existingUser);
		
		return "redirect:/editadmin?success";
	}
	
	
	//----------------------------------------------------------------------------------------------------
	
	
	@GetMapping("/company")
	public String company(Model model, Principal principal) {
		
		List<User> users = userService.findAllByRole("ROLE_VOLUNTEER");
		model.addAttribute("users", users);
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("currentuser", user);
		return "company";
	}
	
	@PostMapping("/sendemailtovolunteer")
	public String sendEmailToVolunteer(@ModelAttribute("emailDto") EmailDto emailDto, Principal principal) throws IOException {
		
		User user = userService.findByUsername(principal.getName());
		String fromEmail = user.getEmail();
		String orgName = user.getOrgName();
		String firstName = emailDto.getFirstName();
		String lastName = emailDto.getLastName();
		String toEmail = emailDto.getEmail();
		
		
        Context context = new Context();
        context.setVariable("orgName", orgName);
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("fromEmail", fromEmail);
        
		try {
			emailService.sendHtmlEmail(toEmail, "Wanting to connect", "emailtemplatetovolunteer", context);
		} catch (Exception e) {
			return "redirect:/company?emailerror";
		}
		return "redirect:/company?emailsuccess";
	}
	
	@GetMapping("/editcompany")
	public String editCompany(@ModelAttribute("userDto") UserDto userDto, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "editcompany";
	}
	
	@PostMapping("/editcompany")
	public String editCompanySave(UserDto userDto, Principal principal) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 

		User existingUser = userService.findByUsername(principal.getName());
		
		if(checkUsername != null && checkUsername.getUsername() != existingUser.getUsername()) {
			return "redirect:/editcompany?usernameexists";
		}
		if(checkEmail != null && checkEmail.getEmail() != existingUser.getEmail()) {
			return "redirect:/editcompany?emailexists";
		}
		
		existingUser.setUsername(userDto.getUsername());
		existingUser.setEmail(userDto.getEmail());
		existingUser.setOrgName(userDto.getOrgName());
		existingUser.setConcern(userDto.getConcern());
		
		userService.save(existingUser);
		return "redirect:/editcompany?success";
	}
	
	//----------------------------------------------------------------------------------------------------
	
	
	@GetMapping("/volunteer")
	public String volunteer(Model model, Principal principal) {
		
		List<User> users = userService.findAllByRole("ROLE_COMPANY");
		model.addAttribute("users", users);
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("currentuser", user);
		return "volunteer";
	}
	
	@PostMapping("/sendemailtocompany")
	public String sendEmailToCompany(@ModelAttribute("emailDto") EmailDto emailDto, Principal principal) throws IOException {
		
		User user = userService.findByUsername(principal.getName());
		String fromEmail = user.getEmail();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String toEmail = emailDto.getEmail();
		String orgName = emailDto.getOrgName();
		
        Context context = new Context();
        context.setVariable("orgName", orgName);
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("fromEmail", fromEmail);
        
		try {
			emailService.sendHtmlEmail(toEmail, "Wanting to connect", "emailtemplatetocompany", context);
		} catch (Exception e) {
			return "redirect:/volunteer?emailerror";
		}
		return "redirect:/volunteer?emailsuccess";
	}
	
	@GetMapping("/editvolunteer")
	public String editVolunteer(@ModelAttribute("userDto") UserDto userDto, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "editvolunteer";
	}
	
	@PostMapping("/editvolunteer")
	public String editVolunteerSave(UserDto userDto, Principal principal) {
		User checkUsername = userService.findByUsername(userDto.getUsername());
		User checkEmail = userService.findByEmail(userDto.getEmail()); 

		User existingUser = userService.findByUsername(principal.getName());
		
		if(checkUsername != null && checkUsername.getUsername() != existingUser.getUsername()) {
			return "redirect:/editvolunteer?usernameexists";
		}
		if(checkEmail != null && checkEmail.getEmail() != existingUser.getEmail()) {
			return "redirect:/editvolunteer?emailexists";
		}
		
		existingUser.setUsername(userDto.getUsername());
		existingUser.setEmail(userDto.getEmail());
		existingUser.setFirstName(userDto.getFirstName());
		existingUser.setLastName(userDto.getLastName());
		existingUser.setHours(userDto.getHours());
		existingUser.setBackgroundCheck(userDto.getBackgroundCheck());
		existingUser.setLinkedinUrl(userDto.getLinkedinUrl());
		
		userService.save(existingUser);
		return "redirect:/editvolunteer?success";
	}
}