package com.tsms.service;

import com.tsms.dto.LoginRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;

import jakarta.validation.Valid;

public interface UserService {

	Response<?> register(RegisterRequest request);

	Response<?> login(@Valid LoginRequest request);

	Response<?> getAllStudent();

	Response<?> getStudentById(Long id);

}
