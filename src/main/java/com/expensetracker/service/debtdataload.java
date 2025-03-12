package com.expensetracker.service;

import com.expensetracker.Database.DBConnection;
import com.expensetracker.UI.Debt;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class debtdataload extends Component {
    int userid = -1;
    String adminemail;
    public debtdataload(ArrayList debtList,String email) {
        this.adminemail = email;
        try (Connection con = DBConnection.getConnection())
        {
            String sql = "SELECT id FROM users WHERE email = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, adminemail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userid = rs.getInt("id");
            }
            String sql1 = "SELECT * FROM debt WHERE user_id = ?";
            PreparedStatement st1 = con.prepareStatement(sql1);
            st1.setString(1,String.valueOf(userid));
            ResultSet rs1 = st1.executeQuery();
            while (rs1.next()) {
                String name = rs1.getString("name");
                String email1 = rs1.getString("email");
                String amountDue = rs1.getString("amount_due");
                String dueDate = rs1.getString("due_date");
                String status = rs1.getString("status");

                debtList.add(new Debt(name, email1, amountDue, dueDate, status,adminemail));
            }

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(, "Error loading debts from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
