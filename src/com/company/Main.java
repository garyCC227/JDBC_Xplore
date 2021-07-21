package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception{
        while(true) {

            System.out.println("==================================================================================================================");
            String AccType = "";
            boolean invalid = true;
            boolean input1;
            boolean input2;
            Scanner input = new Scanner(System.in);
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
            Matcher matcher;
            Connection conn = LoginController.createConnection();

            /*
             * LOGIN Section
             * Inputs:
             * -UserID: alphabetic or alphanumeric, min 8 characters
             * -Password: alphabetic or alphanumeric, min 10 characters
             */

            String userID = "";
            String password = "";
            while (invalid) {
                //handle any unexpected exceptions
                try {
                    System.out.println("Sign In Now");
                    System.out.println("Enter your userID:");
                    userID = input.nextLine();
                    System.out.println("Enter your Password:");
                    password = input.nextLine();
                    invalid = false;
                } catch (Exception e) {
                    System.out.println("Invalid UserID or Password");
                }
                //check for correct input length
                if (userID.length() < 8 || password.length() < 10) {
                    invalid = true;
                    System.out.println("Error: UserID must has at least 8 characters | Password must has at least 10 characters");
                    continue;
                }
                //check for special characters
                matcher = pattern.matcher(userID);
                input1 = matcher.find();
                matcher = pattern.matcher(password);
                input2 = matcher.find();
                if (input1 || input2) {
                    invalid = true;
                    System.out.println("Error: Special characters not allowed");
                    continue;

                }
                //Add your log in function here e.g AccType = YourFunction();
                //if no account found AccType should = ""
                AccType = LoginController.checkLogin(conn, userID, password);
                //
                if (AccType == "") {
                    invalid = true;
                    System.out.println("Error: Username and password are incorrect");
                    continue;

                }


            }


            /*
             * Main while loop
             */
            String STATUS = "OptionPanel1";
            boolean ACTIVE = true;
            while (ACTIVE) {
                System.out.println("=====================================================================================================================================================");
                if (AccType == "Customer" && STATUS == "OptionPanel1") {
                    showOptions(AccType);
                    int cusOption = inputOption(input, "Please enter the number of one of the above options:", 1, 4);
                    switch (cusOption) {
                        case 1:
                            STATUS = "OptionPanel1Sub1";
                            break;
                        case 2:
                            STATUS = "OptionPanel1Sub2";
                            break;
                        case 3:
                            STATUS = "OptionPanel1Sub3";
                            break;
                        case 4:
                            ACTIVE = false;
                            break;
                    }
                    continue;
                }
                //CREATE UPDATE DELETE
                if (AccType == "Customer" && STATUS == "OptionPanel1Sub1") {
                    print("1. Create Customer");
                    print("2. Update Customer");
                    print("3. Delete Customer");
                    print("4. Go Back");
                    int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 4);
                    switch (cusOption) {
                        case 1:
                            STATUS = "CreateCus";
                            break;
                        case 2:
                            STATUS = "UpdateCus";
                            break;
                        case 3:
                            STATUS = "DeleteCus";
                            break;
                        case 4:
                            STATUS = "OptionPanel1";
                    }

                    continue;
                }
                //CREATE CUSTOMER
                if (AccType == "Customer" && STATUS == "CreateCus") {
                    String SSN = digitInput(input, "Enter Customer SSN:", 9, 9);
                    String email = inputString(input, "Enter Email:", 5, 35, false, true);
                    String Name = inputString(input, "Enter Customer Name:", 5, 35, false, true);
                    String Age = inputNumber(input, "Enter Customer Age:", 14, 122);
                    String address = inputString(input, "Enter Customer Address:", 5, 50, true, true);

                    Customer c = new Customer(Integer.parseInt(SSN), email, Name, Integer.parseInt(Age), address);
                    CustomerController c1 = new CustomerController();
                    Statement stmt = conn.createStatement();
                    c1.createCustomer(stmt, c);
                    print("");
                    STATUS = "OptionPanel1Sub1";
                    continue;
                }
                //UPDATE CUSTOMER
                if (AccType == "Customer" && STATUS == "UpdateCus") {
                    Statement stmt = conn.createStatement();
                    CustomerController c1 = new CustomerController();

                    String cid = digitInput(input, "Enter Customer ID:", 3, 3);
                    c1.getCustomerByCustomerId(stmt, Integer.parseInt(cid));

                    String query = String.format("select * from customerstatus where customerid = %d", Integer.parseInt(cid));
                    ResultSet rs = stmt.executeQuery(query);
                    String newName = "";
                    int newAge = 0;
                    String age = "";
                    String newAddress = "";
                    String newEmail = "";
                    boolean check = false;

                    while (rs.next()) {
                        newName = rs.getString("fullname");
                        newAge = rs.getInt("customerid");
                        age = "";
                        newAddress = rs.getString("address");
                        newEmail = rs.getString("email");

                        if (rs.getString("fullname") != "")
                            check = true;
                    }

                    if (check == true) {

                        if (yesNoOption(input, "Change Customer Email ? [y/n]")) {
                            newEmail = inputString(input, "Please enter new Email", 5, 50, true, true);
                        }

                        if (yesNoOption(input, "Change Customer Name? [y/n]")) {
                            newName = inputString(input, "Please enter new Name", 5, 35, false, true);
                        }

                        if (yesNoOption(input, "Change Customer Age? [y/n]")) {
                            age = inputNumber(input, "Please enter new Age", 14, 122);
                        }
                        if (yesNoOption(input, "Change Customer Address ? [y/n]")) {
                            newAddress = inputString(input, "Please enter new Address", 5, 50, true, true);
                        }

                        if (age != "") {
                            newAge = Integer.parseInt(age);
                        }


                        stmt = conn.createStatement();
                        c1.updateCustomer(stmt, Integer.parseInt(cid), newEmail, newName, newAge, newAddress);

                        print("Customer details Updated!");
                    } else {
                        print("Customer not found!");
                    }

                    print("");
                    STATUS = "OptionPanel1Sub1";
                    continue;
                }
                //DELETE CUSTOMER
                if (AccType == "Customer" && STATUS == "DeleteCus") {
                    CustomerController cusController = new CustomerController();
                    Statement stmt = conn.createStatement();
                    cusController.getCustomer(stmt);

                    print("");
                    String customerID = digitInput(input, "Enter Customer ID:", 3, 3);
                    if (yesNoOption(input, "Do you want to delete this Customer [y/n]")) {
                        cusController.deleteCustomer(stmt, Integer.parseInt(customerID));
                    }
                    print("");
                    STATUS = "OptionPanel1Sub1";
                    continue;
                }

                //CREATE AND DELETE ACCOUNT OPTIONS
                if (AccType == "Customer" && STATUS == "OptionPanel1Sub2") {
                    print("1. Create Account");
                    print("2. Delete Account");
                    print("3. Go Back");
                    int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 3);
                    switch (cusOption) {
                        case 1:
                            STATUS = "CreateAccount";
                            break;
                        case 2:
                            STATUS = "DeleteAccount";
                            break;
                        case 3:
                            STATUS = "OptionPanel1";
                            break;
                    }
                    continue;
                }
                //CREATE CUSTOMER ACCOUNT
                if (AccType == "Customer" && STATUS == "CreateAccount") {
                    boolean valid = true;
                    AccountController accController = new AccountController();
                    Statement stmt = conn.createStatement();

                    String customerID = "";
                    String accountType = "";
                    String balance = "";

                    while (valid) {
                        customerID = digitInput(input, "Enter Customer ID:", 3, 3);
                        accountType = inputNumber(input, "Enter 1 for Savings, Enter 2 for Cheque", 1, 2);
                        balance = inputNumber(input, "Please enter balance", 1, 100000);
                        if (accController.checkCustomerID(stmt, Integer.parseInt(customerID))) {
                            valid = false;
                            continue;
                        }
                    }

                    //Put a function here to take the values
                    accountType = (accountType.equals("1")) ? "savings" : "cheque";
                    Account newAcc = new Account(Integer.parseInt(customerID), accountType, null, null, Integer.parseInt(balance));
                    accController.createAccount(stmt, newAcc);
                    print("");
                    STATUS = "OptionPanel1";
                    continue;
                }
                //DELETE CUSTOMER ACCOUNT
                if (AccType == "Customer" && STATUS == "DeleteAccount") {
                    AccountController accountController = new AccountController();
                    Statement stmt = conn.createStatement();
                    String CustomerId = null;
                    while (true) {
                        CustomerId = digitInput(input, "Enter Customer ID:", 3, 3);
                        if (accountController.checkCustomerID(stmt, Integer.parseInt(CustomerId))) {
                            break;
                        }
                    }

                    //show account for customer
                    accountController.getAccountByCustomerId(stmt, Integer.parseInt(CustomerId));
                    print("");
                    String accountId = digitInput(input, "Enter Wanted delete Account ID:", 3, 3);
                    if (yesNoOption(input, "Do you want to delete this Account [y/n]")) {
                        accountController.deleteAccount(stmt, Integer.parseInt(accountId));
                    }

                    print("");
                    STATUS = "OptionPanel1";
                    continue;
                }
                if (AccType == "Customer" && STATUS == "OptionPanel1Sub3") {
                    print("1. Search for Customer by Customer ID");
                    print("2. Search for Customer by SSN");
                    print("3. View All Customers Detail");
                    print("4. View All Account Status");
                    print("5. Go Back");
                    int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 5);
                    if (cusOption == 1) {
                        String customerID = digitInput(input, "Enter Customer ID:", 3, 3);
                        boolean found = searchCustomer(conn, customerID, "customer");
                        if (!found) {
                            print("Customer not found");
                        }
                    }
                    if (cusOption == 2) {
                        String SSN = digitInput(input, "Enter Customer SSN:", 9, 9);
                        boolean foundSSN = searchCustomer(conn, SSN, "ssn");
                        if (!foundSSN) {
                            print("Customer not found!");
                        }
                    }

                    if (cusOption == 3) {
                        Statement stmt = conn.createStatement();
                        CustomerController c1 = new CustomerController();
                        c1.getCustomer(stmt);
                    }

                    if (cusOption == 4) {
                        Statement stmt = conn.createStatement();
                        AccountController.getAccountStatus(stmt);
                    }

                    print("");
                    STATUS = "OptionPanel1";
                    continue;
                }


                //* CASHIER FLOW CONTROL ----------------------------------------------------------------


                if (AccType == "Cashier" && STATUS == "OptionPanel1") {
                    showOptions(AccType);
                    int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 4);
                    switch (cusOption) {
                        case 1:
                            STATUS = "GetAccountInfo";
                            break;
                        case 2:
                            STATUS = "depWitTra"; //deposit withdraw transfer
                            break;
                        case 3:
                            STATUS = "GetAccountTran";
                            break;
                        case 4:
                            ACTIVE = false;
                            break;
                    }
                    continue;
                }
                if (AccType == "Cashier" && STATUS == "GetAccountInfo") {
                    print("");
                    print("1. Search for Accounts by Cutomer ID");
                    print("2. Search for Account by AccountID");
                    print("3. Go Back");
                    int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 3);
                    if (cusOption == 1) {
                        String customerID = digitInput(input, "Enter Customer ID:", 3, 3);
                        boolean found = searchAccount(conn, customerID, "customer");
                        if (!found) {
                            print("Customer not found");
                        }
                    }
                    if (cusOption == 2) {
                        String accountID = digitInput(input, "Enter Account ID:", 3, 3);
                        boolean found = searchAccount(conn, accountID, "account");
                        if (!found) {
                            print("Account not found!");
                        }
                    }

                    if (cusOption == 3) {
                        STATUS = "OptionPanel1";
                    } else {
                        STATUS = "GetAccountInfo";
                    }

                    print("");
                    continue;
                }
                //DEPOSIT WITHDRAW TRANSFER
                if (AccType == "Cashier" && STATUS == "depWitTra") {
                    String accountID = digitInput(input, "Enter Account ID:", 3, 3);
                    boolean found = searchAccount(conn, accountID, "account"); //PUT YOUR SEARCH FUNCTION HERE RETURN FALSE IF NOT FOUND
                    //Put basic customer info balance, type, id e.t.c
                    if (found) {
                        print("1. Deposit");
                        print("2. Withdraw");
                        print("3. Transfer");
                        print("4. Go Back");
                        int cusOption = inputOption(input, "Please enter the number of one of the above options", 1, 4);
                        if (cusOption == 1) { //deposit
                            String depAmount = inputNumber(input, "Enter the amount you wish to deposit", 1, 10000);
                            print("$" + depAmount + " has been added to account " + accountID);
                            print("");
                        }
                        if (cusOption == 2) { //withdraw
                            String withAmount = inputNumber(input, "Enter the amount you wish to withdraw", 1, 10000);
                            boolean canWithdraw = true; //PUT FUNCTION TO TAKE WITH AMOUNT AND DETERMINE IF TRANSACTION TODO:
                            //CAN BE MADE, IF NOT RETURN FALSE;
                            if (canWithdraw) {
                                print("$" + withAmount + " successful withdrawn from account " + accountID);
                            } else {
                                print("Error: Withdraw attempt failed, insufficient funds!");
                            }
                            print("");
                        }
                        if (cusOption == 3) { //transfer
                            String TargetAccountID = digitInput(input, "Enter Target Account ID:", 3, 3);
                            boolean foundTarget = searchAccount(conn, TargetAccountID, "account");
                            ;
                            if (foundTarget) {
                                String transAmount = inputNumber(input, "Enter the amount you wish to transfer", 1, 10000);
                                boolean canTransfer = makeTransfer(conn, transAmount, accountID, TargetAccountID);
                                //also do transfer and return true or false if succesful
                                if (canTransfer) {
                                    print("$" + transAmount + " transferred from account " + accountID + " to account " + TargetAccountID);
                                    ;
                                } else {
                                    print("Error: Transfer failed, insufficient funds!");
                                }
                            } else {
                                print("Error: Target Account not found");
                            }
                            print("");
                        }
                        STATUS = "OptionPanel1";


                    } else {
                        print("Error: Account not found");
                    }
                    STATUS = "OptionPanel1";
                    continue;

                }


                if (AccType == "Cashier" && STATUS == "GetAccountTran") {
                    String accountID = digitInput(input, "Enter Account ID:", 3, 3);
                    boolean found = searchAccount(conn, accountID, "account");//PUT SEARCH FUNCTION HERE RETURN TRUE IF ACCOUNT EXISTS OR FALSE
                    if (found) {
                        String nTrans = inputNumber(input, "Enter number of previous transactions to show:", 1, 10);
                        returnTransactions(conn, accountID, nTrans);
                    } else {
                        print("Acount not found");
                    }
                    STATUS = "OptionPanel1";
                    continue;
                }


            }


            if(!ACTIVE) {
                System.out.println("Logout successfully!");
            }

        }

    }
    public static boolean yesNoOption(Scanner in, String msg) {
        boolean invalid = true;
        String val = "";
        while(invalid) {
            try {
                System.out.println(msg);
                val = in.next();
                invalid = false;
            }catch(Exception e) {
                System.out.println("Invalid input");
            }
            in.nextLine();
            if(val.equals("y")) { //we use equals to compare strings in functions
                return true;
            }
            if(val.equals("n")) {
                return false;
            }
            print("Invalid input");
            invalid = false;

        }

        return false;
    }
    public static String inputNumber(Scanner in, String msg,int min,int max) {
        //in.hasNextLine();
        boolean invalid = true;
        int num = 0;
        while(invalid) {
            try {
                System.out.println(msg);
                num = in.nextInt();
                invalid = false;
            }catch(Exception e) {
                System.out.println("Invalid input");
                invalid = true;
            }

            if(num<min || num>max) {
                invalid = true;
                System.out.println("Invalid input");
            }

        }
        in.nextLine();
        return Integer.toString(num);
    }
    //classifies spaces as special characters
    public static String inputString(Scanner in, String msg,int min,int max, boolean alphanum,boolean specialchar) {

        String val = "";
        boolean invalid = true;
        while(invalid) {
            try {

                System.out.println(msg);
                val = in.nextLine();

                invalid = false;
            }catch(Exception e) {

                System.out.println("Invalid input");
                invalid = true;
            }
            if(alphanum==false && containsNum(val)) {
                invalid = true;
                System.out.println("Invalid input");
            }


            if(specialchar==false && containsSpec(val)) {
                invalid = true;
                System.out.println("Invalid input");
            }

            if(val.length()>max || val.length()<min) {
                invalid = true;
                System.out.println("Invalid input");
            }
        }

        return val;
    }
    public static boolean containsNum(String val) {
        char[] chars = val.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
    public static int inputOption(Scanner in, String msg,int min, int max) {
        int test = 0;
        boolean invalid = true;
        while(invalid) {
            try {
                System.out.println(msg);
                test = in.nextInt();
                invalid = false;
            }catch(Exception e) {
                System.out.println("Invalid input");
            }
            if(test>max || test<min) {
                System.out.println("Invalid input");
                invalid = true;
            }

        }
        in.nextLine();
        return test;

    }

    public static void showOptions(String user) {
        if(user == "Customer") {
            System.out.println("1. Create, Update, Delete Customer");
            System.out.println("2. Create and Delete Account");
            System.out.println("3. View Customer and Account Status");
            print("4. Sign Out");
        }
        if(user == "Cashier") {
            System.out.println("1. Get Customer and Account details");
            System.out.println("2. Manage deposit, Withdraw and Transfer");
            System.out.println("3. Get Customer-Account Transactions/Get Statement");
            print("4. Sign Out");
        }

    }
    public static void print(String msg) {
        System.out.println(msg);
    }
    public static boolean containsSpec(String val) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(val);
        return matcher.find();
    }
    public static String digitInput(Scanner in,String msg,int min,int max) {
        boolean invalid = true;
        String num = "";

        while(invalid) {
            print(msg);
            num = in.nextLine();
            invalid = false;
            if(num.length()>max || num.length()<min) {
                invalid = true;
                print("Invalid input length");

            }
            String test = num.toString();
            if (!test.matches("[0-9]+")) {
                invalid = true;
                print("Invalid input");
            }
            if(containsSpec(num)) {

                invalid = true;
                print("Invalid input special characters detected");
            }


        }
        return num;
    }

    public static boolean makeTransfer(Connection conn, String exchangeNum, String sourceID, String targetID) throws SQLException {
        int amount = Integer.parseInt(exchangeNum);
        int source_id = Integer.parseInt(sourceID);
        int target_id = Integer.parseInt(targetID);
        Date date = new Date(System.currentTimeMillis());
        int newID = 0;
        int oldSourceBalance = 0;
        int oldTargetBalance = 0;

        //Check if transfer possible and get oldBalance of accounts
        String query2 = "select accountid, balance from accountstatus where accountid=" + source_id + "or accountid=" + target_id;
        PreparedStatement pst2 = conn.prepareStatement(query2);
        ResultSet rs2 = pst2.executeQuery();

        while (rs2.next()) {
            if (rs2.getInt(1) == source_id) {
                oldSourceBalance = rs2.getInt(2);
                if (oldSourceBalance < amount) {
                    //System.out.println("Transfer not allowed, please choose smaller amount");
                    return false;
                }
            } else {
                oldTargetBalance = rs2.getInt(2);
            }
        }

        //Make updates to account balances in db
        int newSourceBalance = oldSourceBalance - amount;
        int newTargetBalance = oldTargetBalance + amount;

        String updateQuery = "update accountstatus set balance=" + newSourceBalance + " where accountid=" + source_id;
        Statement st = conn.createStatement();
        int cnt = st.executeUpdate(updateQuery);

        String updateQuery2 = "update accountstatus set balance=" + newTargetBalance + " where accountid=" + target_id;
        Statement st2 = conn.createStatement();
        int cnt2 = st2.executeUpdate(updateQuery2);

        //Create new transaction in db
        String query = "select max(transactionID) from transactions";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            newID = rs.getInt(1) + 1;
        }

        String insertQuery = "insert into transactions (transactionid, account_from, account_to, transaction_day, amount) values "
                + "(" + newID + ", " + source_id + ", " + target_id + ", TO_Date('" + date + "', 'YYYY-MM-DD'), " + amount +")";
        //System.out.println(insertQuery);
        Statement st3 = conn.createStatement();
        int cnt3 = st3.executeUpdate(insertQuery);

        System.out.println("");
        System.out.println("| Source Acc.: " + source_id + " | Target Acc.: " + target_id + " | \n| Old Source Acc. Balance: " + oldSourceBalance +
                " | Old Target Acc. Balance:" + oldTargetBalance + " | \n| New Source Acc. Balance: " + newSourceBalance + " | New Target Acc. Balance: " + newTargetBalance + " |");

        return true;
    }

    public static void returnTransactions(Connection conn, String acc_id, String n) throws SQLException {
        int id = Integer.parseInt(acc_id);
        int num = Integer.parseInt(n);

        String query = "select * from transactions where rownum <= " + num + " and (account_to=" + id + " or account_from=" + id + ") order by transaction_day desc";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();


        String header = "|  Date  | Messgae | Type | Amount |";
        System.out.println("-------------------------------------------------");
        System.out.println(header);
        System.out.println("-------------------------------------------------");

        while (rs.next()) {
            int from = rs.getInt("account_from");
            int to = rs.getInt("account_to");
            int amount = rs.getInt("amount");
            Date transaction_date = rs.getDate("transaction_day");
            String message;
            String type;

            if (from == 0) {
                message = "Deposit";
                type = "Debit";
            } else if (to == 0) {
                message = "Withdrawal";
                type = "Credit";
            } else {
                if (from == id) {
                    message = "Transfer to Account " + to;
                    type = "Credit";
                } else {
                    message = "Transfer from Account " + from;
                    type = "Debit";
                }
            }
            System.out.println(" | " + transaction_date + " | " + message + " | " + type + " | $" + amount + " | ");
        }

        System.out.println("");
        return;
    }

    public static boolean searchCustomer(Connection conn, String inputID, String type) throws SQLException {
        boolean found = false;

        String query = "";
        int id = Integer.parseInt(inputID);
        if (type.equals("ssn")) {
            query = "select ssnid, customerid, fullname, address, age " +
                    "from customerstatus " +
                    "where ssnid = " + id;
        } else if (type.equals("customer")) {
            query = "select ssnid, customerid, fullname, address, age " +
                    "from customerstatus " +
                    "where customerid = " + id;
        }
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        String header = " |  SSN ID  | Customer ID | Customer Name | Address | Age |";
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println(header);
        System.out.println("------------------------------------------------------------------------------------");
        while (rs.next()) {
            found = true;
            System.out.print(" | " + rs.getInt("ssnID"));
            System.out.print(" | " + rs.getInt("customerID"));
            System.out.print(" | " + rs.getString("fullname"));
            System.out.print(" | " + rs.getString("address"));
            System.out.print(" | " + rs.getString("age"));
            System.out.println(" | ");
            System.out.println();
        }

        return found;
    }

    public static boolean searchAccount(Connection conn, String inputID, String type) throws SQLException {
        boolean found = false;

        String query = "";
        int id = Integer.parseInt(inputID);
        if (type.equals("account")) {
            query = "select customerid, accountid, accounttype, balance " +
                    "from accountstatus " +
                    "where accountid = " + id;
//            System.out.println(query);
        } else if (type.equals("customer")) {
            query = "select customerid, accountid, accounttype, balance " +
                    "from accountstatus " +
                    "where customerid = " + id;
//            System.out.println(query);
        }
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        String header = " | Customer ID | Account ID | Account Type | Balance |";
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println(header);
        System.out.println("------------------------------------------------------------------------------------");
        while (rs.next()) {
            found = true;
            System.out.print(" | " + rs.getInt("customerid"));
            System.out.print(" | " + rs.getInt("accountid"));
            System.out.print(" | " + rs.getString("accounttype"));
            System.out.print(" | " + rs.getString("balance"));
            System.out.println();
        }

        return found;
    }
}
