package com.tsms.serviceImpl;

import java.util.*;
import java.util.stream.Collectors;

import com.tsms.dto.ClassSubjectWiseSubjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsms.dto.ClassSubjectDto;
import com.tsms.dto.Response;
import com.tsms.entity.ClassSubject;
import com.tsms.entity.StudentClass;
import com.tsms.entity.Subject;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.ClassRepository;
import com.tsms.repository.ClassSubjectRepository;
import com.tsms.repository.SubjectRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ClassSubjectService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassSubjectServiceImpl implements ClassSubjectService {

    @Autowired
    private ClassSubjectRepository classSubjectRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository studentClassRepository;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(ClassSubjectServiceImpl.class);

    private boolean isAdmin() {
        Optional<User> user = customizedUserDetailsService.getUserDetails();
        return user.isPresent() && user.get().getRole().equals(Role.ADMIN);
    }

    @Override
    public Response<?> add(ClassSubjectDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            Optional<Subject> subject = subjectRepository.findById(dto.getSubjectId());
            if (subject.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject not found", null);

            Optional<StudentClass> studentClass = studentClassRepository.findById(dto.getClassId());
            if (studentClass.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Class not found", null);

            // duplicate check
            Optional<ClassSubject> duplicate = classSubjectRepository
                    .findBySubject_IdAndStudentClass_Id(dto.getSubjectId(), dto.getClassId());
            if (duplicate.isPresent())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Subject already assigned to this class", null);

            ClassSubject cs = new ClassSubject();
            cs.setSubject(subject.get());
            cs.setStudentClass(studentClass.get());
            cs.setCreatedAt(new Date());
            classSubjectRepository.save(cs);

            logger.info("ClassSubject added: {} -> {}", subject.get().getName(), studentClass.get().getStudentClass());
            return new Response<>(HttpStatus.OK.value(), "Assigned successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getAll() {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<ClassSubject> list = classSubjectRepository.findAll();
            return new Response<>(HttpStatus.OK.value(), "Success", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getByClass(Long classId) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<ClassSubject> list = classSubjectRepository.findByStudentClass_Id(classId);
            return new Response<>(HttpStatus.OK.value(), "Success", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> edit(ClassSubjectDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            if (dto.getId() == null)
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Provide id", null);

            Optional<ClassSubject> optional = classSubjectRepository.findById(dto.getId());
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Not found", null);

            ClassSubject cs = optional.get();
            classSubjectRepository.save(cs);

            return new Response<>(HttpStatus.OK.value(), "Updated successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> delete(Long id) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            Optional<ClassSubject> optional = classSubjectRepository.findById(id);
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Not found", null);

            classSubjectRepository.delete(optional.get());
            return new Response<>(HttpStatus.OK.value(), "Deleted successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> bySubectGetAllOrGetSubjectById() {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);


            List<ClassSubject> list = classSubjectRepository.findAll();

            Map<Long, List<ClassSubject>> grouped =
                    list.stream()
                            .collect(Collectors.groupingBy(
                                    cs -> cs.getSubject().getId()));

            List<ClassSubjectWiseSubjectDto> result = new ArrayList<>();

            for (Map.Entry<Long, List<ClassSubject>> entry : grouped.entrySet()) {

                List<ClassSubject> classSubjects = entry.getValue();

                ClassSubjectWiseSubjectDto dto =
                        new ClassSubjectWiseSubjectDto();

                dto.setId(entry.getKey());
                dto.setSubject(classSubjects.get(0).getSubject());
                dto.setClassSubjects(classSubjects);

                result.add(dto);
            }
            return new Response<>(HttpStatus.OK.value(), "Success", result);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    @Transactional
    public Response<?> deleteBySubjectId(Long subjectId) {

        try {
            Optional<Subject> optionalSubject =
                    subjectRepository.findById(subjectId);

            if (optionalSubject.isEmpty()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Not found", null);
            }
            int deleted = classSubjectRepository.deleteBySubjectId(subjectId);
            return new Response<>(HttpStatus.OK.value(), deleted > 0 ? "Deleted successfully" : "Nothing to delete", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getBySubjectId(Long subjectId) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<ClassSubject> classSubjectList = classSubjectRepository.findBySubject_Id(subjectId);
            return new Response<>(HttpStatus.OK.value(), "Success", classSubjectList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }
}