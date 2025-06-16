package com.mypackage.Services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	

	public boolean  SendEmail(String subject, String message, String to) {
	
		boolean f= false;
		// set from 
		
		String from="avikkt09@gmal.com";
		// variable g mail host
				String host = "smtp.gmail.com";

				// get the system properties or information
				Properties properties = System.getProperties();
				System.out.println("PROPERTIES" + properties);

				// Setting important data in properties object

				// set host

				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");

				// Step:1 to get the session object

				Session session = Session.getInstance(properties, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {

						return new PasswordAuthentication("avikkt09@gmail.com", "Enter passsword here");
					}
				});

				session.setDebug(true);

				// Step 2 : compose the message (text, Multi media files)

				MimeMessage mimeMessage = new MimeMessage(session);

				try {
					// set from email
					mimeMessage.setFrom(from);

					// set to (reciever mail)
					mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

					// adding subject to message
					mimeMessage.setSubject(subject);

					// Adding text to message

					/* mimeMessage.setText(message); */
					mimeMessage.setContent(message, "text/html");

					// ++ STEP 4 : send message using Transport class

					Transport.send(mimeMessage);
                     f=true;
					System.out.println("Email sent Successfully");

				} catch (MessagingException e) {

					e.printStackTrace();
				}
            return f;

		

}
}
