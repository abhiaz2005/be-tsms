package com.tsms.serviceImpl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsms.dto.ExamDto;
import com.tsms.dto.Response;
import com.tsms.entity.Exam;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.ExamRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ExamService;

@Service
public class ExamServiceImpl implements ExamService {

	@Autowired
	private ExamRepository examRepository ;
	
	@Autowired
	private EmailService emailService ;
	
	@Autowired
	private CustomizedUserDetailsService customizedUserDetailsService ;
	
	private final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class); 
	
	
	@Override
	public Response<?> createExam(ExamDto examDto) {
		try {
			Optional<User> userDetails = customizedUserDetailsService.getUserDetails();
			if(userDetails.isEmpty()) {
				logger.info("Autorization err");
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please login again", null);
			}
			if(!userDetails.get().getRole().equals(Role.ADMIN)) {
				logger.info("Another user another than admin trying to create exam.");
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "You've no permission to create exam", null);
			}
			Exam exam = new Exam();
			exam.setStudentClass(examDto.getStudentClass()!=null ? examDto.getStudentClass():null);
			exam.setFullMark(examDto.getFullMark());
			exam.setExamName(examDto.getExamName());
			exam.setCreatedAt(new Date());
			examRepository.save(exam);
			logger.info("New exam saved for {} class & fullmark {}",examDto.getStudentClass()!=null ? examDto.getStudentClass():"ALL" ,examDto.getFullMark() );
			
			new Thread(()->{
				emailService.sendExamCreatedMail(exam);
			}).start();
			return new Response<>(HttpStatus.OK.value(), "Exam saved successfully", null);
		}catch(Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

}
