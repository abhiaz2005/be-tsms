package com.tsms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exam")
public class Exam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "exam_name")
	private String examName; 

	@Column(name = "student_class")
	private String studentClass; 

	@Column(name = "full_mark")
	private Double fullMark;

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

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public Double getFullMark() {
		return fullMark;
	}

	public void setFullMark(Double fullMark) {
		this.fullMark = fullMark;
	}

	public Exam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Exam(Long id, String examName, String studentClass, Double fullMark) {
		super();
		this.id = id;
		this.examName = examName;
		this.studentClass = studentClass;
		this.fullMark = fullMark;
	}

	
}
