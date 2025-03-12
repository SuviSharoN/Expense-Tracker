package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import com.expensetracker.service.Alert;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Category_Panel extends JPanel {

    private JPanel mainPanel;
    String email1;
    int userid = -1;

    public Category_Panel(String email) {
        try {
            this.email1 = email;
            setSize(100, 100);
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());

            // Main panel
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            Connection con = DBConnection.getConnection();

            //Connection conn = DBConnection.getConnection();
            String sql = "SELECT id FROM users WHERE email = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email1);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userid = rs.getInt("id");
            }
            System.out.println(userid);

            String checktable = "SELECT user_id FROM user_expenses WHERE user_id = ?";
            PreparedStatement check = con.prepareStatement(checktable);
            check.setString(1, String.valueOf(userid));
            ResultSet rscheck = check.executeQuery();



            if(!rscheck.next()){
                System.out.println("new user");
                mainPanel.add(new BudgetPanel("Entertainment", 0, 0, 300));
                mainPanel.add(new BudgetPanel("Eating out", 0, 0, 100));
                mainPanel.add(new BudgetPanel("Fuel", 0, 0, 120));

                String query1 = "insert into user_expenses(category,budget,user_id) values('Entertainment',0,?)";
                PreparedStatement stinsert = con.prepareStatement(query1);
                stinsert.setString(1, String.valueOf(userid));
                stinsert.executeUpdate();



                String query2 = "insert into user_expenses(category,budget,user_id) values('Eating out',0,?)";
                PreparedStatement stinsert2 = con.prepareStatement(query2);
                stinsert2.setString(1, String.valueOf(userid));
                stinsert2.executeUpdate();



                String query3 = "insert into user_expenses(category,budget,user_id) values('Fuel',0,?)";
                PreparedStatement stinsert3 = con.prepareStatement(query3);
                stinsert3.setString(1, String.valueOf(userid));
                stinsert3.executeUpdate();
            }




            else {
                String expenseQuery = "SELECT category, budget, amount_spent FROM user_expenses WHERE user_id = ?";
                PreparedStatement expenseStmt = con.prepareStatement(expenseQuery);
                expenseStmt.setInt(1, userid);
                ResultSet expenseRs = expenseStmt.executeQuery();
                while (expenseRs.next()) {
                String category = expenseRs.getString("category");
                double budget = expenseRs.getDouble("budget");
                double spent = expenseRs.getDouble("amount_spent");


                double percentageSpent = (budget != 0) ? (spent / budget) * 100 : 0;

                // Add BudgetPanel for each category dynamically
                mainPanel.add(new BudgetPanel(category, (int) percentageSpent, spent, budget));
            }
            }



            // Add button to add new category
            JButton addCategoryButton = new JButton("Add Category");
            addCategoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addCategoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newCategory = JOptionPane.showInputDialog("Enter new category name:");
                    if (newCategory != null && !newCategory.trim().isEmpty()) {
                        BudgetPanel newPanel = new BudgetPanel(newCategory, 0, 0, 100);
                        try (Connection con = DBConnection.getConnection()) {
                            String insertCategoryQuery = "INSERT INTO user_expenses (category, budget, amount_spent, user_id) VALUES (?, ?, ?, ?)";
                            PreparedStatement insertStmt = con.prepareStatement(insertCategoryQuery);
                            insertStmt.setString(1, newCategory); // Category name
                            insertStmt.setDouble(2, 100);         // Initial budget (default is 100 as per your code)
                            insertStmt.setDouble(3, 0);           // Initial spent (default is 0)
                            insertStmt.setInt(4, userid);         // User ID
                            insertStmt.executeUpdate();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error inserting new category into database: " + ex.getMessage());
                        }
                        mainPanel.add(newPanel, mainPanel.getComponentCount() - 1);
                        mainPanel.revalidate(); // Refresh the main panel
                        mainPanel.repaint();    // Repaint the main panel

                    }
                }
            });
            mainPanel.add(addCategoryButton);

            add(new JScrollPane(mainPanel), BorderLayout.CENTER);
       }
        catch(SQLException e)
            {
                e.printStackTrace();
            }
    }

    private class BudgetPanel extends JPanel {
        private double total;
        private double spent;
        private JLabel amountLabel;
        private JProgressBar progressBar;

        public BudgetPanel(String category, int percentage, double spent, double total) {
            this.total = total;
            this.spent = spent;

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createTitledBorder(category));

           // JLabel dateLabel = new JLabel(startDate + " - " + endDate);
            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(percentage);
            amountLabel = new JLabel("₹" + spent + " / ₹" + total);

            JButton addSpentButton = new JButton("Add Spent");
            JButton addBudgetButton = new JButton("Add Budget");

            // Panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align buttons in a single line
            buttonPanel.add(addSpentButton);
            buttonPanel.add(addBudgetButton);

            // Action listener for adding to spent amount
            // Inside the BudgetPanel class
            addSpentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Enter amount to increase spent:");
                    try {
                        double amount = Double.parseDouble(input);
                        BudgetPanel.this.spent += amount;
                        amountLabel.setText(String.format("₹%.2f / ₹%.2f", BudgetPanel.this.spent, BudgetPanel.this.total));
                        progressBar.setValue((int) ((BudgetPanel.this.spent / BudgetPanel.this.total) * 100));

                        // Update the database for spent
                        if(BudgetPanel.this.spent<BudgetPanel.this.total){
                            try (Connection con = DBConnection.getConnection()) {

                                String updateSpentQuery = "UPDATE user_expenses SET amount_spent = ? WHERE category = ? AND user_id = ?";
                                PreparedStatement updateSpentStmt = con.prepareStatement(updateSpentQuery);
                                updateSpentStmt.setDouble(1, BudgetPanel.this.spent);
                                updateSpentStmt.setString(2, ((TitledBorder) getBorder()).getTitle()); // Use category name
                                updateSpentStmt.setInt(3, userid); // Pass the user ID
                                updateSpentStmt.executeUpdate();
                                //new Alert()
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Error updating spent in database: " + ex.getMessage());
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Amount cannot be more than Budget,Please Change it");
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            });

            addBudgetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Enter amount to increase budget:");
                    try {
                        double amount = Double.parseDouble(input);
                        BudgetPanel.this.total += amount;
                        amountLabel.setText(String.format("₹%.2f / ₹%.2f", BudgetPanel.this.spent, BudgetPanel.this.total));
                        progressBar.setValue((int) ((BudgetPanel.this.spent / BudgetPanel.this.total) * 100));

                        // Update the database for budget
                        try (Connection con = DBConnection.getConnection()) {
                            String updateBudgetQuery = "UPDATE user_expenses SET budget = ? WHERE category = ? AND user_id = ?";
                            PreparedStatement updateBudgetStmt = con.prepareStatement(updateBudgetQuery);
                            updateBudgetStmt.setDouble(1, BudgetPanel.this.total);
                            updateBudgetStmt.setString(2, ((TitledBorder) getBorder()).getTitle()); // Use category name
                            updateBudgetStmt.setInt(3, userid); // Pass the user ID
                            updateBudgetStmt.executeUpdate();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error updating budget in database: " + ex.getMessage());
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                    }
                }
            });


            // add(dateLabel);
            add(progressBar);
            add(amountLabel);
            add(buttonPanel); // Add the button panel with both buttons in a single line
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            Category_Panel tracker = new Category_Panel();
//            tracker.setVisible(true);
//        });
//    }
}