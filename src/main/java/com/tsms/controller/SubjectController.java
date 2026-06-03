package com.tsms.controller;

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

import com.tsms.dto.Response;
import com.tsms.dto.SubjectDto;
import com.tsms.service.SubjectService;

@RestController
@RequestMapping("api/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@PostMapping("add")
	public ResponseEntity<?> add(@RequestBody SubjectDto dto) {
		Response<?> res = subjectService.addSubject(dto);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@GetMapping("all")
	public ResponseEntity<?> getAll() {
		Response<?> res = subjectService.getAllSubject();
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@PutMapping("edit")
	public ResponseEntity<?> edit(@RequestBody SubjectDto dto) {
		Response<?> res = subjectService.editSubject(dto);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Response<?> res = subjectService.deleteSubject(id);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}
}