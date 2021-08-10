package com.springboot.BookShop.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.BookShop.dao.RoleRepository;
import com.springboot.BookShop.dao.UserRepository;
import com.springboot.BookShop.entity.Book;
import com.springboot.BookShop.entity.Role;
import com.springboot.BookShop.entity.User;
import com.springboot.BookShop.service.MyUserDetails;
import com.springboot.BookShop.service.UserService;

import net.bytebuddy.utility.RandomString;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public void encodePassword(User user) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public List<User> listAll() {
		
		return userRepository.findAll();
	}
	
	public void registerUser(User user) {

		encodePassword(user);
		user.setCreateDate(new Date());
		user.setEnable(false);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
	}
	
	public User save(User user) {
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

	public User updateUser(User user) {
		
		User existUser = userRepository.findById(user.getId()).get();
		
		if (user.getPassword().isEmpty()) {
			user.setPassword(existUser.getPassword());
		} else {
			encodePassword(user);
		}
		
		user.setEnable(true);
		
		return userRepository.save(user);
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
		User user = null;
		Object principal = authentication.getPrincipal();
		
		if (principal instanceof MyUserDetails) {
			user = userRepository.getUserByUsername(((MyUserDetails) principal).getUsername());
		} 
		
		return user;
	}

	@Override
	public User findByUsername(String username) {
		
		User user = userRepository.findByUsername(username);
		
		return user;
	}
	
	public User findById(Integer id) {
		
		Optional<User> result = userRepository.findById(id);
		
		User user = null;
		
		if (result.isPresent()) {
			
			user = result.get();
		}
		else {
			throw new RuntimeException("Did not find book id - " + id);
		}
		return user;
	}

	@Override
	public List<Role> listRoles() {
		
		return (List<Role>) roleRepository.findAll();
	}
	
	public boolean isEmailUnique(String email) {
		
		User userByEmail = userRepository.getUserByEmail(email);
		return userByEmail == null;
	}
}
