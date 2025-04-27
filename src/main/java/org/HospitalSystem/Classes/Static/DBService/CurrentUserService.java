package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Doctor;

public class CurrentUserService {
    static int cur_id = DatabaseManager.user_id;
    static Doctor doctor = null;
    public static Doctor getInfo(DatabaseManager dbManager) {
        String query = "SELECT * FROM Doctors WHERE Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, cur_id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.err.println("No user found with ID: " + cur_id);
                return null;
            }
            doctor = new Doctor(rs.getInt("Doctor_ID"), 
                                    rs.getString("Password"), 
                                    rs.getString("FirstName"), 
                                    rs.getString("LastName"));
            doctor.specialise(rs.getInt("Specialisation_ID"));
            return doctor;

        } catch (SQLException e) {
            System.err.println("Not authenticated. " + e.getMessage());
            return null;
        }
    }

    public static boolean updateSpecialisation(int specialisation, DatabaseManager dbManager) {
        String query = "UPDATE Doctors SET Specialisation_ID = ? WHERE Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, specialisation);
            stmt.setInt(2, DatabaseManager.user_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update specialisation: " + e.getMessage());
            return false;
        }
    }
    // Logout function
    public static void logout() {
        doctor = null;
        cur_id = 0;
        DatabaseManager.user_id = 0;
    }
}
