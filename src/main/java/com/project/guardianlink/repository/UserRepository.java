package com.project.guardianlink.repository;

import com.project.guardianlink.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
	User findByUsername (String username);
	List<User> findAllByRole(String role);
	User findFirstByRole(String role);
}