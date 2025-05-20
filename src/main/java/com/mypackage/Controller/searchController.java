package com.mypackage.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mypackage.DAO.contactRepository;
import com.mypackage.DAO.userRepository;
import com.mypackage.Entities.Contact;
import com.mypackage.Entities.User;

@RestController
public class searchController {
	@Autowired
	private userRepository userRepository;
	
	@Autowired
 private contactRepository contactRepository;
	
	/*
	 * @GetMapping("/serach/{query}") public ResponseEntity<?>
	 * search(@PathVariable("query") String query,Principal principal){
	 * System.out.println(query); User user
	 * =this.userRepository.getUserByUserName(principal.getName());
	 * 
	 * List<Contact> contacts=
	 * this.contactRepository.findByfirstNameContaining(query, user); return
	 * ResponseEntity.ok(contacts);
	 * 
	 * }
	 */
}
