package com.expensetracker.UI;

import com.expensetracker.Database.DBConnection;
import com.expensetracker.service.BankBalance;
import com.expensetracker.service.WalletBalance;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Dashboard extends JPanel {

    String email;
    String name;
    private JPanel headerPanel, sidebarPanel;
    private JButton toggleButton;
    private boolean sidebarExpanded = false;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public Dashboard(CardLayout cardLayout_main, JPanel cardPanel, String email) throws SQLException {
        this.email = email;
        Connection con = DBConnection.getConnection();
        String sql = "SELECT name FROM users WHERE email = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1,email);
        ResultSet rs = st.executeQuery();
        if(rs.next())
        {
            name = rs.getString("name");
        }



        setLayout(new BorderLayout());
        contentPanel = new JPanel();

        // Create and configure the header panel
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(36, 160, 237));  // Blue header color
        headerPanel.setLayout(new BorderLayout());




        JLabel headerLabel = new JLabel("Expense Tracker", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Create and configure the sidebar panel (initially closed)
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(36, 160, 237));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));  // Vertical buttons
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding inside the sidebar

        // Add components to the sidebar
        JLabel welcomeLabel = new JLabel("Welcome "+this.name, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        sidebarPanel.add(welcomeLabel);

        // Sidebar buttons for navigation
        JButton summaryButton = createSidebarButton("Summary");
        sidebarPanel.add(summaryButton);

        JButton statsButton = createSidebarButton("Stats");
        sidebarPanel.add(statsButton);

        JButton debtButton = createSidebarButton("Debt");
        sidebarPanel.add(debtButton);


        // Create the content panel using CardLayout
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);  // Set the background color to white
        contentPanel.setPreferredSize(new Dimension(1200, 500)); // Set preferred size of the content panel

        // Create individual pages (Summary, Stats, Debt)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout()); // Center content in the panel
//        JPanel cat = new JPanel();
//        cat.setLayout(new BorderLayout());
        summaryPanel.setBackground(Color.WHITE);


        // Create and add the Category_Panel to the summaryPanel
        Category_Panel Categories = new Category_Panel(email);

        Categories.setPreferredSize(new Dimension(650, 800));  // Set square size
        Categories.setBorder(BorderFactory.createEmptyBorder(20, 20, 150, 30));
        Categories.setBackground(Color.WHITE);
//        cat.setSize(600,600);// Padding
//        cat.add(Categories,BorderLayout.CENTER);


        BankBalance bankpanel = new BankBalance(email);
        WalletBalance walletpanel = new WalletBalance(email);
        GoalsPage goalspanel = new GoalsPage(email);

        //summaryPanel.add(BankWalletPanel,BorderLayout.EAST);

        JPanel goalandbal = new JPanel();
        goalandbal.setLayout(new BoxLayout(goalandbal, BoxLayout.Y_AXIS));
        goalandbal.setBackground(Color.white);
        goalandbal.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        goalandbal.add(bankpanel);
        goalandbal.add(Box.createRigidArea(new Dimension(0, 20)));
        goalandbal.add(walletpanel);
        goalspanel.add(Box.createRigidArea(new Dimension(0, 20)));
        goalandbal.add(goalspanel);
        goalandbal.setVisible(true);
        summaryPanel.add(goalandbal,BorderLayout.EAST);




        //goalspanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 200, 0));
        summaryPanel.add(Categories,BorderLayout.WEST);
        summaryPanel.setBackground(Color.WHITE);

        contentPanel.setSize(400,500);

        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 30));


        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS)); // Set BoxLayout to align components vertically
        statsPanel.setBackground(Color.WHITE);


        PieChart pieChartPanel = new PieChart(email);
        pieChartPanel.setBackground(new Color(200, 200, 255));
        pieChartPanel.setPreferredSize(new Dimension(300, 300));
        pieChartPanel.setLayout(new BorderLayout());
        pieChartPanel.add(new JLabel("Pie Chart", JLabel.CENTER), BorderLayout.CENTER);
          // Add PieChart to the stats panel
        BarChart barchartpanel = new BarChart(email);
        barchartpanel.setBackground(new Color(255, 200, 200));
        barchartpanel.setPreferredSize(new Dimension(300, 300));
        barchartpanel.setLayout(new BorderLayout());
        barchartpanel.add(new JLabel("Bar Chart", JLabel.CENTER), BorderLayout.CENTER);

        statsPanel.add(pieChartPanel);
        statsPanel.add(Box.createRigidArea(new Dimension(20,10)));
        statsPanel.add(barchartpanel);






        // Debt panel
        JPanel debtPanel = new JPanel();
        debtPanel.setBackground(Color.WHITE);

        DebtPanel debts = new DebtPanel(email);
        debtPanel.add(debts);

        // Add pages to the content panel (CardLayout container)
        contentPanel.add(summaryPanel, "Summary");
        contentPanel.add(statsPanel, "Stats");
        contentPanel.add(debtPanel, "Debt");

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0,10)));

        // Image for toggle button
        ImageIcon icon1 = new ImageIcon("R:\\menu-bar.png"); // Make sure to put your icon image path here
        Image img = icon1.getImage();  // Get the image from the icon
        Image resizedImg = img.getScaledInstance(20, 30, Image.SCALE_SMOOTH);  // Resize it to the button size
        ImageIcon icon = new ImageIcon(resizedImg);  // Create a new icon with the resized image

        toggleButton = new JButton();
        toggleButton.setIcon(icon);  // Set the resized icon to the button
        toggleButton.setBackground(new Color(36, 160, 237));  // Button background
        toggleButton.setForeground(Color.WHITE);  // Button text color (won't be used, since there's no text)
        toggleButton.setBorder(BorderFactory.createEmptyBorder());
        toggleButton.setFocusPainted(false);
        // Create a panel to wrap the toggle button and add padding
        JPanel toggleButtonWrapper = new JPanel(new BorderLayout());
        toggleButtonWrapper.setBackground(new Color(36, 160, 237)); // Match the header background color
        toggleButtonWrapper.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0)); // Add padding on the left (10px)

// Add the toggle button to the wrapper panel
        toggleButtonWrapper.add(toggleButton, BorderLayout.CENTER);

// Add the wrapper panel to the header
        headerPanel.add(toggleButtonWrapper, BorderLayout.WEST);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSidebar();
            }
        });



        // Add components to the layout
        add(headerPanel, BorderLayout.NORTH);  // Header at the top
        add(sidebarPanel, BorderLayout.WEST);  // Sidebar on the left
        add(contentPanel, BorderLayout.CENTER);  // Content area takes up the remaining space

        // ActionListeners for buttons to switch between pages using CardLayout
        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Summary");
            }
        });

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When stats button is clicked, show the "Stats" card (which contains the PieChart)
                statsPanel.removeAll();
                PieChart piepanel = new PieChart(email);
                BarChart barpanel = new BarChart(email);
                statsPanel.add(piepanel);
                statsPanel.add(barpanel);
                cardLayout.show(contentPanel, "Stats");
            }
        });

        debtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Debt");
            }
        });

        revalidate();
        repaint();

        setVisible(true);
    }

    // Method to create sidebar buttons
    private JButton createSidebarButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(36, 160, 237));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(200, 40));  // Set fixed width and height for buttons
        button.setMaximumSize(new Dimension(200, 40)); // Ensure consistent sizing
        button.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button
        addHoverEffect(button);  // Add hover effect
        return button;
    }

    // Method to add hover effect for the buttons
    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(56, 186, 255)); // Change background color for glow effect
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Add white border to simulate glow
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(36, 160, 237)); // Reset background color
                button.setBorder(null); // Remove the border when not hovered
            }
        });
    }

    // Method to toggle the sidebar's visibility
    private void toggleSidebar() {
        if (sidebarExpanded) {
            sidebarPanel.setPreferredSize(new Dimension(0, getHeight() - 50));
            sidebarExpanded = false;
        } else {
            sidebarPanel.setPreferredSize(new Dimension(250, getHeight() - 50));
            sidebarExpanded = true;
        }
        revalidate();
        repaint();
    }
    public static void main(String[] args) throws SQLException {
        // Use the SwingUtilities.invokeLater method to ensure the UI is built on the Event Dispatch Thread

        // Create the JFrame to hold the Dashboard panel
        JFrame frame = new JFrame("Expense Tracker");

        // Set the size of the frame
        frame.setSize(1000, 700);

        // Set the default close operation so the application closes when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a CardLayout to switch between different pages in the content area
        CardLayout cardLayout = new CardLayout();

        // Create the JPanel to hold the pages
        JPanel cardPanel = new JPanel(cardLayout);

        // Create and add the Dashboard to the cardPanel
        Dashboard dashboard = new Dashboard(cardLayout, cardPanel, "user@example.com");
        cardPanel.add(dashboard, "Dashboard");

        // Add the cardPanel to the frame's content pane
        frame.getContentPane().add(cardPanel);

        // Make the frame visible
        frame.setVisible(true);

    }
}