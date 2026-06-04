package com.tsms.dto;

public class MarkDto {
    private Long id;
    private Long studentId;
    private Long examId;
    private Double securedMark;
    // getters + setters

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

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Double getSecuredMark() {
        return securedMark;
    }

    public void setSecuredMark(Double securedMark) {
        this.securedMark = securedMark;
    }

    public MarkDto(Long id, Long studentId, Long examId, Double securedMark) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.securedMark = securedMark;
    }

    public MarkDto() {
    }
}
