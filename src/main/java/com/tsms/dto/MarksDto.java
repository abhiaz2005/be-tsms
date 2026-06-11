package com.tsms.dto;

import com.tsms.entity.Exam;
import com.tsms.entity.ExamSubject;
import com.tsms.entity.User;
import jakarta.persistence.*;

public class MarksDto {
    private Long id;

    private UserDto student;

    private ExamSubjectDto exam;

    private Double securedMark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getStudent() {
        return student;
    }

    public void setStudent(UserDto student) {
        this.student = student;
    }

    public ExamSubjectDto getExam() {
        return exam;
    }

    public void setExam(ExamSubjectDto exam) {
        this.exam = exam;
    }

    public Double getSecuredMark() {
        return securedMark;
    }

    public void setSecuredMark(Double securedMark) {
        this.securedMark = securedMark;
    }

    public MarksDto(Long id, UserDto student, ExamSubjectDto exam, Double securedMark) {
        this.id = id;
        this.student = student;
        this.exam = exam;
        this.securedMark = securedMark;
    }

    public MarksDto() {
    }
}
