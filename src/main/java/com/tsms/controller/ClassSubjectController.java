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

import com.tsms.dto.ClassSubjectDto;
import com.tsms.dto.Response;
import com.tsms.service.ClassSubjectService;

@RestController
@RequestMapping("api/class-subject")
public class ClassSubjectController {

	@Autowired
	private ClassSubjectService classSubjectService;

	@PostMapping("add")
	public ResponseEntity<?> add(@RequestBody ClassSubjectDto dto) {
		Response<?> res = classSubjectService.add(dto);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@GetMapping("all")
	public ResponseEntity<?> getAll() {
		Response<?> res = classSubjectService.getAll();
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@GetMapping("by-class/{classId}")
	public ResponseEntity<?> getByClass(@PathVariable Long classId) {
		Response<?> res = classSubjectService.getByClass(classId);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@PutMapping("edit")
	public ResponseEntity<?> edit(@RequestBody ClassSubjectDto dto) {
		Response<?> res = classSubjectService.edit(dto);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Response<?> res = classSubjectService.delete(id);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}
}