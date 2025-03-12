package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Debt {
    private String name;
    private String email;
    private String amountDue;
    private String dueDate;
    private String status;
    private String adminemail;

    public Debt(String name, String email, String amountDue, String dueDate, String status,String adminemail) {
        this.adminemail = adminemail;
        this.name = name;
        this.email = email;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getuser_id() throws SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT id FROM users WHERE email = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, adminemail);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return(rs.getInt("id"));
        }
        return -1;
    }
}
