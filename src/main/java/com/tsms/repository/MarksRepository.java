package com.tsms.repository;

import com.tsms.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent_Id(Long studentId);

    List<Marks> findByExam_Id(Long examId);

    Optional<Marks> findByStudent_IdAndExam_Id(
            Long studentId,
            Long examSubjectId
    );

    List<Marks> findByStudentId(Long studentId);

    @Query("""
            SELECT m
            FROM Marks m
            WHERE m.exam.examMaster.createdAt BETWEEN :startDate AND :endDate
            """)
    List<Marks> findByYear(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("""
            SELECT m
            FROM Marks m
            WHERE m.createdAt BETWEEN :startDate AND :endDate
            AND LOWER(m.student.section.studentClass) = LOWER(:className)
            """)
    List<Marks> findByYearAndClass(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("className") String className
    );
}