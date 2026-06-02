package com.tsms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_class")
public class StudentClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "student_class")
	private String studentClass; 
	
	@Column(name = "is_Active")
	private Boolean isActive;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public StudentClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentClass(Long id, String studentClass, Boolean isActive) {
		super();
		this.id = id;
		this.studentClass = studentClass;
		this.isActive = isActive;
	} 
	
	
	
	

}
