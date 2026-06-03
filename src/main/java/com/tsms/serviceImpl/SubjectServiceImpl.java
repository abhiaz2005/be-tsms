package com.tsms.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsms.dto.Response;
import com.tsms.dto.SubjectDto;
import com.tsms.entity.Subject;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.SubjectRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private boolean isAdmin() {
        Optional<User> user = customizedUserDetailsService.getUserDetails();
        return user.isPresent() && user.get().getRole().equals(Role.ADMIN);
    }

    @Override
    public Response<?> addSubject(SubjectDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "No permission", null);

            Optional<Subject> existing = subjectRepository.findByNameIgnoreCase(dto.getName());
            if (existing.isPresent())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject already exists", null);

            Subject subject = new Subject();
            subject.setName(dto.getName());
            subject.setCreatedAt(new Date());
            subjectRepository.save(subject);

            logger.info("Subject added: {}", dto.getName());
            return new Response<>(HttpStatus.OK.value(), "Subject added successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getAllSubject() {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<Subject> subjects = subjectRepository.findAll();
            return new Response<>(HttpStatus.OK.value(), "Success", subjects);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> editSubject(SubjectDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            if (dto.getId() == null)
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Provide subject id", null);

            Optional<Subject> optional = subjectRepository.findById(dto.getId());
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject not found", null);

            Subject subject = optional.get();
            if (dto.getName() != null) subject.setName(dto.getName());
            subjectRepository.save(subject);

            return new Response<>(HttpStatus.OK.value(), "Subject updated successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> deleteSubject(Long id) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            Optional<Subject> optional = subjectRepository.findById(id);
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject not found", null);

            subjectRepository.delete(optional.get());
            return new Response<>(HttpStatus.OK.value(), "Subject deleted successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }
}
