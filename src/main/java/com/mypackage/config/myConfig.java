package com.mypackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class myConfig extends WebSecurityConfiguration {

	@Bean
	public UserDetailsService getuserDetailsService() {

		return new UserdetailServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider Provider = new DaoAuthenticationProvider();

		Provider.setUserDetailsService(getuserDetailsService());
		Provider.setPasswordEncoder(passwordEncoder());
		return Provider;

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();

	}

	
	
	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	  
	  
		  http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/user/**").hasRole("USER")
              .requestMatchers("/**").permitAll()
          ).formLogin().loginPage("/login").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").failureUrl("/login-failed").and().csrf().disable();
          
          
      

      return http.build();
	  
	  }
	  
	  
	  
	 

}
