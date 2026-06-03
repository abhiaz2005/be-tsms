package com.tsms.service;

import com.tsms.dto.Response;
import com.tsms.dto.SubjectDto;

public interface SubjectService {
	Response<?> addSubject(SubjectDto dto);

	Response<?> getAllSubject();

	Response<?> editSubject(SubjectDto dto);

	Response<?> deleteSubject(Long id);
}
