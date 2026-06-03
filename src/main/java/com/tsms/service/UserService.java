package com.tsms.service;

import com.tsms.dto.LoginRequest;
import com.tsms.dto.OtpRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.dto.UserDto;

import jakarta.validation.Valid;

public interface UserService {

	Response<?> register(RegisterRequest request);

	Response<?> login(@Valid LoginRequest request);

	Response<?> getAllStudent();

	Response<?> getStudentById(Long id);

	Response<?> verifyOtp(@Valid OtpRequest request);

	Response<?> sendOtp(OtpRequest request);

	Response<?> registerAdmin( RegisterRequest request);

	Response<?> updateStudent(UserDto request);

}
