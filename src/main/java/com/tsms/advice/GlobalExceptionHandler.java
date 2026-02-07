package com.tsms.advice;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tsms.dto.ErrorResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		List<String>  errorList = new ArrayList<>();
        
        ex.getBindingResult().getFieldErrors()
            .forEach(err -> {
            	errorList.add(err.getDefaultMessage()) ;
            });

        ErrorResponse error = new ErrorResponse(
                400,
                errorList,
                System.currentTimeMillis()
        );

		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {

		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(), System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}