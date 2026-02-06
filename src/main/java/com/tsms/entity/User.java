package com.tsms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(name = "name")
	private String name ;
	
	@Column(name = "password")
	private String password ;
	
	@Column(name = "age")
	private Integer age ;
	
	@Column(name = "dob")
	private Date dob ;
	
	@ManyToOne
    @JoinColumn(name = "present_address_id")
    private Address presentAddress;

    @ManyToOne
    @JoinColumn(name = "permanent_address_id")
    private Address permanentAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Address getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(Address presentAddress) {
		this.presentAddress = presentAddress;
	}

	public Address getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(Address permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String name, String password, Integer age, Date dob, Address presentAddress,
			Address permanentAddress) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.age = age;
		this.dob = dob;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
	}
    
	
    
}
