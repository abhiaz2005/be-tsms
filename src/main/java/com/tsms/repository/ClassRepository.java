package com.tsms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsms.entity.StudentClass;

@Repository
public interface ClassRepository extends JpaRepository<StudentClass, Long> {

}
