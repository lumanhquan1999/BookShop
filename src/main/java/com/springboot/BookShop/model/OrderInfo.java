package com.springboot.BookShop.model;

import java.util.Date;
import java.util.List;

public class OrderInfo {

	private Integer id;
	private Date orderDate;
	private double amount;
	
	private String customerName;
	private String customerAddress;
	private String customerEmail;
	private String customerPhone;
	
	private List<OrderDetailInfo> details;
	
	public OrderInfo() {
		
	}

	public OrderInfo(Integer id, Date orderDate, double amount, String customerName, String customerAddress,
			String customerEmail, String customerPhone) {
		this.id = id;
		this.orderDate = orderDate;
		this.amount = amount;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomername() {
		return customerName;
	}

	public void setCustomername(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}
}
