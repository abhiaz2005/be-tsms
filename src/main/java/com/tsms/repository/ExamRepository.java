package com.tsms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsms.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
	
	List<Exam> findByClassSubject_Id(Long classSubjectId);

	Optional<Exam> findByExamNameAndClassSubject_Id(String examName, Long classSubjectId);
}
