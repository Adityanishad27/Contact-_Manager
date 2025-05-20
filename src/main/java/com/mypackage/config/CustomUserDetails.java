package com.mypackage.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mypackage.Entities.User;



public class CustomUserDetails implements UserDetails {
	
	private User user;
	
	
	

	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
SimpleGrantedAuthority	SimpleGrantedAuthority=	new SimpleGrantedAuthority(user.getRole());
		
		return java.util.List.of(SimpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
	//	return UserDetails.super.isAccountNonExpired();
		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		//return UserDetails.super.isCredentialsNonExpired();
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		//return UserDetails.super.isEnabled();
		return true;
	}

}
