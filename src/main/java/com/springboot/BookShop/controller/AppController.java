package com.springboot.BookShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	
	@GetMapping("")
	public String viewHomePage() {
		
		return "index";
	}
	
	@GetMapping("/403")
	public String error403() {
		
		return "403";
	}
}
