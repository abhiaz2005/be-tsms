package com.tsms.controller;

import com.tsms.dto.MarkDto;
import com.tsms.dto.Response;
import com.tsms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/marks/add")
    public ResponseEntity<?> addMarks(@RequestBody List<MarkDto> dtos) {
        Response<?> res = reportService.addMarks(dtos);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }

    @GetMapping("/marks/all")
    public ResponseEntity<?> getAllMarks(@RequestParam(required = false) String year, @RequestParam(required = false) String className) {
        Response<?> res = reportService.getAllMarks(year,className);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }

    @GetMapping("/marks/student/{studentId}")
    public ResponseEntity<?> getByStudent(@PathVariable Long studentId) {
        Response<?> res = reportService.getMarksByStudent(studentId);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }

    @PutMapping("/marks/edit")
    public ResponseEntity<?> editMark(@RequestBody MarkDto dto) {
        Response<?> res = reportService.editMark(dto);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }

    @DeleteMapping("/marks/delete/{id}")
    public ResponseEntity<?> deleteMark(@PathVariable Long id) {
        Response<?> res = reportService.deleteMark(id);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }

    @GetMapping("generate/report")
    public ResponseEntity<?> generateReport(@RequestParam(required = false) Long studentId) {
        Response<?> res = reportService.generateReport(studentId);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getResponseCode()));
    }
}
