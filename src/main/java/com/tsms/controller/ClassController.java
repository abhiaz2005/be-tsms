package com.tsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.Response;
import com.tsms.entity.StudentClass;
import com.tsms.service.ClassService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ClassController {
	
	@Autowired
	private ClassService classService ;
	
	@GetMapping("get/all/class")
	public ResponseEntity<?> getAllClass() {
		Response<?> response  = classService.getAllClass();
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@PostMapping("add/class")
	public ResponseEntity<?> createClass(@Valid @RequestBody StudentClass studentClass) {
		Response<?> response  = classService.createClass(studentClass);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@DeleteMapping("delete/class/{id}")
	public ResponseEntity<?> editClass(@PathVariable Long id) {
		Response<?> response  = classService.deleteClass(id);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	

}
