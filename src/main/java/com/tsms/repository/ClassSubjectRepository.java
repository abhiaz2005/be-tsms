package com.tsms.repository;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tsms.entity.ClassSubject;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
	List<ClassSubject> findByStudentClass_Id(Long classId);

	Optional<ClassSubject> findBySubject_IdAndStudentClass_Id(Long subjectId, Long classId);

    List<ClassSubject> findBySubject_Id(Long subjectId);

	@Modifying
	@Transactional
	@Query("DELETE FROM ClassSubject cs WHERE cs.subject.id = :subjectId")
	int deleteBySubjectId(@Param("subjectId") Long subjectId);
}