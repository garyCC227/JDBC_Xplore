package com.company;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AccountController {
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
        String query = String.format("Insert into accountstatus(accountid, customerid, accounttype, status, message, lastupdated, balance) values (%d, %d, '%s', '%s', %s, to_date('%s', 'yyyy/mm/dd'), %d)", account.accountId, account.customerId, account.accountType, account.status, account.message, account.lastUpdated, account.balance);
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
            String accMessage = rs.getString(5); // can be null
            String accLastUpdated = rs.getString(6);
            int accBalance = rs.getInt(7);

            Account acc = new Account(accId, cusId, accType, accStatus, accMessage, accLastUpdated, accBalance);
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
