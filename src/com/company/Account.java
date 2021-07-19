package com.company;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Account {
    private int accountId;
    private int customerId;
    private String accountType;
    private String status;
    private String message;
    private String lastUpdated;
    private int balance;

    public Account(int customerId, String accountType, String status, String message, String lastUpdated, int balance) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.status = status;
        this.message = message;
        this.lastUpdated = lastUpdated;
        this.balance = balance;
    }

    public Account(int accountId, int customerId, String accountType, String status, String message, String lastUpdated, int balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountType = accountType;
        this.status = status;
        this.message = message; // can be null
        this.lastUpdated = lastUpdated;
        this.balance = balance;
    }

    //Account creation initiated successfully” Or Relevant error message to be displayed
    public void createAccount(Statement stmt) throws Exception{
        //set status == active, as we just created the account
        this.status = "active";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        this.lastUpdated = (sdf.format(new Date())).toString();

        //Validate account data
        if(!validate()) return;

        //generate new accountID
        ResultSet rs = stmt.executeQuery("select max(accountid) as max_ from accountstatus");
        rs.next();
        this.accountId = rs.getInt("max_") + 1;

        //insert record into db
        String query = String.format("Insert into accountstatus(accountid, customerid, accounttype, status, message, lastupdated, balance) values (%d, %d, '%s', '%s', '%s', to_date('%s', 'yyyy/mm/dd'), %d)", accountId, customerId, accountType, status, null, lastUpdated, 0);
        if(stmt.execute(query)){
            System.out.println("Success");
        }

        //show success messgae
        System.out.println("Account creation initiated successfully");
    }

    private boolean validate(){
        //validate account type
        if(accountType != "saving"  && accountType !="cheque"){
            System.out.println("Error: Account type is neither saving nor cheque");
            return false;
        }

        //validate status
        if( !Arrays.asList("active", "closed", "inactive").contains(status) ){
            System.out.println("Error: Account status is not one of the active|closed|inactive. ");
            return false;
        }

        return true;
    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
