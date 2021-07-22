package com.springboot.BookShop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {

	@Query("SELECT p FROM Book p WHERE "
			+ "CONCAT(p.name, ' ', p.category) "
			+ "LIKE %?1%")
	public Page<Book> findAll(String keyword, Pageable pageable);
	
}
