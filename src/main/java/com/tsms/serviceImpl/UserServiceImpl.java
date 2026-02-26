package com.tsms.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tsms.cache.InMemCache;
import com.tsms.dto.LoginReponse;
import com.tsms.dto.LoginRequest;
import com.tsms.dto.OtpRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.dto.UserDto;
import com.tsms.entity.Address;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.AddressRepository;
import com.tsms.repository.UserRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.security.JwtService;
import com.tsms.service.UserService;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AddressRepository addressRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomizedUserDetailsService customizedUserDetailsService;
	
	@Autowired
	private InMemCache inMemCache ;
	
	@Autowired
	private EmailService emailService ;

	@Override
	public Response<?> register(RegisterRequest request) {
		try {
			Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
			if (userOptional.isPresent()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this email already exists", null);
			}
			Optional<User> userPhoneOptional = userRepository.findByPhoneNo(request.getPhoneNo());
			if (userPhoneOptional.isPresent()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this phone number already exists", null);
			}
			
			User user = new User();
			user.setEmail(request.getEmail());
			user.setDob(request.getDob());
			user.setName(request.getName());
			String firstName = request.getName().trim().split("\\s+")[0];
			int year = request.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
			String pass = firstName + "@" + year;
			user.setPassword(passwordEncoder.encode(pass));
			user.setRole(Role.USER);
			user.setIsActive(true);
			user.setSection(request.getSection());
			user.setGender(request.getGender());
			user.setPhoneNo(request.getPhoneNo());
			user.setFatherName(request.getFatherName());
			user.setMotherName(request.getMotherName());
			user.setStudiedFrom(request.getStudiedFrom());

			// Address
			Address presentAddress = request.getPresentAddress().convertToEntity();
			Address permanentAddress = request.getPermanentAddress().convertToEntity();
			presentAddress = addressRepository.save(presentAddress);
			permanentAddress = addressRepository.save(permanentAddress);
			logger.info("address saved ");
			user.setPermanentAddress(permanentAddress);
			user.setPresentAddress(presentAddress);
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
					LoginReponse response = new LoginReponse();
					response.setId(user.getId());
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
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> getAllStudent() {
		try {
			Optional<User> userDetailsOptional = customizedUserDetailsService.getUserDetails();
			if (userDetailsOptional.isPresent() && userDetailsOptional.get().getRole().equals(Role.ADMIN)) {

				List<User> users = userRepository.findAll();
				List<UserDto> userList = users.stream().filter(e -> e != null).map(e -> {
					UserDto userDto = e.convertToDto();
					Date dob = e.getDob();
					if (dob != null) {
						LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate now = LocalDate.now();
						userDto.setAge(Period.between(birthDate, now).getYears());
					}else {
						userDto.setAge(null);	
					}
					return userDto;
				}).collect(Collectors.toList());
				return new Response<>(HttpStatus.OK.value(), "success.",
						userList.isEmpty() ? Collections.emptyList() : userList);
			}
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "You have no permission for the API", null);

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> getStudentById(Long id) {
		try {
			Optional<User> userOptional = userRepository.findById(id);
			if (userOptional.isPresent()) {
				User user = userOptional.get();

				UserDto userDto = user.convertToDto();
				Date dob = user.getDob();
				if (dob != null) {
					LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate now = LocalDate.now();
					userDto.setAge(Period.between(birthDate, now).getYears());
				}else {
					userDto.setAge(null);	
				}
				return new Response<>(HttpStatus.OK.value(), "success.", userDto);
			}
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No user present", null);

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> verifyOtp(OtpRequest request) {
		try {
			boolean isVerified = inMemCache.verifyOtp(request.getEmail(), request.getOtp());
			if(isVerified) {
				return new Response<>(HttpStatus.OK.value(), "Email verified", null);
			}
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", null);

		}catch(Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> sendOtp(OtpRequest request) {
		try {
			emailService.sendPasswordResetEmail(request);
			inMemCache.storeOtp(request.getEmail());
			return new Response<>(HttpStatus.OK.value(), "email sent", null);
		}catch(Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something error in sending otp", null);
		}
	}

}
