package com.tsms.service;

import com.tsms.dto.MarkDto;
import com.tsms.dto.Response;

import java.util.List;

public interface ReportService {
    Response<?> addMarks(List<MarkDto> dtos);
    Response<?> getAllMarks(String year, String className);
    Response<?> getMarksByStudent(Long studentId);
    Response<?> editMark(MarkDto dto);
    Response<?> deleteMark(Long id);

    Response<?> generateReport(Long studentId);
}