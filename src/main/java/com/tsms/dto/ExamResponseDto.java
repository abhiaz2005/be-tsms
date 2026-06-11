package com.tsms.dto;

import java.util.List;

import java.util.List;

public class ExamResponseDto {

    private Long id;

    private String examName;

    private String examType;

    private Long classId;

    private String className;

    private List<ExamSubjectResponseDto> subjects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ExamSubjectResponseDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<ExamSubjectResponseDto> subjects) {
        this.subjects = subjects;
    }
}