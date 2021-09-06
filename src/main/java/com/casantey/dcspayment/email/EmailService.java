package com.casantey.dcspayment.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.casantey.dcspayment.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;




@Service @RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender javaMailSender;

	
	public void sendEmail(String email, String recipient, String subject, String message) throws MailException {  
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(Constants.SENDER_EMAIL_ADDRESS);
		mail.setTo(email);
		mail.setSubject(subject);
		mail.setText(message);
		
		javaMailSender.send(mail);
	}
	
	public void sendHTMLEmail(String email, String recipient, String subject, String message) throws MailException, MessagingException {  
	 	MimeMessage mail = javaMailSender.createMimeMessage();   
	    MimeMessageHelper helper = new MimeMessageHelper(mail, true);
	    
	    helper.setFrom(Constants.SENDER_EMAIL_ADDRESS);
	    helper.setTo(email);
	    helper.setSubject(subject);
	    helper.setText(message,true);

	javaMailSender.send(mail);
}

}

