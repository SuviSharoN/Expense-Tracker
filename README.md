EXPENSE TRACKER

TEAMNO 5:
Suvi Sharon - 2023103574
Dilshan Chinnappan - 2023103505
Saravana Kumar – 2023103559
Thulasi Sri - 2023103508

DESCRIPTION:
The Expense Tracker is a desktop-based Java application designed to help users efficiently manage and monitor their financial activities. Built using Swing for the frontend and JDBC for backend database interactions, this application provides users with tools to log expenses, set financial goals, and track savings.
NO OF CLASSES:19
TECHNOLOGIES:
•	Frontend: Swing and AWT for creating a user-friendly graphical interface.
•	Backend: Java with JDBC for database connectivity.
•	Database: MySQL for secure and reliable data storage and retrieval.


FRONTEND DESCRIPTION:
The frontend of the Expense Tracker application is developed using Swing, a Java-based GUI framework. It provides an intuitive and visually appealing interface for managing financial activities.
Key Features:
•	Login Page: A secure authentication form with JTextField for username, JPasswordField for password, and JButton for login.
•	Dashboard: Displays wallet balance, bank balance, and financial goals using labels and tables for an organized presentation.
•	Expense Logging: Includes forms with fields like JTextField, JComboBox for categories, and JButton for saving expenses.
•	Analytics View: Displays spending data with calculated percentages and integrates JTable for tabular data presentation.
•	Savings Goals: Provides an interface to set and track weekly savings goals.

BACKEND DESCRIPTION:
The backend of the Expense Tracker application is developed using JDBC for database connectivity and standard Java for business logic. It handles data processing, storage, and retrieval to support the application's core functionality.
Key Features:
1.	Database Connectivity:
o	Utilizes JDBC to connect to a MySQL database.
o	A dedicated DBConnection class ensures a secure and reusable connection setup.
2.	User Authentication:
o	Handles login functionality by validating user credentials stored in the database.
o	Ensures secure access to user-specific data.
3.	Data Management:
o	CRUD (Create, Read, Update, Delete) operations are implemented for users, expenses, and savings goals.
o	Supports multi-user functionality with separate tables for user data, expenses, and analytics.
4.	Financial Calculations:
o	Backend logic computes analytics, such as the percentage of money spent on each category.
o	Tracks weekly savings goals and provides alerts or updates as needed.


 
COMPLEXITIES:
Frontend:
•	Layout Management: Using nested layouts (e.g., BorderLayout, GridLayout) for a responsive and user-friendly interface.
•	Event Handling: Managing user actions like button clicks and real-time updates for charts or goals.
•	Error Handling: Validating inputs and handling backend failures gracefully.
Backend:
•	Database Connectivity: Secure and efficient connection to MySQL with exception handling.
•	Business Logic: Real-time calculations for expenses, percentages, and savings goals.
•	Security: Protecting user data with secure storage and preventing SQL injection.
Overall:
•	Integration: Ensuring seamless communication between Swing frontend and JDBC backend.
•	Performance: Optimizing queries and maintaining responsiveness as data grows.
•	Scalability: Supporting more users and features with modular design.
CONTRIBUTIONS:

Saravana Kumar and Thulasi Sri – Frontend part and Documentation
Suvi Sharon and Dilshan – Backend with SQL Integration
CODE SNIPPETS:
JDBC:
private static final String USER = "root";  // Replace with your database username
private static final String PASSWORD = "***";  // Replace with your database password
private static final String URL = "jdbc:mysql://localhost:3306/expensetracker";

// Method to get a connection to the database
public static Connection getConnection() throws SQLException {
    try {
        // Load MySQL driver (optional but recommended for older versions of Java)
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new SQLException("MySQL JDBC Driver not found.");
    }

    // Return the connection object
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
Add Category Button:
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
PIE CHART:
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
IMAGES:   
   
