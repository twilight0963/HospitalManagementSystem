package org.HospitalSystem.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    //TODO: Change to your DB info
    private static final String URL = org.HospitalSystem.Classes.Static.Constants.DB_URL; // Replace with your DB name
    private static final String USER = org.HospitalSystem.Classes.Static.Constants.DB_USER; // Replace with your DB username
    private static final String PASSWORD = org.HospitalSystem.Classes.Static.Constants.DB_PASS; // Replace with your DB password

    public static final String ACCTABLE = "";

    //User ID for the logged-in user
    public static int user_id;

    private Connection connection;

    // Constructor: Establish connection
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
