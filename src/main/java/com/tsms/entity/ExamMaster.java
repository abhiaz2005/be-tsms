package com.tsms.entity;

import com.tsms.dto.ExamMasterDto;
import com.tsms.enums.ExamType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "exam_master")
public class ExamMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name")
    private String examName;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;

    @Column(name = "created_at")
    private Date createdAt;

    //getter .setter

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

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ExamMaster() {
    }

    public ExamMaster(Long id, String examName, ExamType examType, StudentClass studentClass, Date createdAt) {
        this.id = id;
        this.examName = examName;
        this.examType = examType;
        this.studentClass = studentClass;
        this.createdAt = createdAt;
    }

    public ExamMasterDto convertToDto() {
        ExamMasterDto dto = new ExamMasterDto();
        dto.setId(id);
        dto.setExamName(examName);
        dto.setClassId(studentClass.getId());
        dto.setExamType(examType);
        return dto;
    }
}