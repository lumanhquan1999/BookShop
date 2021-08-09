package com.springboot.BookShop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	@Query("SELECT d FROM OrderDetail d WHERE d.order.id = ?1")
	List<OrderDetail> listOrderDetailsInfos(Integer orderId);
}
