package com.springboot.BookShop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.BookShop.utils.Utils;

@Controller
public class AppController {
	
	@GetMapping("")
	public String viewHomePage() {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String showLoginPage(HttpServletRequest request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			Utils.removeCartInSession(request);
			return "login";
		}
		return "redirect:/books/list";
	}
	
//	@PostMapping("/logout")
//	public String logout() {
//		SecurityContextHolder.getContext().setAuthentication(null);
//		
//		return "redirect:/books/list";
//	}
}
