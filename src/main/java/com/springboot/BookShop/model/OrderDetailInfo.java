package com.springboot.BookShop.model;

public class OrderDetailInfo {

	private Integer id;
	private String bookId;
	private String productName;
	
	private int quantity;
	private double price;
	private double amount;
	
	public OrderDetailInfo() {
		
	}

	public OrderDetailInfo(Integer id, String bookId, String productName, int quantity, double price, double amount) {
		this.id = id;
		this.bookId = bookId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
