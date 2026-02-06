package com.tsms.service;

import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;

public interface UserService {

	Response<?> register(RegisterRequest request);

}
