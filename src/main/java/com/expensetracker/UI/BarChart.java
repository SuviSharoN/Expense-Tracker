package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.*;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
class BarChart extends JPanel{
    private static final String URL = "jdbc:mysql://localhost:3306/expensetracker";

    public BarChart(String email) {
        // Create dataset
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            Connection con = DBConnection.getConnection();


            String query = "SELECT ue.category, ue.amount_spent,ue.budget " +
                    "FROM user_expenses ue " +
                    "JOIN users u ON ue.user_id = u.id " +
                    "WHERE u.email = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);  // Set the email parameter in the query
            ResultSet result1 = stmt.executeQuery();
            double goal = -1;
            double spent = -1;
            String category = "";
            while (result1.next()) {
                goal = result1.getDouble("budget");
                spent = result1.getDouble("amount_spent");
                category = result1.getString("category");
                dataset.addValue(goal, "Goal", category);
                dataset.addValue(spent, "Spent", category);
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Goal Analysis",
                    "Categories",
                    "Amount Spent",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            // Customize the renderer
            CategoryPlot plot = barChart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.PINK);
            renderer.setSeriesPaint(1, Color.BLUE);// First series (Sales) - Red
            setSize(400,500);

            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            setLayout(new BorderLayout());
            add(chartPanel, BorderLayout.CENTER);
            setPreferredSize(new Dimension(320, 320));  // Ensure container also has a fixed size
            revalidate();  // Revalidate the panel to reflect the new size
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}