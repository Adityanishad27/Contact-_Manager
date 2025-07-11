package com.mypackage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mypackage.DAO.userRepository;
import com.mypackage.Entities.User;


public class UserdetailServiceImpl  implements UserDetailsService {
	
	
	@Autowired
    private userRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	// fetching user by database
		
	User user=	userRepository.getUserByUserName(username);
		if(user==null) {
			throw new  UsernameNotFoundException("Could not found user!!");
		}
	
		CustomUserDetails customUserDetails= new CustomUserDetails( user);
		
		return  customUserDetails;
		
	}

	

}
