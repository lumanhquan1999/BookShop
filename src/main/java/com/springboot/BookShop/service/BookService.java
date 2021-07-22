package com.springboot.BookShop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.BookShop.dao.BookRepository;
import com.springboot.BookShop.entity.Book;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	

	public Page<Book> listAll(int pageNumber, String sortField, String sortDir, 
			String keyword) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNumber - 1, 1, sort);
		
		if (keyword != null) {
			return bookRepository.findAll(keyword, pageable);
		}
		
		return bookRepository.findAll(keyword, pageable);
	}

	public Book findById(int Id) {
		
		Optional<Book> result = bookRepository.findById(Id);
		
		Book book = null;
		
		if (result.isPresent()) {
			
			book = result.get();
		}
		else {
			throw new RuntimeException("Did not find book id - " + Id);
		}
		return book;
	}

	public Book save(Book book) {
		
		bookRepository.save(book);
		
		return book;
	}

	public void deleteById(int Id) {
		
		bookRepository.deleteById(Id);
	}

	public void deleteAll() {
		
		bookRepository.deleteAll();
	}

}
