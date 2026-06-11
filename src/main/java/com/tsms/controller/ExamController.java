package com.tsms.controller;

import com.tsms.dto.ExamMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.ExamDto;
import com.tsms.dto.Response;
import com.tsms.service.ExamService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

	@Autowired
	private ExamService examService;

	@PostMapping("/add")
	public ResponseEntity<?> createExam(
			@Valid @RequestBody ExamMasterDto dto) {

		Response<?> response = examService.createExam(dto);

		return new ResponseEntity<>(
				response,
				HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllExam() {

		Response<?> response = examService.getAllExam();

		return new ResponseEntity<>(
				response,
				HttpStatus.valueOf(response.getResponseCode()));
	}

	@PutMapping("/edit")
	public ResponseEntity<?> editExam(@Valid @RequestBody ExamMasterDto dto) {

		Response<?> response = examService.editExam(dto);

		return new ResponseEntity<>(
				response,
				HttpStatus.valueOf(response.getResponseCode()));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteExam(
			@PathVariable Long id) {

		Response<?> response = examService.deleteExam(id);

		return new ResponseEntity<>(
				response,
				HttpStatus.valueOf(response.getResponseCode()));
	}
}
