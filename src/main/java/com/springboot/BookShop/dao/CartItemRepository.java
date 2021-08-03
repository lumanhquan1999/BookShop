package com.springboot.BookShop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.CartItem;
import com.springboot.BookShop.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	List<CartItem> findByUser(User user);
}
