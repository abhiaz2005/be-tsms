package com.tsms.service;

import com.tsms.dto.ExamDto;
import com.tsms.dto.ExamMasterDto;
import com.tsms.dto.Response;

import jakarta.validation.Valid;

public interface ExamService {

	Response<?> createExam(@Valid ExamMasterDto dto);

	Response<?> getAllExam();

	Response<?> editExam(@Valid ExamMasterDto exam);

	Response<?> deleteExam(Long id);

}
