package com.springboot.BookShop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.springboot.BookShop.entity.Role;
import com.springboot.BookShop.entity.User;
import com.springboot.BookShop.service.UserService;
import com.springboot.BookShop.utils.Utils;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/users/new")
	public String showUserForm(Model model) {
		
		List<Role> listRoles = userService.listRoles();
		
		User user = new User();
		model.addAttribute("user", user);
		user.setEnable(true);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create new user");
		return "user/user_form";
	}
	
	@PostMapping("/createUser")
	public String createUser(User user, RedirectAttributes redirectAttributes)
			throws MessagingException, IOException {
		
		userService.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return "redirect:/list_users";
	}
	
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
			
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Registration");
		return "user/user_register";
	}

	@PostMapping("/saveUser")
	public String processRegistration(@ModelAttribute("user") User user, 
			@RequestParam("image") MultipartFile file, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) 
			throws MessagingException, IOException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		user.setAvatar(fileName);
		
		User savedUser = userService.saveRegister(user);
		
		String uploadDir = "./avatar-images/" + savedUser.getId();
		
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save upload file: " + fileName);
		}
		
		String siteURL = Utils.getSiteURL(request);
		userService.sendVerificationEmail(user, siteURL);

		return "/user/register_success";
	}
	
	@GetMapping("/list_users")
	public String viewUserList(Model model) {
		
		return listUserByPage(model, 1, "");
	}
	
	@GetMapping("/userPage/{pageNumber}")
	public String listUserByPage(Model model, @PathVariable("pageNumber") int currentPage,
			@Param("keyword") String keyword) {
		Page<User> page = userService.listAll(currentPage, keyword);
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		
		List<User> users = page.getContent();
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("listUsers", users);
		model.addAttribute("keyword", keyword);
		
		return "users";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		
		boolean verified = userService.verify(code);
		
		String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
		model.addAttribute("pageTitle", pageTitle);
		
		return "user/" + (verified ? "verify_success" : "verify_fail");
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
		
		List<User> listUsers = userService.findAll();
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "E-mail", "Full Name", "Roles", "Enable"};
		String[] nameMapping = {"Id", "email", "name", "roles", "enable"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvWriter.write(user,  nameMapping);
		}
		
		csvWriter.close();
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			User user = userService.findById(id);
			List<Role> listRoles = userService.listRoles();
			
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit user");
			model.addAttribute("listRoles", listRoles);
			
			return "user/user_form";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/list_users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
			
			return "redirect:/list_users";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/list_users";
		}
	}
	
	@GetMapping("users/{id}/enable/{status}")
	public String updateUserEnableStatus(@PathVariable(name="id") Integer id, Model model,
			@PathVariable("status") boolean enable, RedirectAttributes redirectAttributes) {
		
		userService.updateUserEnabledStatus(id, enable);
		String status = enable ? "enable" : "disable";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/list_users";
	}
}
