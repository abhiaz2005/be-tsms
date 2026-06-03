package com.tsms.serviceImpl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tsms.entity.Fees;
import com.tsms.entity.User;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {

	@Query(value = "select * from fees where student_id=?1",nativeQuery = true)
	List<Fees> findAllByUserId(Long id);


	boolean existsByStudentAndMonthAndYear(User student,Integer month,Integer year);

	@Query(value = "select * from fees where year=?1",nativeQuery = true)
	List<Fees> findAllByYear(Integer year);


	
}
