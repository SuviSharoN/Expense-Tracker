package com.expensetracker.UI;

import com.expensetracker.service.InsertUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SignInForm extends JFrame {
    // Declare components for the Sign In page
    JLabel titleLabel, userLabel, passLabel;
    JTextField userText;
    JPasswordField passText;
    JButton signInButton, goToLoginButton;

    // CardLayout container
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public SignInForm() {
        // Set the title of the window
        setTitle("Sign In Page");

        // Create a CardLayout and panel container for cards
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //ALL THE PANELS CREATED HERE
        {

            JPanel signInPanel = createSignInPanel();
            // Register page
            JPanel loginPanel = new LoginForm(cardLayout, cardPanel);// Login page (replace with your actual Login page)

            // Add cards (panels) to the cardPanel
            cardPanel.add(signInPanel, "Sign In");
            cardPanel.add(loginPanel, "Login");


        }


        // Add the card panel to the frame
        add(cardPanel, BorderLayout.CENTER);

        // Set JFrame to fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        //setUndecorated(true); // Remove title bar for true fullscreen
        setVisible(true);

        // Window settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Optional: Add ESC key functionality to exit fullscreen
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0); // Exit the application when ESC is pressed
                }
            }
        });

    }

    // Create Sign In Panel
    private JPanel createSignInPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(204, 255, 255)); // Light Blue Background
        mainPanel.setLayout(new GridBagLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());

        titleLabel = new JLabel("Sign In", JLabel.CENTER);
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

        signInButton = new JButton("Sign In");
        goToLoginButton = new JButton("Go to Login");

        signInButton.setBackground(new Color(36, 160, 237));
        signInButton.setFont(new Font("Arial", Font.BOLD, 18));
        signInButton.setPreferredSize(new Dimension(150, 40));

        goToLoginButton.setFont(new Font("Arial", Font.BOLD, 18));

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
        contentPanel.add(signInButton, gbc);

        gbc.gridx = 1;
        contentPanel.add(goToLoginButton, gbc);

        // Add content panel to the main panel
        mainPanel.add(contentPanel);

        // Add action listeners for buttons
        signInButton.addActionListener(e -> {
            String email = userText.getText();
            String password = new String(passText.getPassword());
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Invalid Email", JOptionPane.WARNING_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Sign In Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                try{
                    JPanel registerPanel = new Register(email,password,cardLayout,cardPanel);

                    cardPanel.add(registerPanel, "Register");
                    cardLayout.show(cardPanel,"Register");

                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                cardLayout.show(cardPanel,"Register");

            }
        });

        goToLoginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login")); // Switch to Login card

        return mainPanel;
    }
}