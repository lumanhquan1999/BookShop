package com.springboot.BookShop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Order;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

	Page<Order> findAll(Pageable pageable);
}
