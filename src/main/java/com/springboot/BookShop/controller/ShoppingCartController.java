package com.springboot.BookShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.BookShop.entity.CartItem;
import com.springboot.BookShop.entity.User;
import com.springboot.BookShop.service.ShoppingCartServiceImpl;
import com.springboot.BookShop.service.UserServiceImpl;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartServiceImpl cartService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping("/cart")
	public String showShoppingCart(Model model, 
			@AuthenticationPrincipal Authentication authentication) {
		
		User user = userService.getCurrentlyLoggedInUser(authentication);
		List<CartItem> cartItems = cartService.listCartItems(user);
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("pageTitle", "Shopping Cart");
		return "shopping_cart";
	}
}
