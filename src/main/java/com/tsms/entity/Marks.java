package com.tsms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "marks")
public class Marks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Student
	@ManyToOne
	@JoinColumn(name = "student_id")
	private User student;

	// Exam
	@ManyToOne
	@JoinColumn(name = "exam_id")
	private Exam exam;

	@Column(name = "secured_mark")
	private Double securedMark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	
	public Double getSecuredMark() {
		return securedMark;
	}

	public void setSecuredMark(Double securedMark) {
		this.securedMark = securedMark;
	}

	public Marks() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Marks(Long id, User student, Exam exam, Double securedMark) {
		super();
		this.id = id;
		this.student = student;
		this.exam = exam;
		this.securedMark = securedMark;
	}

}
