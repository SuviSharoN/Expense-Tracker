package com.expensetracker.service;

import com.expensetracker.Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    public String getUserName(String email) {
        String userName = null;
        try {
            // Assuming you have a connection to your database
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT name FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userName = rs.getString("name");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public boolean authenticateUser(String email, String password) {
        boolean isAuthenticated = false;
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            // Check if the user exists with matching credentials
            if (resultSet.next()) {
                isAuthenticated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed after the operation
            DBConnection.closeConnection(connection);
        }
        return isAuthenticated;
    }
}
