package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.HospitalSystem.Classes.DatabaseManager;

public class UserAddService {
    public static int addDoctor(String fName, String lName, String password, DatabaseManager dbManager) {
        String query = "INSERT INTO Doctors (`FirstName`, `LastName`, `Password`) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, password);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating doctor failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Doctor creation failed: " + e.getMessage());
            return -1;
        }
    }
}
