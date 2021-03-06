package com.springboot.BookShop.model;

import java.util.ArrayList;
import java.util.List;

public class CartInfo {

	private CustomerInfo customerInfo;
	private final List<CartLineInfo> cartLines = new ArrayList<>();
	
	public CartInfo() {
		
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setUserInfo(CustomerInfo userInfo) {
		this.customerInfo = userInfo;
	}

	public List<CartLineInfo> getCartLines() {
		return cartLines;
	}
	
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	private CartLineInfo findLineById(Integer id) {
		for (CartLineInfo line : this.cartLines) {
			if (line.getBookInfo().getId().equals(id)) {
				return line;
			}
		}
		return null;
	}
	
	public void addBook(BookInfo bookInfo, int quantity) {
		CartLineInfo line = this.findLineById(bookInfo.getId());
		
		if (line == null) {
			line = new CartLineInfo();
			line.setQuantity(1);
			line.setBookInfo(bookInfo);
			this.cartLines.add(line);
		} else {
			line.setQuantity(line.getQuantity() + 1);		}
	}
	
	public void updateBook(Integer id, int quantity) {
		CartLineInfo line = this.findLineById(id);
		
		if (line != null) {
			if (quantity <= 0) {
				this.cartLines.remove(line);
			} else {
				line.setQuantity(quantity);
			}
		}
	}
	
	public void removeBook(BookInfo bookInfo) {
		CartLineInfo line = this.findLineById(bookInfo.getId());
		if (line != null) {
			this.cartLines.remove(line);
		}
	}
	
	public boolean isEmpty() {
		return this.cartLines.isEmpty();
	}
	
	public boolean isValidCustomer() {
		return this.customerInfo != null && this.customerInfo.isValid();
	}
	
	public int getQuantityTotal() {
		int quantity = 0;
		for (CartLineInfo line : this.cartLines) {
			quantity += line.getQuantity();
		}
		return quantity;
	}	
	
	public double getAmountTotal() {
		double total = 0;
		for (CartLineInfo line : this.cartLines) {
			total += line.getAmount();
		}
		return total;
	}
	
	public void updateQuantity(CartInfo cartForm) {
		if (cartForm != null) {
			List<CartLineInfo> lines = cartForm.getCartLines();
			for (CartLineInfo line : lines) {
				this.updateBook(line.getBookInfo().getId(), line.getQuantity());
			}
		}
	}
}
