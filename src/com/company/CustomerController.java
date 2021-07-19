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
	

//delete customer row
public void deleteCustomer(Statement stmt, int id) throws Exception{
        String query = String.format("delete from customerstatus where customerid = %d", id);
        stmt.execute(query);
        System.out.println("Customer deletion initiated successfully");
    }

//pulls customer details for customer id
public ArrayList<Customer> getCustomerByCustomerId(Statement stmt, int id) throws Exception{
        String query = String.format("select * from customerstatus where customerid = %d order by accountid", id);
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Customer> result = new ArrayList<>();

        while(rs.next()){ 
            int custId = rs.getInt("customerid");
            String email = rs.getString(2);
            String fullName = rs.getString(3);
            int age = rs.getInt("4");
            String address = rs.getString(5);

            Customer cust = new Customer(custId, email, fullName, age, address);
            result.add(cust);
        }

        String header = "|  Customer ID  | Email | Full Name |  Age |  Address  |";
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println(header);
        System.out.println("------------------------------------------------------------------------------------");
        for(Customer cust: result){
            System.out.println("|  " + cust.custId + "  |  " +  cust.email + "  |  "+ cust.fullName+"  |  " + cust.age + "  | " + cust.address + " |");
        }

        return result;
    }

}
