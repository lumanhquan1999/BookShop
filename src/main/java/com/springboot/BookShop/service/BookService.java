package com.springboot.BookShop.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Book;

@Repository
public interface BookService {

	Page<Book> listAll(int pageNumber, String sortField, String sortDir, 
			String keyword);
	
	Book findById(int Id);
	
	Book save(Book book);
	
	void deleteById(int Id);
}
