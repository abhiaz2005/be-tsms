package com.tsms.entity;

import com.tsms.dto.ExamSubjectDto;
import jakarta.persistence.*;

@Entity
@Table(name = "exam_subjects")
public class ExamSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_master_id")
    private ExamMaster examMaster;

    @ManyToOne
    @JoinColumn(name = "class_subject_id")
    private ClassSubject classSubject;

    @Column(name = "full_mark")
    private Double fullMark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamMaster getExamMaster() {
        return examMaster;
    }

    public void setExamMaster(ExamMaster examMaster) {
        this.examMaster = examMaster;
    }

    public ClassSubject getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(ClassSubject classSubject) {
        this.classSubject = classSubject;
    }

    public Double getFullMark() {
        return fullMark;
    }

    public void setFullMark(Double fullMark) {
        this.fullMark = fullMark;
    }

    public ExamSubject(Long id, ExamMaster examMaster, ClassSubject classSubject, Double fullMark) {
        this.id = id;
        this.examMaster = examMaster;
        this.classSubject = classSubject;
        this.fullMark = fullMark;
    }

    public ExamSubject() {
    }

    public ExamSubjectDto convertToDto(ExamMaster examMaster) {
        ExamSubjectDto dto = new ExamSubjectDto();
        if(examMaster != null) {
            dto.setExamMasterDto(examMaster.convertToDto());
        }
        dto.setId(id);
        dto.setClassSubject(classSubject);
        dto.setFullMark(fullMark);
        return dto;
    }
}