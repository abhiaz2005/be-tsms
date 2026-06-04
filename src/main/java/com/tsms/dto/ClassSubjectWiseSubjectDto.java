package com.tsms.dto;

import com.tsms.entity.ClassSubject;
import com.tsms.entity.Subject;

import java.util.List;

public class ClassSubjectWiseSubjectDto {
    private Long id;

    private Subject subject;

    private List<ClassSubject> classSubjects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<ClassSubject> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(List<ClassSubject> classSubjects) {
        this.classSubjects = classSubjects;
    }

    public ClassSubjectWiseSubjectDto() {
    }

    public ClassSubjectWiseSubjectDto(Long id, Subject subject, List<ClassSubject> classSubjects) {
        this.id = id;
        this.subject = subject;
        this.classSubjects = classSubjects;
    }
}
