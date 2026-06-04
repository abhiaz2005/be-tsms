package com.tsms.service;

import com.tsms.dto.ClassSubjectDto;
import com.tsms.dto.Response;

public interface ClassSubjectService {
	Response<?> add(ClassSubjectDto dto);

	Response<?> getAll();

	Response<?> getByClass(Long classId);

	Response<?> edit(ClassSubjectDto dto);

	Response<?> delete(Long id);

    Response<?> bySubectGetAllOrGetSubjectById();

	Response<?> deleteBySubjectId(Long subjectId);

	Response<?> getBySubjectId(Long subjectId);
}
