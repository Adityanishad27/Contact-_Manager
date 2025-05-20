package com.mypackage.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mypackage.DAO.userRepository;
import com.mypackage.Entities.User;
import com.mypackage.Helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class mycontroller {

	@Autowired
	private userRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// home page
	@RequestMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "HOME - Smart Contact Manager");
		return "home";
	}

	// about page controller
	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "ABOUT - Smart Contact Manager");
		return "about";
	}

	// sign up
	@RequestMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "REGISTER - Smart Contact Manager");

		model.addAttribute("user", new User());
		return "signup";
	}
	
	
	

	// register form handler
	// @PostMapping("/do_register")

	/* @RequestMapping(value = "/do_register", method = RequestMethod.POST) */
	
	
   @PostMapping("/do_register")
	public String regform(@Valid @ModelAttribute("user") User user,

			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,

			HttpSession session) {

		try {
			if (!agreement) {
				System.out.println("You have not Agreed the term and conditions");

				throw new Exception("You have not Agreed the term and conditions !!");

			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("null.png");

			user.setPassword(passwordEncoder.encode(user.getPassword())); // setting password to configuration class
																			// BcryptpasswordEncoder

			System.out.println("Agreement" + agreement);
			System.out.println("User" + user);

			this.userRepository.save(user);

			model.addAttribute("user", new User());

			session.setAttribute("message", new Message("Successfully registered !!", "alert-success"));

			return "signup";

		} catch (Exception e) {
			//e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger"));
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "signup";
		}

	}
	
	
	  @RequestMapping("/login") 
	  public String loginpage(Model model) {
	  
	  model.addAttribute("title", "LOGIN - Smart Contact Manager");
	  
	  return "Login";
	  }
	  
	 @RequestMapping("/login-failed")
	  public String loginFailed() {
		  
		 return "redirect:/login?error=Invalid Email or Password !!";
		 
	  }
	  
	 
	//RazorPay payment 
	 @PostMapping("/create-order")
	 
	 public String create_order() {
		 System.out.println("Payment gateway still working ha ha ha ");
		 
		 return "done";
	 }

}
