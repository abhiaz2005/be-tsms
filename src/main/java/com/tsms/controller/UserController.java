package com.tsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.LoginRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService ;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
		Response<?> response  = userService.register(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		Response<?> response  = userService.login(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

}
