package com.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	
	
	private JavaMailSender mailSender;
	
	public EmailUtils(JavaMailSender mailSender) {
		
		this.mailSender = mailSender;
	}


	public boolean sendMail(String to,String subject,String body) {
		boolean isSent=false;
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body,true);
			
			mailSender.send(mimeMessage);
			isSent=true;
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		return isSent;
	}
	
	public String createMailBody(String name,String password,String url,String msg)
	{
		String mailBody = "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "    <title>Registration Confirmation</title>\r\n"
				+ "    <style>\r\n"
				+ "        /* Basic styling for the email */\r\n"
				+ "        body {\r\n"
				+ "            font-family: Arial, sans-serif;\r\n"
				+ "            font-size: 16px;\r\n"
				+ "            line-height: 1.5;\r\n"
				+ "            padding: 20px;\r\n"
				+ "        }\r\n"
				+ "		h1 {\r\n"
				+ "            color: #333;\r\n"
				+ "            font-size: 24px;\r\n"
				+ "            margin-bottom: 20px;\r\n"
				+ "        }\r\n"
				+ "		p {\r\n"
				+ "            margin-bottom: 10px;\r\n"
				+ "        }\r\n"
				+ "        a {\r\n"
				+ "            color: #007bff;\r\n"
				+ "            text-decoration: none;\r\n"
				+ "        }\r\n"
				+ "    </style>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "    <h1>Welcome to Our Website!</h1>\r\n"
				+ "	 <p>Thank you for registering with us.</p>\r\n"
				+ "	<p><strong>Your User Name is:</strong> {{FULL-NAME}}</p>\r\n"
				+ "	<p><strong>Your </strong> {{temporary_password}}</p>\r\n"
				+ "	<p>Please click the following link to {msg}</p>\r\n"
				+ "    <a href=\"{link}\">Click Here To Activate Your Account</a>\r\n"
				+ "	<p>Best regards,</p>\r\n"
				+ "    <p>The Website Team</p>\r\n"
				+ "</body>\r\n"
				+ "</html>";
			mailBody = mailBody.replace("{{FULL-NAME}}", name);
			mailBody = mailBody.replace("{{temporary_password}}", password);
			mailBody=mailBody.replace("{msg}",msg);
			mailBody = mailBody.replace("{link}", url);
		return mailBody;
	}


}
