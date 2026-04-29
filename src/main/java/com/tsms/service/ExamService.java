package com.tsms.service;

import com.tsms.dto.ExamDto;
import com.tsms.dto.Response;

public interface ExamService {

	Response<?> createExam(ExamDto exam);

}
