package com.bobatea.momandpops.backend.data;

import com.bobatea.momandpops.backend.models.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String dbUrl = "jdbc:sqlite:src/main/resources/com/bobatea/momandpops/database/database.db";
    private static Connection connection;
    private static DatabaseManager instance;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl);
        }
        return connection;
    }

    public static DatabaseManager getInstance() {
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static void createTables() {
        try (Statement stmt = getConnection().createStatement()) {

            // Enable foreign keys in SQLite
            stmt.execute("PRAGMA foreign_keys = ON");

            // Customers table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Customers (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,
                Phone TEXT NOT NULL,
                Email TEXT NOT NULL,
                Address TEXT,
                Password TEXT NOT NULL,
                RewardPoints INTEGER
            )
        """);

            // Cards table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Cards (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Customer_ID INTEGER NOT NULL,
                Card_Number TEXT NOT NULL,
                Security_Code TEXT NOT NULL,
                Exp_Date TEXT,
                FOREIGN KEY(Customer_ID) REFERENCES Customers(ID) ON DELETE CASCADE
            )
        """);

            // Employees table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Employees (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,
                Phone TEXT NOT NULL,
                Email TEXT NOT NULL,
                Address TEXT,
                Password TEXT NOT NULL,
                DOB TEXT NOT NULL,
                Banking_Info TEXT NOT NULL,
                Employee_Type TEXT NOT NULL,
                Role TEXT NOT NULL,
                Salary REAL NOT NULL
            )
        """);

            // Orders table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Orders (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Customer_ID INTEGER NOT NULL,
                Employee_ID INTEGER NOT NULL,
                Receipt_ID INTEGER NOT NULL,
                Total_Cost REAL NOT NULL,
                Payment_Method TEXT NOT NULL,
                Order_Type TEXT NOT NULL,
                Completion_Status TEXT DEFAULT 'false',
                Delivery_Address TEXT,
                Delivery_Instructions TEXT,
                Arrival_Time TEXT,
                Reward_Points_Earned INTEGER NOT NULL,
                Reward_Points_Used INTEGER NOT NULL,
                FOREIGN KEY(Customer_ID) REFERENCES Customers(ID) ON DELETE CASCADE,
                FOREIGN KEY(Employee_ID) REFERENCES Employees(ID),
                FOREIGN KEY(Receipt_ID) REFERENCES Receipts(ID) ON DELETE CASCADE
            )
        """);

            // Receipts table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Receipts (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Order_ID INTEGER NOT NULL,
                Total REAL NOT NULL,
                Date TEXT NOT NULL,
                FOREIGN KEY (Order_ID) REFERENCES Orders(ID) ON DELETE CASCADE
            )
        """);

            // Items table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS Items (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,
                Description TEXT NOT NULL,
                Cost REAL NOT NULL,
                Sizes TEXT,
                Colors TEXT,
                Options TEXT,
                Type TEXT NOT NULL,
                Toppings TEXT NOT NULL,
                Image_Path TEXT NOT NULL
            )
        """);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Remove from DB
     */
    public static void removeCustomer(Integer customerID){
        String query = "DELETE FROM Customers WHERE ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)){
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void removeEmployee(Integer customerID){
        String query = "DELETE FROM Employees WHERE ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)){
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
     * Insert into DB
     */
    public static void addOrder(Order order){
        String query = """
        INSERT INTO Orders (
            Customer_ID, Employee_ID, Receipt_ID, Total_Cost, Payment_Method,
            Order_Type, Completion_Status, Delivery_Address, Delivery_Instructions,
            Arrival_Time, Reward_Points_Earned, Reward_Points_Used
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getEmployeeId());
            stmt.setInt(3, order.getReceiptId());
            stmt.setDouble(4, order.getTotalCost());
            stmt.setString(5, order.getPaymentMethod());
            stmt.setString(6, order.getOrderType());
            stmt.setString(7, order.isCompletionStatus() ? "true" : "false");
            stmt.setString(8, order.getDeliveryAddress());
            stmt.setString(9, order.getDeliveryInstructions());
            stmt.setString(10, order.getArrivalTime());
            stmt.setInt(11, order.getRewardPointsEarned());
            stmt.setInt(12, order.getRewardPointsUsed());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addReceipt(Receipt receipt){
        String query = "INSERT INTO Receipts(ID, Order_ID, Total, Date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, receipt.getId());
            stmt.setInt(2, receipt.getOrderId());
            stmt.setDouble(3, receipt.getTotal());
            stmt.setString(4, receipt.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(Item item){
        String query = "INSERT INTO Items(ID, Name, Description, Cost, Sizes, Colors, Options, Toppings, Type, Image_Path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, item.getItemId());
            stmt.setString(2, item.name);
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getBasePrice());
            stmt.setString(5, item.getAttributeAsString("sizes"));
            stmt.setString(6, item.getAttributeAsString("colors"));
            stmt.setString(7, item.getAttributeAsString("crusts"));
            stmt.setString(8, item.getAttributeAsString("toppings"));
            stmt.setString(9, item.getType());
            stmt.setString(10, item.imagePath);  // ADD THIS LINE
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCard(Card card){
        String query = "INSERT INTO Cards(ID, Customer_ID, Card_Number, Security_Code, Exp_Date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, card.getId());
            stmt.setInt(2, card.getCustomerId());
            stmt.setString(3, card.getCardNumber());
            stmt.setString(4, card.getSecurityCode());
            stmt.setString(5, card.getExpDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCustomer(Customer customer){
        String query = "INSERT INTO Customers(Name, Phone, Email, Address, Password, RewardPoints) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getPassword());
            stmt.setInt(6, customer.getRewardPoints());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addEmployee(Employee employee){
        String query = "INSERT INTO Employees(ID, Name, Phone, Email, Address, Password, DOB, Banking_Info, Employee_Type, Role, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            stmt.setString(5, employee.getAddress());
            stmt.setString(6, employee.getPassword());
            stmt.setString(7, employee.getDob());
            stmt.setString(8, employee.getBankingInfo());
            stmt.setString(9, employee.getEmployeeType());
            stmt.setString(10, employee.getRole());
            stmt.setDouble(11, employee.getSalary());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Extract from DB
     */
    public static ArrayList<Receipt> getCustomerReceipts(Integer customerID){
        ArrayList<Receipt> result = new ArrayList<Receipt>();
        String query = "SELECT * FROM Cards WHERE ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt receipt = new Receipt(
                        rs.getInt("ID"),
                        rs.getInt("Order_ID"),
                        rs.getDouble("Total"),
                        rs.getString("Date")
                );
                result.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Order> getCustomerOrders(Integer customerID){
        ArrayList<Order> result = new ArrayList<Order>();
        String query = "SELECT * FROM Orders WHERE Customer_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("ID"),
                        rs.getInt("Customer_ID"),
                        rs.getObject("Employee_ID") != null ? rs.getInt("Employee_ID") : null,
                        rs.getObject("Receipt_ID") != null ? rs.getInt("Receipt_ID") : null,
                        rs.getDouble("Total_Cost"),
                        rs.getString("Payment_Method"),
                        rs.getString("Order_Type"),
                        "true".equals(rs.getString("Completion_Status")),
                        rs.getString("Delivery_Address"),
                        rs.getString("Delivery_Instructions"),
                        rs.getString("Arrival_Time"),
                        rs.getInt("Reward_Points_Earned"),
                        rs.getInt("Reward_Points_Used")
                );
                result.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Customer getCustomer(String name){
        String query = "SELECT * FROM Customers WHERE Name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getInt("RewardPoints")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Employee getEmployee(String name){
        String query = "SELECT * FROM Employees WHERE Name = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("DOB"),
                        rs.getString("Banking_Info"),
                        rs.getString("Employee_Type"),
                        rs.getString("Role"),
                        rs.getDouble("Salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Card> getCustomerCards(Integer customerID){
        ArrayList<Card> result = new ArrayList<Card>();
        String query = "SELECT * FROM Cards WHERE Customer_ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Card card = new Card(
                    rs.getInt("ID"),
                    rs.getInt("Customer_ID"),
                    rs.getString("Card_Number"),
                    rs.getString("Security_Code"),
                    rs.getString("Exp_Date")
                );
                result.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Item getItem(int itemID){
        Item resultItem = null;
        String query = "SELECT * FROM Items WHERE ID = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resultItem = new Item(
                    rs.getString("Name"),
                    rs.getInt("ID"),
                    rs.getString("Description"),
                    rs.getDouble("Cost"),
                    (rs.getString("Type") == "MENU"),
                    (rs.getString("Sizes") != null),
                    rs.getString("Image_Path"));

                resultItem.setAttributeAsString(rs.getString("Colors"), "colors");
                resultItem.setAttributeAsString(rs.getString("Options"), "crusts");
                resultItem.setAttributeAsString(rs.getString("Toppings"), "toppings");
                resultItem.setAttributeAsString(rs.getString("Sizes"), "sizes");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultItem;
    }

    public static ArrayList<Item> getAllItems() {
        ArrayList<Item> result = new ArrayList<Item>();
        String query = "SELECT * FROM Items";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Item item = new Item(
                        rs.getString("Name"),
                        rs.getInt("ID"),
                        rs.getString("Description"),
                        rs.getDouble("Cost"),
                        (rs.getString("Type") == "MENU"),
                        (rs.getString("Sizes") != null),
                        rs.getString("Image_Path")
                );

                item.setAttributeAsString(rs.getString("Colors"), "colors");
                item.setAttributeAsString(rs.getString("Options"), "crusts");
                item.setAttributeAsString(rs.getString("Toppings"), "toppings");
                item.setAttributeAsString(rs.getString("Sizes"), "sizes");

                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
