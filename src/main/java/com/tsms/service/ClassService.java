package com.tsms.service;

import com.tsms.dto.Response;
import com.tsms.entity.StudentClass;

import jakarta.validation.Valid;

public interface ClassService {

	Response<?> getAllClass();

	Response<?> createClass(@Valid StudentClass studentClass);

	Response<?> deleteClass(Long id);


}
