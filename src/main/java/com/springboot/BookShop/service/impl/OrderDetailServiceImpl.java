package com.springboot.BookShop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.BookShop.dao.OrderDetailRepository;
import com.springboot.BookShop.entity.OrderDetail;
import com.springboot.BookShop.service.OrderDetailService;

@Component
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	public List<OrderDetail> listOrderDetailInfos(Integer orderId) {
		
		return orderDetailRepository.listOrderDetailsInfos(orderId);
	}

}
