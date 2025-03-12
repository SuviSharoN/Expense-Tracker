package com.expensetracker.service;

import com.expensetracker.Database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertUser {

    // Constructor for inserting email and password
    public InsertUser(String email, String Password) throws SQLException {
        Connection con = DBConnection.getConnection();

        try {
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement st1 = con.prepareStatement(query);
            st1.setString(1, email);
            st1.setString(2, Password); // Corrected to set the password, not the email

            // Execute the update
            int rowsAffected = st1.executeUpdate();
            boolean isdone = (rowsAffected > 0); // Check if insert was successful
            if (isdone) {
                System.out.println("User successfully registered.");
            } else {
                System.out.println("User registration failed.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (con != null) con.close();  // Make sure to close the connection
        }
    }

    // Constructor for inserting other user details
    public InsertUser(String email,String pass,String name, String Profession, String Walletb, String Bankb) throws SQLException {
        Connection con = DBConnection.getConnection();

        try {
            String query = "INSERT INTO users (email,password,name, profession, bank_balance, wallet_balance) VALUES (?,?,?, ?, ?, ?)";
            PreparedStatement st1 = con.prepareStatement(query);
            st1.setString(1,email);
            st1.setString(2,pass);
            st1.setString(3, name);
            st1.setString(4, Profession);
            st1.setString(5, Bankb);
            st1.setString(6, Walletb);

            // Execute the update
            int rowsAffected = st1.executeUpdate();
            boolean isdone = (rowsAffected > 0); // Check if insert was successful
            if (isdone) {
                System.out.println("User details successfully inserted.");
            } else {
                System.out.println("Failed to insert user details.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (con != null) con.close();  // Make sure to close the connection
        }
    }
}
