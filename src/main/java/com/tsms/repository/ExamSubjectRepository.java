package com.tsms.repository;

import com.tsms.entity.ExamMaster;
import com.tsms.entity.ExamSubject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Long> {
    @Transactional
    @Modifying
    void deleteAllByExamMaster(ExamMaster examMaster);

    @Transactional
    @Modifying
    void deleteByExamMaster(ExamMaster examMaster);

    List<ExamSubject> findByExamMaster(ExamMaster exam);
}
