package com.expensetracker.UI;

import com.expensetracker.service.InsertUser;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Register extends JPanel {

    // Declare components as instance variables
    private JLabel titleLabel, nameLabel, professionLabel, walletBalanceLabel, bankBalanceLabel, otherProfessionLabel;
    private JTextField nameField, walletBalanceField, bankBalanceField, otherProfessionField;
    private JComboBox<String> professionComboBox;
    private JButton submitButton, resetButton, nextButton;

    public Register(String email, String pass, CardLayout cardLayout,JPanel cardPanel) {
        // Set layout for the panel
        setLayout(new BorderLayout());
        setBackground(Color.CYAN); // Background color for the main panel

        //DASHBOARD PANEL




        // Create the content panel (center section)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setPreferredSize(new Dimension(300, 300)); // Set preferred size
        contentPanel.setBackground(Color.WHITE); // Background color for content panel
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Optional border

        // GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components

        // Initialize components
        titleLabel = new JLabel("Enter Your Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        nameLabel = new JLabel("Name: ");
        professionLabel = new JLabel("Profession: ");
        walletBalanceLabel = new JLabel("Wallet Balance: ");
        bankBalanceLabel = new JLabel("Bank Balance: ");
        otherProfessionLabel = new JLabel("Specify Profession: ");

        nameLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        professionLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        walletBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        bankBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        otherProfessionLabel.setFont(new Font("Arial", Font.PLAIN, 22));

        nameField = new JTextField(20);
        walletBalanceField = new JTextField(20);
        bankBalanceField = new JTextField(20);
        otherProfessionField = new JTextField(20);

        nameField.setFont(new Font("Arial", Font.PLAIN, 22));
        walletBalanceField.setFont(new Font("Arial", Font.PLAIN, 22));
        bankBalanceField.setFont(new Font("Arial", Font.PLAIN, 22));
        otherProfessionField.setFont(new Font("Arial", Font.PLAIN, 22));

        String[] professions = {"Student", "Professor", "Engineer", "Doctor", "Other"};
        professionComboBox = new JComboBox<>(professions);
        professionComboBox.setFont(new Font("Arial", Font.PLAIN, 22));

        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");

        submitButton.setFont(new Font("Arial", Font.PLAIN, 22));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 22));

        // Add components to the content panel
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        contentPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(professionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        contentPanel.add(professionComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(walletBalanceLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        contentPanel.add(walletBalanceField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(bankBalanceLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        contentPanel.add(bankBalanceField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        contentPanel.add(otherProfessionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        contentPanel.add(otherProfessionField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        contentPanel.add(resetButton, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        contentPanel.add(submitButton, gbc);

        // Add content panel to the main panel
        add(contentPanel, BorderLayout.CENTER);

        // Initially hide "Other" profession fields
        otherProfessionLabel.setVisible(false);
        otherProfessionField.setVisible(false);

        // Action listener for the profession combo box
        professionComboBox.addActionListener(e -> {
            if (professionComboBox.getSelectedItem().equals("Other")) {
                otherProfessionLabel.setVisible(true);
                otherProfessionField.setVisible(true);
            } else {
                otherProfessionLabel.setVisible(false);
                otherProfessionField.setVisible(false);
            }
        });

        // Action listener for submit button
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String profession = (String) professionComboBox.getSelectedItem();
            if ("Other".equals(profession)) {
                profession = otherProfessionField.getText();
            }
            String walletBalance = walletBalanceField.getText();
            String bankBalance = bankBalanceField.getText();

            if (name.isEmpty() || profession.isEmpty() || walletBalance.isEmpty() || bankBalance.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all details.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Successfully Registered!");
                try {
                    new InsertUser(email, pass, name, profession, walletBalance, bankBalance);
                    // Switch to the Dashboard after successful registration
                    JPanel dashboardpanel = new Dashboard(cardLayout,cardPanel,email);
                    cardPanel.add(dashboardpanel,"Dashboard");
                    cardLayout.show(cardPanel, "Dashboard"); // Assuming "Dashboard" is the card name for the Dashboard panel
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for reset button
        resetButton.addActionListener(e -> {
            nameField.setText("");
            walletBalanceField.setText("");
            bankBalanceField.setText("");
            otherProfessionField.setText("");
            professionComboBox.setSelectedIndex(0);
            otherProfessionLabel.setVisible(false);
            otherProfessionField.setVisible(false);
        });
    }
}
