package com.company;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Customer {

	int customerid;
	int ssnid;
	int age;
	String email;
	String fullname;
	String address;

	public Customer(int customerid, int ssnid, String email, String fullname, int age, String address) {
		this.customerid = customerid;
		this.ssnid = ssnid;
		this.email = email;
		this.fullname = fullname;
		this.age = age;
		this.address = address;

	}
	
	public Customer(int customerid, String email, String fullname, int age, String address) {
		this.customerid = customerid;
		this.email = email;
		this.fullname = fullname;
		this.age = age;
		this.address = address;

	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getSsnid() {
		return ssnid;
	}

	public void setSsnid(int ssnid) {
		this.ssnid = ssnid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer{" + "customerid=" + customerid + ", ssnid='" + ssnid + '\'' + ", email='" + email + '\''
				+ ", fullname='" + fullname + '\'' + ", age='" + age + '\'' + ", address=" + address + '}';
	}

}
