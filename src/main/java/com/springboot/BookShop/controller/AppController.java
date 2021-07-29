package com.springboot.BookShop.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.BookShop.entity.Book;
import com.springboot.BookShop.model.BookInfo;
import com.springboot.BookShop.model.CartInfo;
import com.springboot.BookShop.service.BookService;
import com.springboot.BookShop.utils.Utils;

@Controller
public class AppController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("")
	public String viewHomePage() {
		
		return "index";
	}
	
	@GetMapping("/403")
	public String error403() {
		
		return "403";
	}
	
	@GetMapping("/buyBook")
	public String listBookHandler(HttpServletRequest request , Model model,
			@RequestParam(value="id", defaultValue="") Integer id) {
		Book book = null;
		if (id != null) {
			book = bookService.findById(id);
		}
		
		if (book != null) {
			CartInfo cartInfo = Utils.getCartInSession(request);
			
			BookInfo bookInfo = new BookInfo(book);
			
			cartInfo.addBook(bookInfo, 1);
		}
		return "redirect:/shoppingCart";
	}
	
	
	@GetMapping("/shoppingCart")
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);
		
		model.addAttribute("cartForm", myCart);
		return "shopping_cart";
	}
	
	@PostMapping("/shoppingCart")
	public String shoppingCartUpdateQuantity(HttpServletRequest request, Model model,
			@ModelAttribute("cartForm") CartInfo cartForm) {
		
		CartInfo cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);
		
		return "redirect:/shoppingCart";
	}
	
	@GetMapping("/shoppingCartRemoveBook")
	public String removeBook(HttpServletRequest request, Model model,
			@RequestParam(value="id", defaultValue="") Integer id) {
		Book book = null;
		if (id != null) {
			book = bookService.findById(id);
		}
		
		if (book != null) {
			CartInfo cartInfo = Utils.getCartInSession(request);
			BookInfo bookInfo = new BookInfo(book);
			cartInfo.removeBook(bookInfo);
		}
		
		return "redirect:/shoppingCart";
	}
	
	@GetMapping("/bookImage")
	public void bookImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Integer id) throws IOException {
		Book book = null;
		if (id != null) {
			book = this.bookService.findById(id);
		}
		
		if (book != null && book.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png");
		}
	}
}
