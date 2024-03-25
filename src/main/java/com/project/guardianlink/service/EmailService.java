package com.project.guardianlink.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TemplateEngine templateEngine;

	@Async
	public void sendHtmlEmail(String toEmail, String subject, String templateName, Context context) throws MessagingException, IOException {
	  MimeMessage message = mailSender.createMimeMessage();
	  MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

	  helper.setTo(toEmail);
	  helper.setSubject(subject);
	  String htmlContent = templateEngine.process(templateName, context);
	  helper.setText(htmlContent, true);
	  mailSender.send(message);
	  
	}
// exceptions caught in controller for Url redirection purposes
}