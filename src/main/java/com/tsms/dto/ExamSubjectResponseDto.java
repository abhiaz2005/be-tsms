package com.tsms.dto;


public class ExamSubjectResponseDto {

    private Long id;

    private Long classSubjectId;

    private String subjectName;

    private Double fullMark;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getFullMark() {
        return fullMark;
    }

    public void setFullMark(Double fullMark) {
        this.fullMark = fullMark;
    }
}