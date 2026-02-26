package com.tsms.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tsms.dto.OtpRequest;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailService;

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	
	public void sendPasswordResetEmail(OtpRequest user) {
		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
			log.error("Invalid user details. Cannot send password reset email.");
		}


		Thread emailThread = new Thread(() -> {
			try {
				MimeMessage mimeMessage = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				// Email Content
				String subject = "Account Creation - Otp verification";
				String content = String.format("Dear %s,<br><br>"
						+ "Your account has been successfully created. To activate your account, please reset your password.<br><br>"
						+ "<b>Click the link below to reset your password:</b><br>"
						+ "If you did not request this, please ignore this email.<br><br>"
						+ "Best regards,<br>Support Team", user.getName());

				helper.setTo(user.getEmail());
				helper.setSubject(subject);
				helper.setText(content, true);

				javaMailService.send(mimeMessage);
				log.info("Password reset email sent successfully to user: {}", user.getEmail());
			} catch (Exception e) {
				log.error("Failed to send password reset email to user: {}", user.getEmail(), e);
			}
		});

		emailThread.setPriority(Thread.NORM_PRIORITY); 
		emailThread.start(); 
	}
}
