package com.tsms.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tsms.dto.OtpRequest;
import com.tsms.entity.Exam;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailService;

	@Autowired
	private UserRepository userRepository;

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

	public void sendAccountConfirmationMail(User user) {
		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
			log.error("Invalid user details. Cannot send account confirmation email.");
			return;
		}

		Thread emailThread = new Thread(() -> {
			try {
				MimeMessage mimeMessage = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String subject = "Account Created Successfully";

				String content = String.format(
						"""
								<!DOCTYPE html>
								<html>
								<head>
								    <meta charset="UTF-8">
								    <meta name="viewport" content="width=device-width, initial-scale=1.0">
								</head>
								<body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, sans-serif;">

								    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:20px;">
								        <tr>
								            <td align="center">

								                <table width="600" cellpadding="0" cellspacing="0"
								                    style="background:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.08);">

								                    <!-- Header -->
								                    <tr>
								                        <td style="background:#4CAF50; padding:20px; text-align:center; color:white;">
								                            <h2 style="margin:0;">Account Created Successfully</h2>
								                        </td>
								                    </tr>

								                    <!-- Body -->
								                    <tr>
								                        <td style="padding:30px;">
								                            <p style="font-size:16px; color:#333;">
								                                Dear <strong>%s</strong>,
								                            </p>

								                            <p style="font-size:15px; color:#555; line-height:1.6;">
								                                Your account has been successfully created. You can now login and start using our services.
								                            </p>

								                            <!-- User Details Card -->
								                            <table width="100%%" cellpadding="0" cellspacing="0"
								                                style="margin-top:20px; background:#f9fafb; border-radius:8px; padding:15px;">

								                                <tr>
								                                    <td style="padding:8px 0; font-size:14px;">
								                                        <strong>Name:</strong> %s
								                                    </td>
								                                </tr>

								                                <tr>
								                                    <td style="padding:8px 0; font-size:14px;">
								                                        <strong>Email:</strong> %s
								                                    </td>
								                                </tr>

								                                <tr>
								                                    <td style="padding:8px 0; font-size:14px;">
								                                        <strong>Phone:</strong> %s
								                                    </td>
								                                </tr>

								                            </table>

								                            <!-- Username & password-->
															<table width="100%%" cellpadding="0" cellspacing="0"
															    style="margin-top:20px; background:#f9fafb; border-radius:8px; padding:15px;">

															    <tr>
															        <td style="padding:8px 0; font-size:14px;">
															            <strong>Username:</strong> %s
															        </td>
															    </tr>

															    <tr>
															        <td style="padding:8px 0; font-size:14px;">
															            <strong>Password:</strong> %s
															        </td>
															    </tr>

															</table>

								                            <p style="margin-top:25px; font-size:14px; color:#666;">
								                                If you did not create this account, please contact our support team immediately.
								                            </p>

								                            <p style="margin-top:30px; font-size:14px;">
								                                Best regards,<br>
								                                <strong>Support Team</strong>
								                            </p>
								                        </td>
								                    </tr>

								                    <!-- Footer -->
								                    <tr>
								                        <td style="background:#f1f1f1; padding:15px; text-align:center; font-size:12px; color:#888;">
								                            © 2026 Your Company. All rights reserved.
								                        </td>
								                    </tr>

								                </table>

								            </td>
								        </tr>
								    </table>

								</body>
								</html>
								""",
						user.getName(), user.getName(), user.getEmail(), user.getPhoneNo(), user.getEmail(),
						user.getPassword());

				helper.setTo(user.getEmail());
				helper.setSubject(subject);
				helper.setText(content, true);

				javaMailService.send(mimeMessage);
				log.info("User successfully mail sent: {}", user.getEmail());

			} catch (Exception e) {
				log.error("Failed to send account confirmation email to user: {}", user.getEmail(), e);
			}
		});

		emailThread.setPriority(Thread.NORM_PRIORITY);
		emailThread.start();
	}

	public void sendExamCreatedMail(Exam exam) {

		List<User> admins = userRepository.findByRole(Role.ADMIN);

		if (admins == null || admins.isEmpty()) {
			log.warn("No admin users found to send exam mail");
			return;
		}

		String[] toMails = admins.stream().filter(e -> e.getEmail() != null && !e.getEmail().isEmpty())
				.map(e -> e.getEmail()).toArray(String[]::new);
		Thread emailThread = new Thread(() -> {

//	        for (User admin : admins) {

			try {
				MimeMessage mimeMessage = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String subject = "New Exam Created";

				String content = String.format("""
						<!DOCTYPE html>
						<html>
						<body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial">

						<table width="100%%" style="padding:20px">
						<tr>
						<td align="center">

						<table width="600" style="background:white;border-radius:10px;
						box-shadow:0 2px 8px rgba(0,0,0,0.08);overflow:hidden">

						<tr>
						<td style="background:#1976D2;color:white;padding:20px;text-align:center">
						<h2 style="margin:0">New Exam Created</h2>
						</td>
						</tr>

						<tr>
						<td style="padding:30px">

						<p>Dear Admin</strong>,</p>

						<p>A new exam has been created in the system.</p>

						<table width="100%%" style="background:#f9fafb;
						padding:15px;border-radius:8px">

						<tr>
						<td style="padding:8px 0">
						<strong>Class:</strong> %s
						</td>
						</tr>

						<tr>
						<td style="padding:8px 0">
						<strong>Full Marks:</strong> %s
						</td>
						</tr>

						</table>

						<p style="margin-top:20px">
						Please login to the admin panel for more details.
						</p>


						</td>
						</tr>

						<tr>
						<td style="background:#f1f1f1;text-align:center;
						padding:15px;font-size:12px;color:#777">
						© 2026 Exam System
						</td>
						</tr>

						</table>

						</td>
						</tr>
						</table>

						</body>
						</html>
						""",exam.getClassSubject().getStudentClass() != null ? exam.getClassSubject().getStudentClass().getStudentClass() : "ALL", exam.getFullMark());

				helper.setTo(toMails);
				helper.setSubject(subject);
				helper.setText(content, true);

				javaMailService.send(mimeMessage);

				log.info("Exam mail sent to admin: {}", toMails);

			} catch (Exception e) {
				log.error("Failed to send exam mail to {}", toMails, e);
			}
//	        }

		});

		emailThread.start();
	}

	public void sendCredentials(String email, String name, String userId, String password) {

		Thread emailThread = new Thread(() -> {

			try {

				MimeMessage mimeMessage = javaMailService.createMimeMessage();

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String subject = "Registration Successful - Login Credentials";

				String content = String.format("""
						<!DOCTYPE html>
						<html>
						<body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial">

						<table width="100%%" style="padding:20px">
						<tr>
						<td align="center">

						<table width="600"
						style="background:white;
						border-radius:10px;
						box-shadow:0 2px 8px rgba(0,0,0,0.08);
						overflow:hidden">

						<tr>
						<td style="
						    background:#1976D2;
						    color:white;
						    padding:20px;
						    text-align:center">
						    <h2 style="margin:0">
						        Registration Successful
						    </h2>
						</td>
						</tr>

						<tr>
						<td style="padding:30px">

						<p>
						    Dear <strong>%s</strong>,
						</p>

						<p>
						    Your registration has been completed successfully.
						</p>

						<table width="100%%"
						style="
						    background:#f9fafb;
						    padding:15px;
						    border-radius:8px">

						<tr>
						<td style="padding:8px 0">
						    <strong>User ID:</strong> %s
						</td>
						</tr>

						<tr>
						<td style="padding:8px 0">
						    <strong>Password:</strong> %s
						</td>
						</tr>

						</table>

						<p style="margin-top:20px;color:#d32f2f">
						    Please change your password after first login.
						</p>

						<p>
						    Regards,<br>
						    <strong>Genius Guidelines</strong>
						</p>

						</td>
						</tr>

						<tr>
						<td style="
						    background:#f1f1f1;
						    text-align:center;
						    padding:15px;
						    font-size:12px;
						    color:#777">
						    © Genius Guidelines
						</td>
						</tr>

						</table>

						</td>
						</tr>
						</table>

						</body>
						</html>
						""", name, userId, password);

				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(content, true);

				javaMailService.send(mimeMessage);

				log.info("Credentials mail sent successfully to {}", email);

			} catch (Exception e) {

				log.error("Failed to send credentials mail to {}", email, e);
			}

		});

		emailThread.start();
	}

	public void sendOtp(String email, String otp) {

		Thread emailThread = new Thread(() -> {

			try {

				MimeMessage mimeMessage = javaMailService.createMimeMessage();

				MimeMessageHelper helper =
						new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String subject = "Password Reset OTP";

				String content = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial">

                    <table width="100%%" style="padding:20px">
                    <tr>
                    <td align="center">

                    <table width="600"
                    style="
                        background:white;
                        border-radius:10px;
                        box-shadow:0 2px 8px rgba(0,0,0,0.08);
                        overflow:hidden">

                    <tr>
                    <td style="
                        background:#1976D2;
                        color:white;
                        padding:20px;
                        text-align:center">
                        <h2 style="margin:0">
                            Password Reset OTP
                        </h2>
                    </td>
                    </tr>

                    <tr>
                    <td style="padding:30px">

                    <p>
                        Dear User,
                    </p>

                    <p>
                        We received a request to reset your password.
                    </p>

                    <p>
                        Please use the following OTP to continue:
                    </p>

                    <div style="
                        text-align:center;
                        margin:25px 0;">

                        <span style="
                            display:inline-block;
                            background:#f5f5f5;
                            border:1px solid #ddd;
                            padding:15px 30px;
                            font-size:28px;
                            font-weight:bold;
                            letter-spacing:5px;
                            border-radius:8px;">
                            %s
                        </span>

                    </div>

                    <p style="color:#d32f2f">
                        This OTP is valid for 5 minutes.
                    </p>

                    <p>
                        If you did not request a password reset,
                        please ignore this email.
                    </p>

                    <p>
                        Regards,<br>
                        <strong>Genius Guidelines</strong>
                    </p>

                    </td>
                    </tr>

                    <tr>
                    <td style="
                        background:#f1f1f1;
                        text-align:center;
                        padding:15px;
                        font-size:12px;
                        color:#777">
                        © Genius Guidelines
                    </td>
                    </tr>

                    </table>

                    </td>
                    </tr>
                    </table>

                    </body>
                    </html>
                    """, otp);

				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(content, true);

				javaMailService.send(mimeMessage);

				log.info("OTP mail sent successfully to {}", email);

			} catch (Exception e) {

				log.error("Failed to send OTP mail to {}", email, e);
			}

		});

		emailThread.start();
	}
}
