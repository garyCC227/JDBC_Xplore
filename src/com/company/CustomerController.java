package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CustomerController {
	public static void main(String[] args) {
		try {
			// step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// step2 create the connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", "system", "admin");

			// step3 create the statement object
			Statement stmt = con.createStatement();
			Customer c = new Customer(1, 1, "a ", "b ", 5, "c");
			CustomerController c1 = new CustomerController();
			c1.createCustomer(stmt, c);

			// step5 close the connection object
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void createCustomer(Statement stmt, Customer customer) throws Exception {

		// generate new customerID
		ResultSet rs = stmt.executeQuery("select max(accountid) as max_ from accountstatus");
		rs.next();
		customer.customerid = rs.getInt("max_") + 1;

		// insert record into db
		

	
		String sql = "INSERT INTO customerstatus(customerid, ssnid, email, fullname, age, address ) VALUES ("+customer.customerid+",'" +customer.ssnid+ "','"+customer.email+ "','"+customer.fullname+ "', "+ customer.age + ",'" + customer.address + "')";
		stmt.execute(sql);

		// show success message
		System.out.println("Customer creation initiated successfully");
	}

	public void updateCustomer(Statement stmt, int customerid, String email, String fullname, int age, String address)
			throws Exception {

		// update record into db

		String sql = "UPDATE customerstatus SET email =" + email + ",fullname='" + fullname + "', age=" + age
				+ ", address='" + address + "'  WHERE customerid =" + customerid + ")";
		stmt.execute(sql);

		// show success message
		System.out.println("Customer update successfully");
	}

}
