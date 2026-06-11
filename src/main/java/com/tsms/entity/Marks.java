package com.tsms.entity;

import com.tsms.dto.MarksDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "student_marks")
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
	@JoinColumn(name = "exam_subject_id")
	private ExamSubject exam;

	@Column(name = "secured_mark")
	private Double securedMark;

	@CreationTimestamp
	@Column(updatable = false,name = "created_at")
	private Date createdAt;

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

	public ExamSubject getExam() {
		return exam;
	}

	public void setExam(ExamSubject exam) {
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

	public Marks(Long id, User student, ExamSubject exam, Double securedMark) {
		this.id = id;
		this.student = student;
		this.exam = exam;
		this.securedMark = securedMark;
	}

	public MarksDto convertToDto() {
		return new MarksDto(id, student.convertToDto(), exam.convertToDto(exam.getExamMaster()), securedMark);
	}
}
