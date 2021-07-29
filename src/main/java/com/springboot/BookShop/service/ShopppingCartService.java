package com.springboot.BookShop.service;

import java.util.List;

import com.springboot.BookShop.entity.CartItem;
import com.springboot.BookShop.entity.User;

public interface ShopppingCartService {

	List<CartItem> listCartItems(User user);
}
