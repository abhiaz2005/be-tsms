package com.tsms.repository;

import com.tsms.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent_Id(Long studentId);
    List<Marks> findByExam_Id(Long examId);
    Optional<Marks> findByStudent_IdAndExam_Id(Long studentId, Long examId);
}