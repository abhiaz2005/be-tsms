package com.tsms.dto;

public class ClassSubjectDto {
	private Long id;
	private Long subjectId;
	private Long classId;
	private Double fullMark;

	// getters + setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Double getFullMark() {
		return fullMark;
	}

	public void setFullMark(Double fullMark) {
		this.fullMark = fullMark;
	}

	public ClassSubjectDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassSubjectDto(Long id, Long subjectId, Long classId, Double fullMark) {
		super();
		this.id = id;
		this.subjectId = subjectId;
		this.classId = classId;
		this.fullMark = fullMark;
	}

}