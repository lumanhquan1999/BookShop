package com.springboot.BookShop.model;

public class CartLineInfo {

	private BookInfo bookInfo;
	private int quantity;
	
	public CartLineInfo() {
		
	}

	public BookInfo getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getAmount() {
		return this.bookInfo.getPrice() * this.quantity;
	}
}
