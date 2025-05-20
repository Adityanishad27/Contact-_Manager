package com.mypackage.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mypackage.Entities.User;

public interface userRepository extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.email = :email ")
public User getUserByUserName(@Param("email") String email);
}
