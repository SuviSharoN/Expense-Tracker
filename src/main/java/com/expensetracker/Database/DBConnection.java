package com.expensetracker.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String USER = "root";  // Replace with your database username
    private static final String PASSWORD = "SharoN079";  // Replace with your database password
    private static final String URL = "jdbc:mysql://localhost:3306/expensetracker";

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver (optional but recommended for older versions of Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }

        // Return the connection object
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // You can add a method to close the connection as well (optional)
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
