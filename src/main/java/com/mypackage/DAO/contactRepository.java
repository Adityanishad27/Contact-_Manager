package com.mypackage.DAO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mypackage.Entities.Contact;

public interface contactRepository extends JpaRepository<Contact, Integer> {
	
	
	
	@Query("from Contact as u where u.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param("userId")  int userId,Pageable pageable);

}
