package com.tsms.dto;

import java.util.List;

import com.tsms.enums.ExamType;

public class ExamMasterDto {

    private Long id;

    private String examName;

    private Long classId;

    private ExamType examType;

    private List<ExamSubjectDto> examSubjects;

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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public List<ExamSubjectDto> getExamSubjects() {
        return examSubjects;
    }

    public void setExamSubjects(List<ExamSubjectDto> examSubjects) {
        this.examSubjects = examSubjects;
    }
}