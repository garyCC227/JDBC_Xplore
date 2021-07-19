package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AccountController {
    public static void main(String[] args) {
        try{
//step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

//step2 create  the connection object
            Connection con= DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe","system","chenqq227");

//step3 create the statement object
            Statement stmt=con.createStatement();
            Account ac = new Account(1,"saving", null, null, 0);
            AccountController acc = new AccountController();
            acc.createAccount(stmt, ac);
            acc.getAccountByCustomerId(stmt, 1);
            acc.deleteAccount(stmt, 6);
            acc.getAccountByCustomerId(stmt, 1);

//step5 close the connection object
            con.close();

        }catch(Exception e){ System.out.println(e);}

    }



    //Account creation initiated successfully” Or Relevant error message to be displayed
    public void createAccount(Statement stmt, Account account) throws Exception{
        //set status == active, as we just created the account
        account.status = "active";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        account.lastUpdated = (sdf.format(new Date())).toString();


        //Validate account data
        if(!account.validate()) return;

        //generate new accountID
        ResultSet rs = stmt.executeQuery("select max(accountid) as max_ from accountstatus");
        rs.next();
        account.accountId = rs.getInt("max_") + 1;

        //insert record into db
        String query = String.format("Insert into accountstatus(accountid, customerid, accounttype, status, lastupdated, balance) values (%d, %d, '%s', '%s', to_date('%s', 'yyyy/mm/dd'), %d)", account.accountId, account.customerId, account.accountType, account.status, account.lastUpdated, account.balance);
        stmt.execute(query);

        //show success message
        System.out.println("Account creation initiated successfully");
    }

    public ArrayList<Account> getAccountByCustomerId(Statement stmt, int id) throws Exception{
        String query = String.format("select * from accountstatus where customerid = %d", id);
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Account> result = new ArrayList<>();

        while(rs.next()){
            int accId = rs.getInt("accountid");
            int cusId = rs.getInt("customerid");
            String accType = rs.getString(3);
            String accStatus = rs.getString(4);
            String accLastUpdated = rs.getString(5);
            int accBalance = rs.getInt(6);

            Account acc = new Account(accId, cusId, accType, accStatus, accLastUpdated, accBalance);
            result.add(acc);
        }

        //TODO: can be removed
        for(Account acc : result){
            System.out.println(acc.toString());
        }

        return result;
    }


    //Success message: Account deletion initiated successfully
    public void deleteAccount(Statement stmt, int id) throws Exception{
        String query = String.format("delete from accountstatus where accountid = %d", id);
        stmt.execute(query);
        System.out.println("Account deletion initiated successfully");
    }




}
