package com.expensetracker.UI;

import javax.swing.*;
import java.awt.*;

public class BalanceDisplayPanel extends JPanel {

    private JLabel accountBalanceLabel;
    private JLabel walletBalanceLabel;
    private JLabel accountBalanceValue;
    private JLabel walletBalanceValue;

    public BalanceDisplayPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create labels for Account Balance and Wallet Balance
        accountBalanceLabel = new JLabel("Account Balance: ");
        walletBalanceLabel = new JLabel("Wallet Balance: ");

        // Create labels to display values of Account Balance and Wallet Balance
        accountBalanceValue = new JLabel("$0.00");
        walletBalanceValue = new JLabel("$0.00");

        // Set fonts for the labels
        accountBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        walletBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        accountBalanceValue.setFont(new Font("Arial", Font.BOLD, 20));
        walletBalanceValue.setFont(new Font("Arial", Font.BOLD, 20));

        // Align the labels to the left
        accountBalanceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        walletBalanceLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create a panel for the account balance (left part)
        JPanel accountBalancePanel = new JPanel();
        accountBalancePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        accountBalancePanel.setBackground(Color.WHITE);
        accountBalancePanel.add(accountBalanceLabel);
        accountBalancePanel.add(accountBalanceValue);

        // Create a panel for the wallet balance (right part)
        JPanel walletBalancePanel = new JPanel();
        walletBalancePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        walletBalancePanel.setBackground(Color.WHITE);
        walletBalancePanel.add(walletBalanceLabel);
        walletBalancePanel.add(walletBalanceValue);

        // Create a separator line in the middle
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 50)); // Adjust the height of the line

        // Create a container panel to hold both account and wallet balance panels with a separator
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(accountBalancePanel, BorderLayout.WEST);  // Add Account Balance on the left
        centerPanel.add(separator, BorderLayout.CENTER);  // Add separator
        centerPanel.add(walletBalancePanel, BorderLayout.EAST);  // Add Wallet Balance on the right

        // Add the center panel to the main panel
        add(centerPanel, BorderLayout.CENTER);
    }

    // Method to update the account balance
    public void setAccountBalance(double balance) {
        accountBalanceValue.setText("$" + String.format("%.2f", balance));
    }

    // Method to update the wallet balance
    public void setWalletBalance(double balance) {
        walletBalanceValue.setText("$" + String.format("%.2f", balance));
    }

    // Main method to run the GUI and test the balance display
    public static void main(String[] args) {
        // Create the frame to hold the panel
        JFrame frame = new JFrame("Balance Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Create an instance of BalanceDisplayPanel
        BalanceDisplayPanel balancePanel = new BalanceDisplayPanel();

        // Add the balance panel to the frame
        frame.add(balancePanel);

        // Display the frame
        frame.setVisible(true);

        // Set sample account and wallet balances
        balancePanel.setAccountBalance(1200.50);
        balancePanel.setWalletBalance(300.75);
    }
}
