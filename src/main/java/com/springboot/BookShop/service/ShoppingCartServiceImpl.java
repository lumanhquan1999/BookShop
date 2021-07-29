package com.springboot.BookShop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.BookShop.dao.CartItemRepository;
import com.springboot.BookShop.entity.CartItem;
import com.springboot.BookShop.entity.User;

@Service
public class ShoppingCartServiceImpl implements ShopppingCartService {

	@Autowired
	private CartItemRepository cartRepo;
	
	public List<CartItem> listCartItems(User user) {
		
		return cartRepo.findByUser(user);
	}
}
