package com.tsms.service;

import com.tsms.dto.ExamDto;
import com.tsms.dto.Response;

import jakarta.validation.Valid;

public interface ExamService {

	Response<?> createExam(ExamDto exam);

	Response<?> getAllExam();

	Response<?> editExam(@Valid ExamDto exam);

	Response<?> deleteExam(Long id);

}
