package com.company;

import oracle.jdbc.internal.XSCacheOutput;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try{
//step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

//step2 create  the connection object
            Connection con= DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe","system","admin");

//step3 create the statement object
            Statement stmt=con.createStatement();
            Account ac = new Account(1,"saving", null, null, null, 0);
            AccountController acc = new AccountController();
            acc.createAccount(stmt, ac);
            acc.getAccountByCustomerId(stmt, 1);
            acc.deleteAccount(stmt, 11);
            acc.getAccountByCustomerId(stmt, 1);

//step5 close the connection object
            con.close();

        }catch(Exception e){ System.out.println(e);}

    }

}