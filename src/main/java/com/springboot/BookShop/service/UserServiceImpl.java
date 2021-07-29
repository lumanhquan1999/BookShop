package com.springboot.BookShop.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.BookShop.dao.UserRepository;
import com.springboot.BookShop.entity.User;

import net.bytebuddy.utility.RandomString;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	public void encodePassword(User user) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public List<User> listAll() {
		
		return userRepository.findAll();
	}
	
	public User registerUser(User user) {

		encodePassword(user);
		user.setCreateDate(new Date());
		user.setEnable(false);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		
		return userRepository.save(user);
	}
	
	public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
		
		String subject = "Please verify your registration";
		String senderName = "Book Shop";
		String mailContent = "<p>Dear " + user.getName() + ",</p>";
		mailContent += "<p>Please click the link below to verify your registration:</p>";
		
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
		mailContent += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";
		
		mailContent += "<p>Thank you<br>Book Shop</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("lumanhquan1999@gmail.com", senderName);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.setText(mailContent, true);
		
		mailSender.send(message);
	}

	public void updateUser(User user) {
		
		User existUser = userRepository.findById(user.getId()).get();
		
		if (user.getPassword().isEmpty()) {
			user.setPassword(existUser.getPassword());
		} else {
			encodePassword(user);
		}
		
		user.setEnable(true);
		
		userRepository.save(user);
	}
	
	public boolean verify(String verificationCode) {
		
		User user = userRepository.findByVerificationCode(verificationCode);
		
		if (user == null || user.isEnable()) {
			return false;
		} else {
			userRepository.enable(user.getId());
			return true;
		}
	}	
	
	public User getCurrentlyLoggedInUser(Authentication authentication) {
		
		if (authentication == null) 
			return null;
		System.out.println("1");
		User user = null;
		Object principal = authentication.getPrincipal();
		
		if (principal instanceof MyUserDetails) {
			user = userRepository.getUserByUsername(((MyUserDetails) principal).getUsername());
		} 
		
		return user;
	}
}
