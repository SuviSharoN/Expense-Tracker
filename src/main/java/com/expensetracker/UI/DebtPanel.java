package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import com.expensetracker.service.debtdataload;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class DebtPanel extends JPanel {
    private JTable debtTable;
    private JButton markPaidButton;
    private JButton insertButton;
    private DefaultTableModel tableModel;
    private ArrayList<Debt> debtList;
    String adminemail;

    public DebtPanel(String email) {
        // Initialize the debt list
        this.adminemail = email;
        debtList = new ArrayList<>();

        // Fetch debts from the database
        new debtdataload(debtList, email);

        // Set up the panel layout
        setLayout(new BorderLayout());

        // Create column names
        String[] columnNames = {"Name", "Email", "Amount Due", "Due Date", "Status"};

        // Create the table model using data from debtList
        tableModel = new DefaultTableModel(convertDebtListToArray(), columnNames);
        debtTable = new JTable(tableModel);

        // Make the table more interactive
        debtTable.setFillsViewportHeight(true);
        debtTable.setRowHeight(30);  // Make rows taller for readability
        debtTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));  // Bold headers

        // Customizing the table columns (adjust widths)
        TableColumn column = debtTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(150);
        column = debtTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = debtTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);
        column = debtTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(debtTable);
        scrollPane.setPreferredSize(new Dimension(800, 400)); // Increase panel size
        add(scrollPane, BorderLayout.CENTER);

        // Panel for the buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Mark as Paid button
        markPaidButton = new JButton("Mark as Paid");
        markPaidButton.setBackground(new Color(85, 200, 85));  // Green button color
        markPaidButton.setForeground(Color.WHITE);
        markPaidButton.setFont(new Font("Arial", Font.BOLD, 14));
        markPaidButton.setPreferredSize(new Dimension(200, 40));
        markPaidButton.setFocusPainted(false);
        buttonPanel.add(markPaidButton);

        // Insert button
        insertButton = new JButton("Insert");
        insertButton.setBackground(new Color(85, 85, 200));  // Blue button color
        insertButton.setForeground(Color.WHITE);
        insertButton.setFont(new Font("Arial", Font.BOLD, 14));
        insertButton.setPreferredSize(new Dimension(200, 40));
        insertButton.setFocusPainted(false);
        buttonPanel.add(insertButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for the Mark as Paid button
        markPaidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and update status to "Paid"
                int selectedRow = debtTable.getSelectedRow();
                if (selectedRow != -1) { // Ensure a row is selected
                    // Update the debt object
                    Debt selectedDebt = debtList.get(selectedRow);
                    selectedDebt.setStatus("Paid");

                    // Update the table view
                    debtTable.setValueAt("Paid", selectedRow, 4);

                    // Update status in the database
                    updateDebtStatusInDatabase(selectedDebt);
                } else {
                    JOptionPane.showMessageDialog(DebtPanel.this, "Please select a debt to mark as paid.",
                            "No selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Add ActionListener for the Insert button
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a dialog to insert a new debt
                JTextField nameField = new JTextField();
                JTextField emailField = new JTextField();
                JTextField amountField = new JTextField();
                JTextField dateField = new JTextField();
                JTextField statusField = new JTextField();
                Object[] message = {
                        "Name:", nameField,
                        "Email:", emailField,
                        "Amount Due:", amountField,
                        "Due Date:", dateField,
                        "Status:", statusField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Insert New Debt", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    // Add new debt to the list and table model
                    Debt newDebt = new Debt(nameField.getText(), emailField.getText(), amountField.getText(), dateField.getText(), statusField.getText(),adminemail);
                    debtList.add(newDebt);
                    tableModel.addRow(new Object[]{newDebt.getName(), newDebt.getEmail(), newDebt.getAmountDue(), newDebt.getDueDate(), newDebt.getStatus()});

                    // Insert the new debt into the database
                    insertDebtIntoDatabase(newDebt);
                }
            }
        });

        // Customize the panel's appearance
        setBackground(new Color(240, 240, 240));  // Light gray background
    }

    // Helper method to convert debtList to a 2D array for DefaultTableModel
    private Object[][] convertDebtListToArray() {
        Object[][] data = new Object[debtList.size()][5]; // 5 columns: name, email, amount due, due date, status
        for (int i = 0; i < debtList.size(); i++) {
            Debt debt = debtList.get(i);
            data[i][0] = debt.getName();        // Name
            data[i][1] = debt.getEmail();       // Email
            data[i][2] = debt.getAmountDue();   // Amount Due
            data[i][3] = debt.getDueDate();     // Due Date
            data[i][4] = debt.getStatus();      // Status (Paid / Unpaid)
        }
        return data;
    }

    // Update debt status in the database
    public void updateDebtStatusInDatabase(Debt debt) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE debt SET status = ? WHERE email = ? AND amount_due = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, debt.getStatus());
                pst.setString(2, debt.getEmail());
                pst.setString(3, debt.getAmountDue());

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating debt status in the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Insert new debt into the database
    public void insertDebtIntoDatabase(Debt debt) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO debt (name, email, amount_due, due_date, status,user_id) VALUES (?, ?, ?, ?, ?,?)";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, debt.getName());
                pst.setString(2, debt.getEmail());
                pst.setString(3, debt.getAmountDue());
                pst.setString(4, debt.getDueDate());
                pst.setString(5, debt.getStatus());
                pst.setString(6,String.valueOf(debt.getuser_id()));

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error inserting new debt into the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Test the DebtPanel in a JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Debt Tracker");

                // Set up the frame size and default close operation
                frame.setSize(800, 600); // Increased frame size
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Add the DebtPanel to the frame
                DebtPanel debtPanel = new DebtPanel("suvisharon79@gmail.com");
                frame.getContentPane().add(debtPanel);

                // Make the window visible
                frame.setVisible(true);
            }
        });
    }
}
