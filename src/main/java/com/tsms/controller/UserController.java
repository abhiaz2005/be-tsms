package com.tsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.LoginRequest;
import com.tsms.dto.OtpRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.dto.UserDto;
import com.tsms.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserService userService ;
	
	@PostMapping("auth/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
		Response<?> response  = userService.register(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("auth/register-s-admin")
	public ResponseEntity<?> registerAdmin( @RequestBody RegisterRequest request) {
		Response<?> response  = userService.registerAdmin(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("auth/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		Response<?> response  = userService.login(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("verify/otp")
	public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpRequest request) {
		Response<?> response  = userService.verifyOtp(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("send/otp")
	public ResponseEntity<?> sendOtp(@RequestBody OtpRequest request) {
		Response<?> response  = userService.sendOtp(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("api/get/all/student")
	public ResponseEntity<?> getAllStudent() {
		Response<?> response  = userService.getAllStudent();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("api/get/student")
	public ResponseEntity<?> getStudentById(@RequestParam(required = false)Long id) {
		Response<?> response  = userService.getStudentById(id);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PutMapping("api/update/user")
	public ResponseEntity<?> updateStudent(@RequestBody UserDto request) {
		Response<?> response  = userService.updateStudent(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	
}
