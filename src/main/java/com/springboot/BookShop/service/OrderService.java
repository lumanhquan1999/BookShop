package com.springboot.BookShop.service;

import org.springframework.stereotype.Service;

import com.springboot.BookShop.entity.Order;
import com.springboot.BookShop.model.CartInfo;

@Service
public interface OrderService {

	Order saveOrder(CartInfo cartInfo);
	
	Order getOrder(Integer Id);
	
}
