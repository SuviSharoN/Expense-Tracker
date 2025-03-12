package com.expensetracker.UI;

import com.expensetracker.service.Authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JPanel {
    JLabel titleLabel, userLabel, passLabel;
    JTextField userText;
    JPasswordField passText;
    JButton loginButton, resetButton;

    public LoginForm(CardLayout cardLayout ,JPanel cardPanel) {
        // Set the layout for the panel
        setLayout(new BorderLayout());

        // Create the main panel with blue background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(204, 255, 255)); // Light Blue Background
        mainPanel.setLayout(new GridBagLayout());

        // Create content panel with white background
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());

        // Create labels, text fields, and buttons
        titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        userLabel = new JLabel("Email:");
        passLabel = new JLabel("Password:");
        userLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        passLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));

        userText = new JTextField();
        userText.setColumns(25);
        userText.setFont(new Font("Sans-Serif", Font.PLAIN, 16));

        passText = new JPasswordField();
        passText.setColumns(25);
        passText.setFont(new Font("Sans-Serif", Font.PLAIN, 16));
        passText.setEchoChar('*');

        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        JButton backbutton = new JButton("Back");
        loginButton.setBackground(new Color(36, 160, 237));
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(150, 40));

        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        backbutton.setFont(new Font("Arial", Font.BOLD, 18));
        // Layout setup for the content panel
        // Layout setup for the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

// Add components to the content panel
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        contentPanel.add(passText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        contentPanel.add(resetButton, gbc);

// Update position for the backButton to avoid overlap
        gbc.gridx = 0;
        gbc.gridy = 4; // Place the Back button in a new row
        contentPanel.add(backbutton, gbc);

// Add content panel to the main panel
        mainPanel.add(contentPanel);

// Add the main panel to the current panel
        add(mainPanel, BorderLayout.CENTER);

// Back button action listener
        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Sign In");
            }
        });

        // Login button functionality
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = userText.getText();
                String password = new String(passText.getPassword());

                // Create an instance of Authentication service
                Authentication authentication = new Authentication();
                String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
                // Authenticate the user
                if (authentication.authenticateUser(email, password)) {
                    JOptionPane.showMessageDialog(null, "Log In Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    try{

                        JPanel dashboardpanel = new Dashboard(cardLayout,cardPanel,email);
                        cardPanel.add(dashboardpanel,"Dashboard");

                        cardLayout.show(cardPanel, "Dashboard");


                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }

                }
                else if (!email.matches(emailRegex)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Invalid Email", JOptionPane.WARNING_MESSAGE);

                }else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Reset button functionality
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userText.setText("");
                passText.setText("");
            }
        });
    }
}
