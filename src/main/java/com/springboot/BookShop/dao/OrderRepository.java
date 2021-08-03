package com.springboot.BookShop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	
}
