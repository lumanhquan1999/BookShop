package com.springboot.BookShop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.BookShop.entity.OrderDetail;

@Service
public interface OrderDetailService {

	List<OrderDetail> listOrderDetailInfos(Integer orderId);
}
