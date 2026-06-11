package com.tsms.serviceImpl;

import java.util.*;

import com.tsms.dto.*;
import com.tsms.entity.*;
import com.tsms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsms.enums.Role;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ExamService;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamMasterRepository examMasterRepository;

    @Autowired
    private ExamSubjectRepository examSubjectRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Autowired
    private ClassSubjectRepository classSubjectRepository;

    @Autowired
    private ClassRepository studentClassRepository;

    @Override
    @Transactional
    public Response<?> createExam(ExamMasterDto dto) {

        try {

            Optional<User> userDetails =
                    customizedUserDetailsService.getUserDetails();

            if (userDetails.isEmpty()) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Please login again",
                        null);
            }

            if (!Role.ADMIN.equals(userDetails.get().getRole())) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "You've no permission to create exam",
                        null);
            }

            if (dto == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request",
                        null);
            }

            if (dto.getExamName() == null ||
                    dto.getExamName().trim().isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam name is required",
                        null);
            }

            if (dto.getClassId() == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Class is required",
                        null);
            }

            if (dto.getExamType() == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam type is required",
                        null);
            }

            if (dto.getExamSubjects() == null ||
                    dto.getExamSubjects().isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "At least one subject is required",
                        null);
            }

            Optional<StudentClass> studentClassOptional =
                    studentClassRepository.findById(dto.getClassId());

            if (studentClassOptional.isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Class not found",
                        null);
            }

            StudentClass studentClass =
                    studentClassOptional.get();

            // Optional duplicate exam validation

            boolean exists =
                    examMasterRepository
                            .existsByExamNameAndStudentClass(
                                    dto.getExamName(),
                                    studentClass);

            if (exists) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam already exists for this class",
                        null);
            }


            ExamMaster examMaster = new ExamMaster();

            examMaster.setExamName(dto.getExamName().trim());
            examMaster.setExamType(dto.getExamType());
            examMaster.setStudentClass(studentClass);
            examMaster.setCreatedAt(new Date());

            ExamMaster savedExam =
                    examMasterRepository.save(examMaster);

            List<ExamSubject> examSubjects =
                    new ArrayList<>();

            Set<Long> uniqueSubjectIds =
                    new HashSet<>();

            for (ExamSubjectDto subjectDto :
                    dto.getExamSubjects()) {

                if (subjectDto.getClassSubjectId() == null) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Class subject id is required",
                            null);
                }

                if (subjectDto.getFullMark() == null ||
                        subjectDto.getFullMark() <= 0) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Full mark must be greater than zero",
                            null);
                }

                // duplicate subject validation
                if (!uniqueSubjectIds.add(
                        subjectDto.getClassSubjectId())) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Duplicate subject selected",
                            null);
                }

                Optional<ClassSubject> classSubjectOptional =
                        classSubjectRepository.findById(
                                subjectDto.getClassSubjectId());

                if (classSubjectOptional.isEmpty()) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "ClassSubject not found. Id : "
                                    + subjectDto.getClassSubjectId(),
                            null);
                }

                ClassSubject classSubject =
                        classSubjectOptional.get();

                // Subject belongs to selected class validation
                if (!classSubject
                        .getStudentClass()
                        .getId()
                        .equals(dto.getClassId())) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Selected subject does not belong to selected class",
                            null);
                }

                ExamSubject examSubject =
                        new ExamSubject();

                examSubject.setExamMaster(savedExam);
                examSubject.setClassSubject(classSubject);
                examSubject.setFullMark(
                        subjectDto.getFullMark());

                examSubjects.add(examSubject);
            }

            examSubjectRepository.saveAll(examSubjects);

            return new Response<>(
                    HttpStatus.OK.value(),
                    "Exam created successfully",
                    savedExam);

        } catch (Exception e) {

            logger.error(
                    "Error while creating exam",
                    e);

            return new Response<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Something went wrong",
                    null);
        }
    }


    @Override
    @Transactional
    public Response<?> editExam(@Valid ExamMasterDto dto) {

        try {

            Optional<User> userDetails =
                    customizedUserDetailsService.getUserDetails();

            if (userDetails.isEmpty()) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Please login again",
                        null);
            }

            if (!Role.ADMIN.equals(userDetails.get().getRole())) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "You've no permission to update exam",
                        null);
            }

            if (dto.getId() == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam id is required",
                        null);
            }

            Optional<ExamMaster> examOptional =
                    examMasterRepository.findById(dto.getId());

            if (examOptional.isEmpty()) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam not found",
                        null);
            }

            if (dto.getExamName() == null ||
                    dto.getExamName().trim().isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam name is required",
                        null);
            }

            if (dto.getClassId() == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Class is required",
                        null);
            }

            if (dto.getExamType() == null) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam type is required",
                        null);
            }

            if (dto.getExamSubjects() == null ||
                    dto.getExamSubjects().isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "At least one subject is required",
                        null);
            }

            Optional<StudentClass> studentClassOptional =
                    studentClassRepository.findById(dto.getClassId());

            if (studentClassOptional.isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Class not found",
                        null);
            }

            StudentClass studentClass =
                    studentClassOptional.get();

            ExamMaster examMaster =
                    examOptional.get();

            // Update master fields
            examMaster.setExamName(
                    dto.getExamName().trim());

            examMaster.setExamType(
                    dto.getExamType());

            examMaster.setStudentClass(
                    studentClass);

            examMasterRepository.save(examMaster);

            // Delete old subjects
            examSubjectRepository.deleteAllByExamMaster(
                    examMaster);

            List<ExamSubject> newSubjects =
                    new ArrayList<>();

            Set<Long> uniqueSubjectIds =
                    new HashSet<>();

            for (ExamSubjectDto subjectDto :
                    dto.getExamSubjects()) {

                if (subjectDto.getClassSubjectId() == null) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Class subject id is required",
                            null);
                }

                if (subjectDto.getFullMark() == null ||
                        subjectDto.getFullMark() <= 0) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Full mark must be greater than zero",
                            null);
                }

                // duplicate subject validation
                if (!uniqueSubjectIds.add(
                        subjectDto.getClassSubjectId())) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Duplicate subject selected",
                            null);
                }

                Optional<ClassSubject> classSubjectOptional =
                        classSubjectRepository.findById(
                                subjectDto.getClassSubjectId());

                if (classSubjectOptional.isEmpty()) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "ClassSubject not found. Id : "
                                    + subjectDto.getClassSubjectId(),
                            null);
                }

                ClassSubject classSubject =
                        classSubjectOptional.get();

                // Validate subject belongs to selected class
                if (!classSubject
                        .getStudentClass()
                        .getId()
                        .equals(dto.getClassId())) {

                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Selected subject does not belong to selected class",
                            null);
                }

                ExamSubject examSubject =
                        new ExamSubject();

                examSubject.setExamMaster(
                        examMaster);

                examSubject.setClassSubject(
                        classSubject);

                examSubject.setFullMark(
                        subjectDto.getFullMark());

                newSubjects.add(examSubject);
            }

            examSubjectRepository.saveAll(
                    newSubjects);

            return new Response<>(
                    HttpStatus.OK.value(),
                    "Exam updated successfully",
                    examMaster);

        } catch (Exception e) {

            logger.error(
                    "Error while updating exam",
                    e);

            return new Response<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Something went wrong",
                    null);
        }
    }

    @Override
    @Transactional
    public Response<?> deleteExam(Long id) {

        try {

            Optional<User> userDetails =
                    customizedUserDetailsService.getUserDetails();

            if (userDetails.isEmpty()) {
                logger.info("Authorization error");

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Please login again",
                        null);
            }

            if (!Role.ADMIN.equals(userDetails.get().getRole())) {

                logger.info(
                        "Non-admin user trying to delete exam");

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "You've no permission to delete exam",
                        null);
            }

            if (id == null) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam id is required",
                        null);
            }

            Optional<ExamMaster> examOptional =
                    examMasterRepository.findById(id);

            if (examOptional.isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Exam not found",
                        null);
            }

            ExamMaster examMaster =
                    examOptional.get();

            // delete child records first
            examSubjectRepository
                    .deleteAllByExamMaster(examMaster);

            // delete master record
            examMasterRepository
                    .delete(examMaster);

            logger.info(
                    "Exam deleted successfully. Id : {}",
                    id);

            return new Response<>(
                    HttpStatus.OK.value(),
                    "Exam deleted successfully",
                    null);

        } catch (Exception e) {

            logger.error(
                    "Error while deleting exam",
                    e);

            return new Response<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Something went wrong",
                    null);
        }
    }

    @Override
    public Response<?> getAllExam() {

        try {

            Optional<User> userDetails =
                    customizedUserDetailsService.getUserDetails();

            if (userDetails.isEmpty()) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Please login again",
                        null);
            }

            if (!Role.ADMIN.equals(userDetails.get().getRole())) {

                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "You've no permission",
                        null);
            }

            List<ExamMaster> exams =
                    examMasterRepository.findAll();

            List<ExamResponseDto> responseList =
                    new ArrayList<>();

            for (ExamMaster exam : exams) {

                ExamResponseDto dto =
                        new ExamResponseDto();

                dto.setId(exam.getId());
                dto.setExamName(exam.getExamName());

                if (exam.getExamType() != null) {
                    dto.setExamType(
                            exam.getExamType().name());
                }

                if (exam.getStudentClass() != null) {

                    dto.setClassId(
                            exam.getStudentClass().getId());

                    dto.setClassName(
                            exam.getStudentClass()
                                    .getStudentClass());
                }

                List<ExamSubjectResponseDto> subjectDtos =
                        new ArrayList<>();

                List<ExamSubject> examSubjects =
                        examSubjectRepository
                                .findByExamMaster(exam);

                for (ExamSubject examSubject :
                        examSubjects) {

                    ExamSubjectResponseDto subjectDto =
                            new ExamSubjectResponseDto();

                    subjectDto.setId(examSubject.getId());

                    if (examSubject.getClassSubject() != null) {

                        subjectDto.setClassSubjectId(
                                examSubject
                                        .getClassSubject()
                                        .getId());

                        if (examSubject
                                .getClassSubject()
                                .getSubject() != null) {

                            subjectDto.setSubjectName(
                                    examSubject
                                            .getClassSubject()
                                            .getSubject()
                                            .getName());
                        }
                    }

                    subjectDto.setFullMark(
                            examSubject.getFullMark());

                    subjectDtos.add(subjectDto);
                }

                dto.setSubjects(subjectDtos);

                responseList.add(dto);
            }

            return new Response<>(
                    HttpStatus.OK.value(),
                    "Success",
                    responseList);

        } catch (Exception e) {

            logger.error(
                    "Error while fetching exams",
                    e);

            return new Response<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Something went wrong",
                    null);
        }
    }
}
