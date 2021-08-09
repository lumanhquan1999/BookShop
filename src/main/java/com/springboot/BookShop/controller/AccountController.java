package com.springboot.BookShop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.BookShop.entity.User;
import com.springboot.BookShop.service.MyUserDetails;
import com.springboot.BookShop.service.UserService;
import com.springboot.BookShop.utils.Utils;

@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal MyUserDetails loggedUser, 
			Model model) {
		
		String username = loggedUser.getUsername();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		return "account";
	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal MyUserDetails loggedUser,
			@RequestParam("image") MultipartFile file) throws IOException {
		
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			user.setAvatar(fileName);
			User savedUser = userService.updateUser(user);
			
			String uploadDir = "avatar-images/" + savedUser.getId();
			
			
			
		}
		return null;
	}
}
