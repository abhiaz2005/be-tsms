package com.tsms.serviceImpl;

import com.tsms.dto.MarkDto;
import com.tsms.dto.Response;
import com.tsms.entity.Exam;
import com.tsms.entity.Marks;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.ExamRepository;
import com.tsms.repository.MarksRepository;
import com.tsms.repository.UserRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private boolean isAdmin() {
        Optional<User> user = customizedUserDetailsService.getUserDetails();
        return user.isPresent() && user.get().getRole().equals(Role.ADMIN);
    }

    @Override
    public Response<?> addMarks(List<MarkDto> dtos) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<Marks> toSave = new ArrayList<>();

            for (MarkDto dto : dtos) {
                Optional<User> student = userRepository.findById(dto.getStudentId());
                if (student.isEmpty())
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), "Student not found: " + dto.getStudentId(), null);

                Optional<Exam> exam = examRepository.findById(dto.getExamId());
                if (exam.isEmpty())
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), "Exam not found: " + dto.getExamId(), null);

                // duplicate check
                Optional<Marks> duplicate = marksRepository.findByStudent_IdAndExam_Id(dto.getStudentId(), dto.getExamId());
                if (duplicate.isPresent())
                    return new Response<>(HttpStatus.BAD_REQUEST.value(),
                            "Marks already added for student " + dto.getStudentId() + " in exam " + dto.getExamId(), null);

                Marks mark = new Marks();
                mark.setStudent(student.get());
                mark.setExam(exam.get());
                mark.setSecuredMark(dto.getSecuredMark());
                toSave.add(mark);
            }

            marksRepository.saveAll(toSave);
            logger.info("{} marks saved", toSave.size());
            return new Response<>(HttpStatus.OK.value(), "Marks added successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getAllMarks() {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<Marks> marks = marksRepository.findAll();
            return new Response<>(HttpStatus.OK.value(), "Success", marks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getMarksByStudent(Long studentId) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            List<Marks> marks = marksRepository.findByStudent_Id(studentId);
            return new Response<>(HttpStatus.OK.value(), "Success", marks);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> editMark(MarkDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            if (dto.getId() == null)
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Provide mark id", null);

            Optional<Marks> optional = marksRepository.findById(dto.getId());
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Mark not found", null);

            Marks mark = optional.get();
            if (dto.getSecuredMark() != null) mark.setSecuredMark(dto.getSecuredMark());
            marksRepository.save(mark);

            return new Response<>(HttpStatus.OK.value(), "Mark updated successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> deleteMark(Long id) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            Optional<Marks> optional = marksRepository.findById(id);
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Mark not found", null);

            marksRepository.delete(optional.get());
            return new Response<>(HttpStatus.OK.value(), "Mark deleted successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }
}