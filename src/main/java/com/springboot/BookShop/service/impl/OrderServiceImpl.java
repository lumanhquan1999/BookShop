package com.springboot.BookShop.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.springboot.BookShop.dao.OrderDetailRepository;
import com.springboot.BookShop.dao.OrderRepository;
import com.springboot.BookShop.entity.Book;
import com.springboot.BookShop.entity.Order;
import com.springboot.BookShop.entity.OrderDetail;
import com.springboot.BookShop.model.CartInfo;
import com.springboot.BookShop.model.CartLineInfo;
import com.springboot.BookShop.model.CustomerInfo;
import com.springboot.BookShop.service.BookService;
import com.springboot.BookShop.service.OrderService;

@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private BookService bookService;
	
	public void saveOrder(CartInfo cartInfo) {
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
		
		orderRepository.save(order);
	
		List<CartLineInfo> lines = cartInfo.getCartLines();
		
		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getBookInfo().getPrice());
			detail.setQuantity(line.getQuantity());
			
			Integer id = line.getBookInfo().getId();
			Book book = this.bookService.findById(id);
			detail.setBook(book);
			orderDetailRepository.save(detail);
		}	
	}

	public Page<Order> listAllOrder(int pageNumber) {
		
		Pageable pageable = PageRequest.of(pageNumber - 1, 4);
		
		return orderRepository.findAll(pageable);
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
	
	public Order getOrderInfo(Integer Id) {
		Order order = this.getOrder(Id);
		if (order == null) {
			return null;
		}
		
		return order;
	}
}
