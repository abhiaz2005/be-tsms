package com.tsms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsms.entity.User;
import com.tsms.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByPhoneNo(String phoneNo);

	List<User> findByRole(Role role);
}
