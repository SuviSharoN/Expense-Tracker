package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PieChart extends JPanel {

    public PieChart(String email) {
        try {
            Connection con = DBConnection.getConnection();
            // SQL query with JOIN to get data based on the email
            String query = "SELECT ue.category, ue.amount_spent " +
                    "FROM user_expenses ue " +
                    "JOIN users u ON ue.user_id = u.id " +
                    "WHERE u.email = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);  // Set the email parameter in the query
            ResultSet result = stmt.executeQuery();

            // Create the pie chart dataset dynamically
            DefaultPieDataset dataset = new DefaultPieDataset();
            while (result.next()) {
                String category = result.getString("category");
                double amountSpent = result.getDouble("amount_spent");
                dataset.setValue(category, amountSpent);
            }

            // Create a pie chart using the dataset
            JFreeChart chart = ChartFactory.createPieChart(
                    "Your Expenses",   // Chart title
                    dataset,           // Dataset
                    true,              // Include legend
                    true,              // Include tooltips
                    false              // No URLs
            );

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));  // Smaller font for labels
            plot.setCircular(true);  // Ensure the chart is circular
            plot.setNoDataMessage("No data available");  // Handle empty dataset gracefully

            // Create a chart panel with smaller dimensions
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(300, 300));  // Set reduced size for the chart
            chartPanel.setMinimumSize(new Dimension(300, 300));  // Ensure minimum size of the chart
            chartPanel.setMaximumSize(new Dimension(450, 300));  // Ensure maximum size is fixed as well
            chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));  // Optional padding

            // Set the layout for the panel
            setLayout(new BorderLayout());
            add(chartPanel, BorderLayout.CENTER);  // Add the chart panel to the center

            // Set the size of the container (optional, if needed for parent resizing)
            setPreferredSize(new Dimension(320, 320));  // Ensure container also has a fixed size
            revalidate();  // Revalidate the panel to reflect the new size
            repaint();  // Repaint the panel


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
