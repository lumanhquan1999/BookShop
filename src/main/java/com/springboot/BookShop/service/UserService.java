package com.springboot.BookShop.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.User;

@Repository
public interface UserService {

	List<User> listAll();
	
	User registerUser(User user);
	
	void updateUser(User user);
	
	void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException;
	
	boolean verify(String verificationCode);
}
