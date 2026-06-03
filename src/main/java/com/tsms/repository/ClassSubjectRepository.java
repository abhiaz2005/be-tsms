package com.tsms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsms.entity.ClassSubject;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
	List<ClassSubject> findByStudentClass_Id(Long classId);

	Optional<ClassSubject> findBySubject_IdAndStudentClass_Id(Long subjectId, Long classId);
}