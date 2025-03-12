package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import com.expensetracker.service.Alert;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalsPage extends JPanel {
    private JTextField currentAmountField;

    // Constructor to set up the goal panel
    public GoalsPage(String email) {
        // Set the layout for the panel (using GridBagLayout)
        JPanel forTwo = new JPanel();
        JPanel spacer = new JPanel();

        spacer.setPreferredSize(new Dimension(35, 35));
        spacer.setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        forTwo.setLayout(new FlowLayout(FlowLayout.LEFT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setSize(400, 400);
        forTwo.setBackground(Color.WHITE);
        setBackground(Color.WHITE);
        forTwo.setSize(400, 400);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50);  // Set padding for the components
        setSize(50, 50);

        // Create the labels, text fields, and button
        JLabel currentAmount = new JLabel("Current Goal: ");
        currentAmount.setFont(new Font("Arial", Font.BOLD, 20));
        currentAmountField = new JTextField(15);  // Set columns to control width
        currentAmountField.setEditable(false);  // Disable editing the current amount field

        currentAmount.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 20));

        JButton modifyButton = new JButton("Modify Goal Amount");
        modifyButton.setAlignmentY(50f);
        modifyButton.setMargin(new Insets(50, 20, 50, 20));

        // Fetch and set initial amount from the database
        fetchAndSetInitialAmount(email);

        forTwo.add(currentAmount);
        forTwo.add(currentAmountField);
        forTwo.setVisible(true);

        add(forTwo);
        add(modifyButton);

        // Set constraints for modify button and add it
        gbc.gridx = 0; // Positioning column
        gbc.gridy = 3; // Positioning row
        gbc.gridwidth = 2;  // Span across 2 columns
        gbc.fill = GridBagConstraints.NONE;  // No fill
        gbc.anchor = GridBagConstraints.CENTER; // Align button to center
        add(modifyButton, gbc);
        gbc.gridy = 4;
        add(spacer, gbc);

            // Action listener for the modify button (opens a dialog for the user to modify the amount)
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show an input dialog where the user can enter a new amount
                String newAmount = JOptionPane.showInputDialog(null, "Enter New Amount", "Modify Amount", JOptionPane.PLAIN_MESSAGE);

                if (newAmount != null && !newAmount.isEmpty()) {
                    try {
                        // Update the current amount field with the entered value
                        currentAmountField.setText(newAmount);
                        // Update the goal amount in the database
                        updateGoalAmountInDatabase(email, Double.parseDouble(newAmount));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(GoalsPage.this, "Please enter a valid numeric amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Make the button round by using setBorder and setBackground
        modifyButton.setBorder(new RoundedBorder(25));  // 30px radius for rounded corners
        modifyButton.setBackground(Color.WHITE);
        modifyButton.setForeground(Color.BLACK); // Make the content area transparent
        modifyButton.setFocusPainted(false);  // Remove focus highlight
    }

    // Fetch initial goal amount from the database and set it in the text field
    private void fetchAndSetInitialAmount(String email) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT amount_goal FROM users WHERE email = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, email);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    double initialAmount = rs.getDouble("amount_goal");
                    currentAmountField.setText(String.valueOf(initialAmount));
                } else {
                    // If no goal is set for this email, set default value
                    currentAmountField.setText("0");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching goal amount from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update the goal amount in the database
    private void updateGoalAmountInDatabase(String email, double newAmount) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {

//            Alert alert = new Alert();
//             if(alert.Alertcheck(email) != 1) {}
                 String query = "UPDATE users SET amount_goal = ? WHERE email = ?";
                 try (PreparedStatement pst = con.prepareStatement(query)) {
                     pst.setDouble(1, newAmount);
                     pst.setString(2, email);
                     pst.executeUpdate();
                 } catch (SQLException e) {
                     throw new RuntimeException(e);
                 }
             }

//             else {
//                 JOptionPane.showMessageDialog(null,"Enter Goal Amount greater than the spent value");
//             }


    }


    // Custom border to make the button rounded
    private static class RoundedBorder extends AbstractBorder {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);  // Padding for the rounded border
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Smooth out the border
            g2d.setColor(c.getForeground());  // Set the border color
            g2d.drawRoundRect(x, y, width - 2, height - 2, radius, radius);  // Draw rounded rectangle
        }
    }

//    public static void main(String[] args) {
//        // Test the GoalsPage in a JFrame
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFrame frame = new JFrame("Goals Page");
//
//                // Set up the frame size and default close operation
//                frame.setSize(400, 300);
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//                // Add the GoalsPage to the frame
//                GoalsPage goalsPage = new GoalsPage("your_email@example.com");
//                frame.getContentPane().add(goalsPage);
//
//                // Make the window visible
//                frame.setVisible(true);
//            }
//        });
//    }
}
