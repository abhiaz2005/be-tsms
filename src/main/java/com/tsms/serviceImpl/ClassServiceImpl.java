package com.tsms.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tsms.dto.Response;
import com.tsms.entity.Exam;
import com.tsms.entity.StudentClass;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.ClassRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ClassService;

import jakarta.validation.Valid;

@Service
public class ClassServiceImpl implements ClassService {

	@Autowired
	private ClassRepository classRepository ;
	
	@Autowired
	private CustomizedUserDetailsService customizedUserDetailsService ;
	
	private final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);

	@Override
	public Response<?> getAllClass() {
		try {
			List<StudentClass> studentClasses = classRepository.findAll();
			return new Response<>(HttpStatus.OK.value(), "OK", studentClasses);
		}catch(Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> createClass(@Valid @RequestBody StudentClass studentClass) {
		try {
			classRepository.save(studentClass) ;
			return new Response<>(HttpStatus.OK.value(), "Saved Successfully", null);
		}catch(Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	public Response<?> deleteClass(Long id) {
		try {
			Optional<User> userDetails = customizedUserDetailsService.getUserDetails();
			if (userDetails.isEmpty()) {
				logger.info("Autorization err");
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please login again", null);
			}
			if (!userDetails.get().getRole().equals(Role.ADMIN)) {
				logger.info("Another user another than admin trying to create exam.");
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "You've no permission to create exam", null);
			}
			Optional<StudentClass> classOptional = classRepository.findById(id);
			if (classOptional.isEmpty()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "class not present with this id", null);
			}
			classRepository.delete(classOptional.get());
			return new Response<>(HttpStatus.OK.value(), "Class deleted successfuly", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

}
