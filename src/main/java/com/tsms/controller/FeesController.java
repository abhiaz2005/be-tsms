package com.tsms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsms.dto.FeesDto;
import com.tsms.dto.Response;
import com.tsms.service.FeesService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/fees")
public class FeesController {
	
	@Autowired
	private FeesService feesService ;
	
	@GetMapping("all")
	public ResponseEntity<?> getAllFees() {
		Response<?> response  = feesService.getAllFeesV2();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<?> getAllFeesByStudent(@RequestParam(required = false) Long id) {
		Response<?> response  = feesService.getAllFeesByStudent(id);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> createNewFee(@RequestBody List<FeesDto> fees) {
		Response<?> response  = feesService.createBulkFee(fees);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
