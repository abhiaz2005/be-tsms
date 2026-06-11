package com.tsms.dto;

import com.tsms.entity.ClassSubject;

public class ExamSubjectDto {

    private Long id;

    private Long classSubjectId;

    private Double fullMark;

    private ClassSubject  classSubject;

    private ExamMasterDto examMasterDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassSubjectId() {
        return classSubjectId;
    }

    public void setClassSubjectId(Long classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public Double getFullMark() {
        return fullMark;
    }

    public void setFullMark(Double fullMark) {
        this.fullMark = fullMark;
    }

    public ClassSubject getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(ClassSubject classSubject) {
        this.classSubject = classSubject;
    }

    public ExamMasterDto getExamMasterDto() {
        return examMasterDto;
    }

    public void setExamMasterDto(ExamMasterDto examMasterDto) {
        this.examMasterDto = examMasterDto;
    }
}