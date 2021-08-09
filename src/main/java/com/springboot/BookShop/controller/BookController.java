package com.springboot.BookShop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.BookShop.entity.Book;
import com.springboot.BookShop.service.BookService;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping("/books/list")
	public String listBook(Model model) {
		
		return listByPage(model, 1, "name", "asc", "");
	}
	
	@GetMapping("/page/{pageNumber}")
	public String listByPage(Model model, @PathVariable("pageNumber") int currentPage, 
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, 
			@Param("keyword") String keyword) {
		
		Page<Book> page = bookService.listAll(currentPage, sortField, sortDir, keyword);
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		
		List<Book> books = page.getContent();
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("books", books);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "books/list-books";
	}
	
	@GetMapping("/books/showFormForAdd")
	public String showFormForAdd(Model model) {
		
		Book book = new Book();
		
		model.addAttribute("book", book);
		
		return "books/book-form";
	}
	
	@GetMapping("books/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("bookId") Integer bookId, Model model) {
		
		Book book = bookService.findById(bookId);
		
		model.addAttribute("book", book);
		
		return "books/book-form";
	}
	
	@PostMapping("books/save")
	public String save(@ModelAttribute("book") Book book, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		book.setImage(fileName);
		
		Book savedBook = bookService.save(book);
		
		String uploadDir = "./book-images/" + savedBook.getId();
		
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save upload file: " + fileName);
		}

		return "redirect:/books/list";
	}
	
	@GetMapping("books/deleteById")
	public String deleteById(@RequestParam("bookId") Integer bookId) {
	
		bookService.deleteById(bookId);
		
		return "redirect:/books/list";
	}
	
}
