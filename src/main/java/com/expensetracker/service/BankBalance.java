package com.expensetracker.service;

import com.expensetracker.Database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankBalance extends JPanel {
    private double bank_balance;

    public BankBalance(String email) throws SQLException {
        // Fetch bank balance from the database
        Connection con = DBConnection.getConnection();
        String sql = "SELECT bank_balance FROM users WHERE email = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            bank_balance = rs.getDouble("bank_balance");
        }

        // Configure the main panel
        setPreferredSize(new Dimension(250, 200));  // Adjust size as needed
        setLayout(new GridBagLayout());  // Use GridBagLayout for centering
        setBackground(Color.LIGHT_GRAY);

        // Create the outer box panel
        JPanel outerBox = new JPanel();
        outerBox.setPreferredSize(new Dimension(200, 150));  // Inner box size
        outerBox.setLayout(new BoxLayout(outerBox, BoxLayout.Y_AXIS));  // BoxLayout for vertical alignment
        outerBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Border around the box
        outerBox.setBackground(Color.WHITE);

        // Add heading
        JLabel headingLabel = new JLabel("Bank Balance");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(Color.BLACK);
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center alignment
        outerBox.add(headingLabel);

        // Add balance display
        JLabel balanceLabel = new JLabel(String.valueOf(bank_balance));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balanceLabel.setForeground(Color.GREEN);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center alignment
        outerBox.add(Box.createRigidArea(new Dimension(0, 10)));  // Add spacing
        outerBox.add(balanceLabel);

        // Add button
        JButton newButton = new JButton("Enter Amount");
        newButton.setBackground(Color.GRAY);
        newButton.setForeground(Color.WHITE);
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center alignment
        newButton.setPreferredSize(new Dimension(150, 40));  // Button size
        outerBox.add(Box.createRigidArea(new Dimension(0, 10)));  // Add spacing
        outerBox.add(newButton);

        // Add the outer box to the main panel
        add(outerBox);

        // ActionListener for the button
        newButton.addActionListener(e -> {
            // Perform the action when the button is clicked (e.g., open a dialog to enter amount)
            String input = JOptionPane.showInputDialog(this, "Enter Amount:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    // Here, you can update the bank balance in the database
                    updateBankBalance(amount, email);
                    // Update the display label after modifying the bank balance
                    balanceLabel.setText(String.valueOf(bank_balance)); // Update balance
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateBankBalance(double amount, String email) throws SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE users SET bank_balance = bank_balance + ? WHERE email = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setDouble(1, amount);
        stmt.setString(2, email);
        stmt.executeUpdate();

        // Update the local bank balance variable
        bank_balance += amount;
    }

}
