package com.tsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.ExamDto;
import com.tsms.dto.Response;
import com.tsms.service.ExamService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/exam")
public class ExamController {

	@Autowired
	private ExamService examService ;
	
	@PostMapping("add")
	public ResponseEntity<?> createExam(@Valid @RequestBody ExamDto exam) {
		Response<?> response  = examService.createExam(exam);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("all")
	public ResponseEntity<?> getAllExam() {
		Response<?> response  = examService.getAllExam();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
