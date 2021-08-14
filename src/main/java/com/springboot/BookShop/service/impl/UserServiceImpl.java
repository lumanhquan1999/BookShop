package com.springboot.BookShop.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.BookShop.dao.RoleRepository;
import com.springboot.BookShop.dao.UserRepository;
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
	
	public Page<User> listAll(int pageNumber, String keyword) {
		
		Pageable pageable = PageRequest.of(pageNumber - 1, 5);
		
		if (keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}
		return userRepository.findAll(keyword, pageable);
	}
	
	public User saveRegister(User user) {
		
		Set<Role> listRoles = new HashSet<>();
		listRoles.add(roleRepository.findByName("USER"));
		encodePassword(user);
		user.setCreateDate(new Date());
		user.setEnable(false);
		user.setRoles(listRoles);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
			
		 return userRepository.save(user);
	}
	
	public void save(User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			
		} else {
			user.setCreateDate(new Date());
			encodePassword(user);
		}
			
		userRepository.save(user);
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
	
	public boolean isEmailUnique(Integer id, String email) {
		
		User userByEmail = userRepository.getUserByEmail(email);
		
		if (userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}

	public List<User> findAll() {
		
		return (List<User>) userRepository.findAll();
	}
	
	public void delete(Integer id) {
		
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new RuntimeException("Could not find any user with ID " + id);
		}
		
		userRepository.deleteById(id);
	}

	@Override
	public void updateUserEnabledStatus(Integer id, boolean enable) {
		
		userRepository.updateEnabledStatus(id, enable);
	}
}
