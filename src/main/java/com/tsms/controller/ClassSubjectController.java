package com.tsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("by-subject/{subjectId}")
	public ResponseEntity<?> getBySubjectId(@PathVariable Long subjectId) {
		Response<?> res = classSubjectService.getBySubjectId(subjectId);
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

	@DeleteMapping("delete/all/by/subjectId")
	public ResponseEntity<?> deleteBySubjectId(@RequestParam Long subjectId) {
		Response<?> res = classSubjectService.deleteBySubjectId(subjectId);
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}

	@GetMapping("group/by/subject")
	public ResponseEntity<?> bySubectGetAllOrGetSubjectById() {
		Response<?> res = classSubjectService.bySubectGetAllOrGetSubjectById();
		return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
	}
}