package com.springboot.BookShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.BookShop.entity.Order;
import com.springboot.BookShop.entity.OrderDetail;
import com.springboot.BookShop.service.OrderDetailService;
import com.springboot.BookShop.service.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@GetMapping("/orders/list")
	public String listOrder(Model model) {
		
		return listOrderByPage(model, 1);
	}
	
	@GetMapping("/orderPage/{orderPageNumber}")
	public String listOrderByPage(Model model, @PathVariable("orderPageNumber") int currentPage) {
		
		Page<Order> page = orderService.listAllOrder(currentPage);
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		
		List<Order> orders = page.getContent();
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("orders", orders);
		return "orders/list-orders";
	}
	
	@GetMapping("/orders/order")
	public String orderView(Model model, @RequestParam("orderId") Integer orderId) {
		
		Order order = null;
		if(orderId != null) {
			order = this.orderService.getOrderInfo(orderId);
		}
		
		if(order == null) {
			return "redirect:/orders/list";
		}
		
		List<OrderDetail> orderDetails = this.orderDetailService.listOrderDetailInfos(orderId);
		order.setOrderDetails(orderDetails);
		
		model.addAttribute("orderInfo", order);
		
		return "orders/order_detail";
	}
}
