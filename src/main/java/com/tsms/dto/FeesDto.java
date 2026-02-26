package com.tsms.dto;

import java.util.Date;

import com.tsms.enums.PaymentMode;

public class FeesDto {
	private Long id;

	private UserDto student;

	private Integer month;

	private Integer year;

	private Double amount;

	private Date paymentDate;

	private PaymentMode mode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDto getStudent() {
		return student;
	}

	public void setStudent(UserDto student) {
		this.student = student;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public PaymentMode getMode() {
		return mode;
	}

	public void setMode(PaymentMode mode) {
		this.mode = mode;
	}

	public FeesDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeesDto(Long id, UserDto student, Integer month, Integer year, Double amount, Date paymentDate,
			PaymentMode mode) {
		super();
		this.id = id;
		this.student = student;
		this.month = month;
		this.year = year;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.mode = mode;
	}
	
	
}


