package com.mypackage.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mypackage.DAO.contactRepository;
import com.mypackage.DAO.userRepository;
import com.mypackage.Entities.Contact;
import com.mypackage.Entities.User;
import com.mypackage.Helper.Message;

import jakarta.servlet.http.HttpSession;

@EnableAutoConfiguration
@Controller
@RequestMapping("/user")
public class User_controller {

    /*~~(Unable to determine parameter type)~~>*/
	/* private final Controller.mycontroller mycontroller; */
	
	private final SecurityFilterChain filterChain;
    private final AuthenticationManager authenticationManager;
	
@Autowired	
private BCryptPasswordEncoder bCryptPasswordEncoder;
	   

	@Autowired
	private userRepository userRepository;

	@Autowired
	private contactRepository contactRepository;
	
	
	

	// adding common data to response //loggedIn user

	@ModelAttribute
	public void AddCommanDataUser(Model model, Principal principal) {

		String userName = principal.getName();

		System.out.println("USERNAME" + userName);

		// get user name by email

		User user = this.userRepository.getUserByUserName(userName);

		System.out.println(userName + user);
		model.addAttribute("user", user);

	}

	User_controller(AuthenticationManager authenticationManager, SecurityFilterChain filterChain) {
		this.authenticationManager = authenticationManager;
		this.filterChain = filterChain;
	}
	
	

	// user Dash board home page
	@RequestMapping("/index")
	public String user_dashboard(Model model, Principal principal) {

		/*
		 * String userName= principal.getName();
		 * 
		 * System.out.println("USERNAME" +userName);
		 * 
		 * // get user name by email
		 * 
		 * User user= this.userRepository.getUserByUserName(userName);
		 * 
		 * System.out.println(userName + user); model.addAttribute("user", user);
		 */

		return "normalUser/user_dashboard";
	}

	
	
	// Add contact handler open form 

	@GetMapping("/AddContact")
	public String AddContact(Model model) {

		model.addAttribute("title", "Add-Contact");
		model.addAttribute("contact", new Contact());
		return "normalUser/AddContact_form";
	}

	
	
	
	
	
	
	
	
	
	
	// process contact to form save

	@PostMapping("/process_contact")
	public String SaveContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			String name = principal.getName();

			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file

			if (file.isEmpty()) {
				// file the file is empty
				System.out.println(" Image Uploading Failed... No File Selected !!");

				contact.setImage("noProfile.png");

				

			} else {

				// file upload
				contact.setImage(file.getOriginalFilename());
			File saveFile= new ClassPathResource("/static/img").getFile();
             Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
				System.out.println("Image uploaded Successfully");

			}

			contact.setUser(user);

			user.getContact().add(contact);

			this.userRepository.save(user);

			System.out.println("Successfully Save to Data base");

			// Success message
			session.setAttribute("message", new Message(" Contact Saved Successfully... ", "alert-success"));

			// System.out.println("DATA"+ contact);
		} catch (Exception e) {

			// Error Messages

			session.setAttribute("message", new Message(" Contact Saved Failed... ", "alert-danger"));
			System.out.println("Error" + e.getMessage());
			e.printStackTrace();
		}

		return "normalUser/AddContact_form";
	}

	
	
	
	
	
	
	
	
	
	
	
	// SHOW CONTACT HANDLER

	@GetMapping("/showContacts/{page}")
	public String show_contact(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "SHOW-CONTACTS");

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contact = (Page<Contact>) this.contactRepository.findContactsByUser(user.getId(), pageable);
		model.addAttribute("contact", contact);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contact.getTotalPages());

		return "normalUser/show_contacts";

	}

	
	
	
	
	
	
	
	
	
	
	
	
	  // show contact on click email
	  
	  
	  @RequestMapping("contact/{contactId}")
	  
	  public String showDetailsOnClick( Model model,Principal principal ,@PathVariable("contactId") Integer contactId) {
	  
	
	  System.out.println("CONTACT ID "+contactId);
	  Optional<Contact> optionalcontact= this.contactRepository.findById(contactId);
	  Contact contact =optionalcontact.get();
	  
	  String userName=principal.getName();
	User user=  this.userRepository.getUserByUserName(userName);
	
	  if(user.getId()==contact.getUser().getId()) {
		  model.addAttribute("contact", contact);
		  model.addAttribute("title",contact.getFirstName());
	  }
	 
	  
	  return "normalUser/show_contactdetails"; 
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  //Delete Contact hmadler
	  
	  @GetMapping("/delete/{contactId}")
	  public String deleteContact(@PathVariable("contactId") Integer contactId,HttpSession session,Principal principal) {
		 
            Optional<Contact> optionalContact	=  this.contactRepository.findById(contactId);

              Contact contact=optionalContact.get();
                System.out.println("contactID"+contactId);
                    //     contact.setUser(null); //unlinked to the user to delete contact
                   //  this.contactRepository.delete(contact);
          User user=      this.userRepository.getUserByUserName(principal.getName());
                user.getContact().remove(contact);
                this.userRepository.save(user);
                
                System.out.println("Deleted");
                
                     session.setAttribute("message", new Message("Contact Deleted successfully", "success"));
                     
		  
		               return "redirect:/user/showContacts/0";
		               
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  //update form  open handler
	  //open form 
	  @PostMapping("/updateContact/{contactId}")
	  public String updateForm(@PathVariable("contactId") Integer contactId,Model model) {
		  
		 model.addAttribute("title","Update-Contact");
		 
	Contact contact = this.contactRepository.findById(contactId).get();
		 
	model.addAttribute("contact", contact);
		 
		  return "normalUser/updateForm";
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  // update contact handler process form
		
		
		  @PostMapping("/updateform-process")
		  
			public String updateformHandler(@ModelAttribute Contact contact	,HttpSession session, Model model ,@RequestParam("profileImage")MultipartFile file,Principal principal
													 ) {
			  try {
				  
				  //get old contact details
		  Contact  oldcontact=this.contactRepository.findById(contact.getContactId()).get();
				  model.addAttribute("title", "Update-Contact");
				  
				  if(!file.isEmpty()) {
					  
					  // Delete old
					  File deletefile= new ClassPathResource("/static/img").getFile(); 
						  File oldfile= new File(deletefile, oldcontact.getImage());
						  oldfile.delete();
						 
						  
					  
					  // update new 
						  
							contact.setImage(file.getOriginalFilename());
							File saveFile= new ClassPathResource("/static/img").getFile();
				             Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
							Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
							
								System.out.println("Image uploaded Successfully");
						  
						
				  }
				  else {
					  
					oldcontact.setImage(oldcontact.getImage());
					
				}
				  
				  
				  User user=this.userRepository.getUserByUserName(principal.getName());
				  contact.setUser(user);
				  this.contactRepository.save(contact);
				  
				   session.setAttribute("message", new Message("Your Contact is Updated","success"));
				
			} catch (Exception e) {
				
				System.out.println(e);
				
			}
			 
		  
			 System.out.println("Contact ID =" + contact.getContactId());
			 System.out.println("Contact Name =" + contact.getFirstName());
			 
			  
			  return "redirect:/user/contact/" +contact.getContactId() ;
				
				 
		  }
		  
		  
		  //profile handler
		  
		  @GetMapping("/profile")
		  public String profile(Model model) {
			  
			  
			  return "normalUser/profile";
		  }
		  
		  
		  //user  setting handler 
		  
		  @GetMapping("/setting")
		  public String openSetting() {
			  
			  
			  return "normalUser/setting";
		  }
		  
		  
		  
        // change password handler 
		  
		  @PostMapping("/change_password")
		  public String changePassword(@RequestParam("oldpasswordfield") String oldpassword, @RequestParam("newpasswordfield") String newpassword,Principal principal,HttpSession  session) {
			 
			 System.out.println("OLD PASSWORD IS :"+ oldpassword);
			 System.out.println("NEW PASSWORD IS :"+newpassword);
			 
			 String userName= principal.getName();
			 
			 User currentUser= this.userRepository.getUserByUserName(userName);
			 
			 System.out.println(currentUser.getPassword());
			 
			 if(bCryptPasswordEncoder.matches(oldpassword, currentUser.getPassword())) {
				 
				 //change password 
				 
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
				session.setAttribute("message", new Message("Password Changed Successfully ", "success"));
				
				//save user password 
				this.userRepository.save(currentUser);
				 
			 }
			 else {
				 
				 // error
				 
				 session.setAttribute("message", new Message("Old password didn't match try again.........  ", "danger"));
				 
				 return "redirect:/user/setting";
			 }
			 
			 
			  return "redirect:/user/index";
		  }
		  
		
	 
}
