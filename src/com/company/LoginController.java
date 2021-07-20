package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class LoginController {
    static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static String user = "system";
    static String password = "chenqq227";

    static Connection createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            //System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
        return null;


    }
    // Takes a username and password, and checks them against the database. Returns role tpe on success, or "Login failed"
    static String checkLogin(Connection conn, String username, String password) {
        String loginQuery = "SELECT accounttype FROM userstore WHERE userlogin = ? AND userpw = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(loginQuery);
            pst.setString(1, username);
            pst.setString(2, password);
            String result = "";
            ResultSet rs=pst.executeQuery();
            if (rs.next()) {
                String temp =  rs.getString(1); // This should be the correct account type
                boolean timeChanged = updateTime(conn, username); // Now, update the login timestamp
                // Sanity checks
                System.out.println("Login Successful, user type: " + temp);
                System.out.println("Login time updated: " + timeChanged);

                switch(temp) {
                    case "Executive":
                        result = "Customer";
                        break;
                    case "Cashier":
                        result = "Cashier";
                        break;
                    case "Teller":
                        result = "Cashier";
                        break;
                    default:
                        result = "";
                        break;
                }

                return result;
            }
        } catch (Exception e) {
            //System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
        return "";
    }

    // updates timestamp on successful login. returns true if successful, false otherwise
    public static boolean updateTime(Connection conn, String username) {
        try {
            /* Test update
            String checkBefore = "SELECT logintimestamp FROM userstore WHERE userlogin = ?";
            PreparedStatement pst1 = conn.prepareStatement(checkBefore);
            pst1.setString(1, username);
            ResultSet rs1 = pst1.executeQuery();

            while(rs1.next()) {
                System.out.println(rs1.getDate(1));
            }
             */

            String timeQuery = "UPDATE userstore SET logintimestamp = SYSDATE WHERE userlogin = ?";
            PreparedStatement pst2 = conn.prepareStatement(timeQuery);
            pst2.setString(1, username);
            pst2.executeUpdate();

            return true;

            /* Test update
            String checkAfter = "SELECT logintimestamp FROM userstore WHERE userlogin = ?";
            PreparedStatement pst3 = conn.prepareStatement(checkBefore);
            pst3.setString(1, username);
            ResultSet rs3 = pst3.executeQuery();

            while(rs3.next()) {
                System.out.println(rs3.getDate(1));
            }
             */
        } catch (Exception e) {
            //System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
        return false;
    }
}
