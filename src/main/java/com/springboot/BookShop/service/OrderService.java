package com.springboot.BookShop.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.springboot.BookShop.entity.Order;
import com.springboot.BookShop.model.CartInfo;

@Service
public interface OrderService {

	void saveOrder(CartInfo cartInfo);
	
	Order getOrder(Integer Id);
	
	Page<Order> listAllOrder(int pageNumber);
	
	Order getOrderInfo(Integer Id);
}
