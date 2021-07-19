package com.company;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Account {
    int accountId;
    int customerId;
    String accountType;
    String status;
    String lastUpdated;
    int balance;

    public Account(int customerId, String accountType, String status, String lastUpdated, int balance) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.balance = balance;
    }

    public Account(int accountId, int customerId, String accountType, String status, String lastUpdated, int balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountType = accountType;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.balance = balance;
    }

    protected boolean validate(){
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

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", accountType='" + accountType + '\'' +
                ", status='" + status + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", balance=" + balance +
                '}';
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
