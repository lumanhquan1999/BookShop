package com.springboot.BookShop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.BookShop.entity.Book;
import com.springboot.BookShop.model.BookInfo;
import com.springboot.BookShop.model.CartInfo;
import com.springboot.BookShop.model.CustomerForm;
import com.springboot.BookShop.model.CustomerInfo;
import com.springboot.BookShop.service.BookService;
import com.springboot.BookShop.service.OrderService;
import com.springboot.BookShop.utils.Utils;

@Controller
public class CartController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BookService bookService;
	
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
		return "cart/shopping_cart";
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
	
	@GetMapping("/shoppingCartCustomer")
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		}
		
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		CustomerForm customerForm = new CustomerForm(customerInfo);
		model.addAttribute("customerForm", customerForm);
		
		return "cart/shopping_cart_customer";
	}
	
	@PostMapping("/shoppingCartCustomer")
	public String shoppingCartCustomerSave(HttpServletRequest request, Model model,
		@ModelAttribute("customerForm") @Validated CustomerForm customerForm,
		BindingResult result, final RedirectAttributes redirectAttributes) {
			if (result.hasErrors()) {
				customerForm.setValid(false);
				return "cart/shopping_cart_customer";
			}
			
			customerForm.setValid(true);
			CartInfo cartInfo = Utils.getCartInSession(request);
			CustomerInfo customerInfo = new CustomerInfo(customerForm);
			cartInfo.setCustomerInfo(customerInfo);
			
			return "redirect:/shoppingCartConfirmation";
		}
	
	@GetMapping("/shoppingCartConfirmation")
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		
		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			return "redirect:/shoppingCartCustomer";
		}
		model.addAttribute("myCart", cartInfo);
		
		return "cart/shopping_cart_confirmation";
	}
	
	@PostMapping("/shoppingCartConfirmation")
	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} 
		System.out.println(cartInfo.getCustomerInfo().getName());
		/**
		try {
			orderService.saveOrder(cartInfo);
		} catch (Exception e) {
			return "cart/shopping_cart_confirmation";
		}**/
		orderService.saveOrder(cartInfo);
		System.out.println(cartInfo.getCustomerInfo().getName());
		Utils.removeCartInSession(request);
		
		Utils.storeLastOrderedCartInSession(request, cartInfo);
		
		return "redirect:/shoppingCartFinalize";
	}
	
	@GetMapping("/shoppingCartFinalize")
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {
		CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
		
		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		
		return "cart/shopping_cart_finalize";
	}
}
