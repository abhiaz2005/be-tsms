package com.tsms.dto;

public class MarkDto {

    private Long id;

    private Long studentId;

    private Long examSubjectId;

    private Double securedMark;

    public MarkDto(Long id, Long studentId, Long examSubjectId, Double securedMark) {
        this.id = id;
        this.studentId = studentId;
        this.examSubjectId = examSubjectId;
        this.securedMark = securedMark;
    }

    public MarkDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(Long examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public Double getSecuredMark() {
        return securedMark;
    }

    public void setSecuredMark(Double securedMark) {
        this.securedMark = securedMark;
    }
}