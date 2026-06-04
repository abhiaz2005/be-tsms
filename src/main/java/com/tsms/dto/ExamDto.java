package com.tsms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExamDto {

	private Long id;

	@NotBlank(message = "examName is required")
	@NotNull(message = "examName is required")
	private String examName;

//	private String studentClass;

	private Double fullMark;
	
	private Long classSubjectId;

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

//	public String getStudentClass() {
//		return studentClass;
//	}
//
//	public void setStudentClass(String studentClass) {
//		this.studentClass = studentClass;
//	}

	public Double getFullMark() {
		return fullMark;
	}

	public void setFullMark(Double fullMark) {
		this.fullMark = fullMark;
	}
	
	

	public Long getClassSubjectId() {
		return classSubjectId;
	}

	public void setClassSubjectId(Long classSubjectId) {
		this.classSubjectId = classSubjectId;
	}

	public ExamDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExamDto(Long id, String examName,  Double fullMark) {
		super();
		this.id = id;
		this.examName = examName;
		this.fullMark = fullMark;
	}

}
