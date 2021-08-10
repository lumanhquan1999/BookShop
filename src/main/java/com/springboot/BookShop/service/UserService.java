package com.springboot.BookShop.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Role;
import com.springboot.BookShop.entity.User;

@Repository
public interface UserService {

	List<User> listAll();
	
	void registerUser(User user);
	
	User save(User user);
	
	User updateUser(User user);
	
	void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException;
	
	boolean verify(String verificationCode);
	
	User findByUsername(String username);
	
	User findById(Integer id);
	
	List<Role> listRoles();
	
	boolean isEmailUnique(String email);
}
