package com.project.guardianlink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
    AuthenticationSuccessHandler simpleAuthenticationSuccessHandler(){
        return new SimpleAuthenticationSuccessHandler();
    }
	
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/signin/**").permitAll()
                .requestMatchers("/home/**").permitAll()
                .requestMatchers("/global/**").permitAll()
                .requestMatchers("/signup/**").permitAll()
                .requestMatchers("/sendemailtoadmin/**").permitAll()
                .requestMatchers("/forgotpassword/**").permitAll()
                .requestMatchers("/volunteersignup/**").permitAll()
                .requestMatchers("/companysignup/**").permitAll()
                .requestMatchers("/accessdenied/**").permitAll()
                .requestMatchers("/userdeleted/**").authenticated()
                .requestMatchers("/changepassword/**").authenticated()
        		.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
        		.requestMatchers("/admindeleteuser/**").hasAuthority("ROLE_ADMIN")
        		.requestMatchers("/admincreateuser/**").hasAuthority("ROLE_ADMIN")
        		.requestMatchers("/editadmin/**").hasAuthority("ROLE_ADMIN")
        		.requestMatchers("/sendemailtovolunteer/**").hasAuthority("ROLE_COMPANY")
        		.requestMatchers("/company/**").hasAuthority("ROLE_COMPANY")
        		.requestMatchers("/editcompany/**").hasAuthority("ROLE_COMPANY")
        		.requestMatchers("/sendemailtocompany/**").hasAuthority("ROLE_VOLUNTEER")
        		.requestMatchers("/editvolunteer/**").hasAuthority("ROLE_VOLUNTEER")
        		.requestMatchers("/volunteer/**").hasAuthority("ROLE_VOLUNTEER"))
                .formLogin(login -> login
                        .loginPage("/signin")
                        .loginProcessingUrl("/signin")
                		.successHandler(simpleAuthenticationSuccessHandler()))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/signin?logout").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/accessdenied"))
                .sessionManagement(session -> session
                		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
                
		return http.build();
	}
}