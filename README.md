# Expense Tracker

## Team Members
- **Suvi Sharon**  
- Dilshan Chinnappan  
- Saravana Kumar  
- Thulasi Sri  

## Description
The Expense Tracker is a **desktop-based Java application** designed to help users efficiently manage and monitor their financial activities. Built using **Swing for the frontend** and **JDBC for backend database interactions**, this application provides users with tools to **log expenses, set financial goals, and track savings.**

## Technologies Used
- **Frontend:** Java Swing, AWT
- **Backend:** Java, JDBC
- **Database:** MySQL

## Features
### Frontend
- **Login Page:** Secure authentication with username and password fields.
- **Dashboard:** Displays wallet balance, bank balance, and financial goals.
- **Expense Logging:** Input form with categories and save option.
- **Analytics View:** Displays spending data and category-wise percentages.
- **Savings Goals:** Set and track weekly savings goals.

### Backend
- **Database Connectivity:** Uses JDBC to connect to MySQL.
- **User Authentication:** Secure login with user-specific data access.
- **Data Management:** CRUD operations for expenses and savings goals.
- **Financial Calculations:** Computes spending percentages and savings goals.

## Technical Complexities
### Frontend
- **Layout Management:** Nested layouts for responsive UI.
- **Event Handling:** Real-time updates for charts and financial data.
- **Error Handling:** Validations for secure input and data retrieval.

### Backend
- **Database Security:** Protecting user data and preventing SQL injection.
- **Business Logic:** Real-time calculations for expenses and savings.
- **Scalability:** Modular design to support additional users and features.

## Contributions
- **Suvi Sharon & Dilshan Chinnappan:** Backend development and SQL integration.
- **Saravana Kumar & Thulasi Sri:** Frontend development and documentation.

## Code Snippets
### **JDBC Connection Setup**
```java
private static final String USER = "root";
private static final String PASSWORD = "***";
private static final String URL = "jdbc:mysql://localhost:3306/expensetracker";

public static Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new SQLException("MySQL JDBC Driver not found.");
    }
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
```

### **Adding a New Expense Category**
```java
JButton addCategoryButton = new JButton("Add Category");
addCategoryButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String newCategory = JOptionPane.showInputDialog("Enter new category name:");
        if (newCategory != null && !newCategory.trim().isEmpty()) {
            try (Connection con = DBConnection.getConnection()) {
                String query = "INSERT INTO user_expenses (category, budget, amount_spent, user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, newCategory);
                stmt.setDouble(2, 100);
                stmt.setDouble(3, 0);
                stmt.setInt(4, userid);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error inserting new category: " + ex.getMessage());
            }
        }
    }
});
```

### **Generating a Pie Chart for Expense Analysis**
```java
Connection con = DBConnection.getConnection();
String query = "SELECT ue.category, ue.amount_spent FROM user_expenses ue JOIN users u ON ue.user_id = u.id WHERE u.email = ?";
PreparedStatement stmt = con.prepareStatement(query);
stmt.setString(1, email);
ResultSet result = stmt.executeQuery();
DefaultPieDataset dataset = new DefaultPieDataset();
while (result.next()) {
    dataset.setValue(result.getString("category"), result.getDouble("amount_spent"));
}
JFreeChart chart = ChartFactory.createPieChart("Your Expenses", dataset, true, true, false);
```

## Documentation
📎 [Expense Tracker Documentation (Word File)](Your Google Drive or GitHub Link Here)

## Installation & Setup
1. **Clone the repository**:  
   ```sh
   git clone https://github.com/your-username/Expense-Tracker.git
   ```
2. **Import the project** into an IDE (Eclipse/IntelliJ).
3. **Set up MySQL database** (import provided SQL file).
4. **Run the application** and start tracking expenses
