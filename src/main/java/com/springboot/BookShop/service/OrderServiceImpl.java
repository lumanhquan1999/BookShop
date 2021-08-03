package com.springboot.BookShop.service;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.BookShop.dao.OrderRepository;
import com.springboot.BookShop.entity.Order;
import com.springboot.BookShop.model.CartInfo;
import com.springboot.BookShop.model.CustomerInfo;
import com.springboot.BookShop.model.OrderInfo;

@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Transactional
	public Order saveOrder(CartInfo cartInfo) {
		Order order = new Order();
		
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());
		
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhoneNumber());
		order.setCustomerAddress(customerInfo.getAddress());
		
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//			order.setUser(null);
//		}
		
		return orderRepository.save(order);
		/**
		List<CartLineInfo> lines = cartInfo.getCartLines();
		
		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getQuantity());
			
			Integer id = line.getBookInfo().getId();
			Book book = this.bookService.findById(id);
			detail.setBook(book);
		}
		**/
		
	}

	public Order getOrder(Integer Id) {
		
		Optional<Order> result = orderRepository.findById(Id);
		Order order = null;
		
		if (result.isPresent()) {
			order = result.get();
		} else {
			throw new RuntimeException("Did not found order id - Id");
		}
		
		return order;
	}
	
	public OrderInfo getOrderInfo(Integer Id) {
		Order order = this.getOrder(Id);
		if (order == null) {
			return null;
		}
		
		return new OrderInfo(order.getId(), order.getOrderDate(),
				order.getAmount(), order.getCustomerName(), 
				order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
	}
}
