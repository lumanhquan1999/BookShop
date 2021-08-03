package com.springboot.BookShop.model;

import com.springboot.BookShop.entity.User;

public class CustomerForm {

	private String name;
	private String address;
	private String email;
	private String phoneNumber;
	
	private boolean valid;
	
	public CustomerForm() {
		
	}
	
	public CustomerForm(CustomerInfo customerInfo) {
		if (customerInfo != null) {
			this.name = customerInfo.getName();
			this.address = customerInfo.getAddress();
			this.email = customerInfo.getEmail();
			this.phoneNumber = customerInfo.getPhoneNumber();
		}
	}
	
	public CustomerForm(User user) {
		if (user != null) {
			this.name = user.getName();
			this.email = user.getEmail();
			this.phoneNumber = user.getTelephoneNumber();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
