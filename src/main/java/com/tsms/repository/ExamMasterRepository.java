package com.tsms.repository;

import com.tsms.entity.ExamMaster;
import com.tsms.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamMasterRepository extends JpaRepository<ExamMaster, Long> {
    boolean existsByExamNameAndStudentClass(String examName, StudentClass studentClass);
}
