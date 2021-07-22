package com.springboot.BookShop.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.springboot.BookShop.entity.User;
import com.springboot.BookShop.service.UserService;
import com.springboot.BookShop.service.Utility;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String showSingUpForm(Model model) {
		
		model.addAttribute("user", new User());
		return "register/signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(User user, HttpServletRequest request) 
			throws UnsupportedEncodingException, MessagingException {
		
		userService.registerUser(user);
		
		String siteURL = Utility.getSiteURL(request);
		userService.sendVerificationEmail(user, siteURL);
		
		return "register/register_success";
	}
	
	@GetMapping("/list_users")
	public String viewUserList(Model model) {
		
		List<User> listUsers = userService.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		
		boolean verified = userService.verify(code);
		
		String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
		model.addAttribute("pageTitle", pageTitle);
		
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
	
	@GetMapping("/users/export")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String fileName = "users" + currentDateTime + ".csv";
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		
		response.setHeader(headerKey,  headerValue);
		
		List<User> listUsers = userService.listAll();
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "E-mail", "Full Name", "Roles", "Enable"};
		String[] nameMapping = {"Id", "email", "name", "roles", "enable"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvWriter.write(user,  nameMapping);
		}
		
		csvWriter.close();
	}
	
}
