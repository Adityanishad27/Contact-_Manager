package com.mypackage.Controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mypackage.DAO.userRepository;
import com.mypackage.Entities.User;
import com.mypackage.Helper.Message;
import com.mypackage.Services.EmailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class ForgetPassword_Contoller {

	
	
	@Autowired
private	EmailService emailService;
	
	@Autowired
private userRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// to generate random number 
 	Random random= new Random(100000);
 	
 	
	
	// open handler forot page 
	
	@RequestMapping("/forgot")
	 public String openForgetpage(Model model) {
		 
		model.addAttribute("title", "Forgot-Page");
		 
		 return "forgetEmailForm";
	 }
	
	
	
  // send otp handler 
	
	@PostMapping("/sendotp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		
		System.out.println("Email :" +email);
		
		// GGenerating 6 digit OTP

		
	int otp=random.nextInt(999999);  // for 4digit 9999
		
	System.out.println("OTP"+ otp);
	
	// sent otp to email 
	String subject="OTP from Contact Manager";
	String messagess=""
			+"<div style='border:1px solid #e2e2e2; padding'>"
			+"<h1>"
			+"Contact Manager OTP is :"
	       +"<b>"+otp
	       +"</n>"
	       +"</h1>"
	       +"</div>";
	String to=email;
	
boolean flag=	emailService.SendEmail(subject, messagess, to);

if(flag) {
	
	session.setAttribute("myotp", otp);
	session.setAttribute("email", email);
	
	return "VerifyOTP";
	
}else {
	
	//session.setAttribute("message","Check your Email");
	session.setAttribute("message", new Message(" Please Check your Email !!", "alert-danger"));
	
	return "forgetEmailForm";
	
}
		}
	
	
	
	// verifyOTP handler 
	
	
	
	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("otp") int otp, HttpSession session) {
		
		
   int myotp=  (int) session.getAttribute("myotp");  // get myotp by send Otp handler ^up
   String email= (String) session.getAttribute("email");
   
   if(myotp==otp) {
	   
	   User user=this.userRepository.getUserByUserName(email);
	   
	   if(user==null) {
		  // send error 
			session.setAttribute("message", new Message(" User does not exist with this email !!", "alert-danger"));
			
			return "forgetEmailForm";
	   }
	   
	   else {
		   // change password  from 
		   
		   return "changePasswordform";
		   
	   }
	   
	 
	  
   }
   
   else {
		
	
   
   session.setAttribute("message", new Message("you have entered incorrect OTP !!", "alert-danger"));
   return "VerifyOTP";	
   }
	}	
	
	
	// changeForgetPassword Handler
	
     @PostMapping("/changeForgetPassword")
	public String  changeForgetPassword(@RequestParam("newpassword") String newpassword,HttpSession session) {
    	 
  String email=  (String) session.getAttribute("email");
  
 User user= this.userRepository.getUserByUserName(email);
 
 user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
 
 this.userRepository.save(user);
    	 
		return "redirect:/login?change=change password successfully......";
		
	}
	

}
