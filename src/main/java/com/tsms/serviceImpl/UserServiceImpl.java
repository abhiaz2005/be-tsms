package com.tsms.serviceImpl;

import java.time.ZoneId;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tsms.dto.LoginReponse;
import com.tsms.dto.LoginRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.UserRepository;
import com.tsms.security.JwtService;
import com.tsms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private JwtService jwtService ;

	@Override
	public Response<?> register(RegisterRequest request) {
		try {
			Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
			if (userOptional.isPresent()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "User already exists", null);
			}
			User user = new User();
			user.setEmail(request.getEmail());
			user.setDob(request.getDob());
			user.setName(request.getName());
			String firstName = request.getName().trim().split("\\s+")[0];
			int year = request.getDob().toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate().getYear();
			String pass =firstName+"@"+year;
			user.setPassword(passwordEncoder.encode(pass));
			user.setRole(Role.USER);
			user.setIsActive(true);
			User savedUser = userRepository.save(user);
			logger.info("User saved");
			return new Response<>(HttpStatus.OK.value(), "Registration successful", null);

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
		}
	}


	
	@Override
	public Response<Object> login(LoginRequest loginRequest) {
		Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
		try {
			if (userOptional.isPresent() && userOptional.get().getIsActive()) {
				User user = userOptional.get();

				
					if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

						String token = jwtService.generateToken(loginRequest.getEmail());
						LoginReponse response = new LoginReponse() ;
						response.setToken(token);
						response.setUserName(user.getName());
						response.setEmail(user.getEmail());
						response.setRole(user.getRole());

						return new Response<>(HttpStatus.OK.value(), "Login Success.", response);
					}
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Invalid credentials.", null);
				}
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "invalid credential", null);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something went wrong", null);
		}
	}

}
