package com.expensetracker.service;

import com.expensetracker.Database.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Alert {
    double goal,spent,userid;
    public int Alertcheck(String email) {
        try {

        Connection con = DBConnection.getConnection();
        String sql = "SELECT amount_goal from users WHERE email = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1,email);
        ResultSet rs = st.executeQuery();
        if(rs.next())
        {
            this.goal =  rs.getDouble("amount_goal");
        }
        String sql2 = "SELECT id FROM users WHERE email = ?";
        PreparedStatement stmt = con.prepareStatement(sql2);
        stmt.setString(1, email);
        ResultSet rs2 = stmt.executeQuery();

        if (rs2.next()) {
            userid = rs2.getInt("id");
        }



        String sql3 = "SELECT amount_spent from user_expenses WHERE user_id = ?";
        PreparedStatement stmtt = con.prepareStatement(sql3);
        stmtt.setString(1, String.valueOf(userid));
        ResultSet rs3 = stmtt.executeQuery();

        while(rs3.next())
        {
            spent+= rs3.getDouble("amount_spent");
            System.out.println(spent);
        }
        System.out.println(goal);
        if(spent<goal)
            return 1;
        else return 0;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter a valid goal amount.", "Invalid Goal", JOptionPane.ERROR_MESSAGE); } catch (
            SQLException e) {
        throw new RuntimeException(e);
    }
        return 0;
    }
}
