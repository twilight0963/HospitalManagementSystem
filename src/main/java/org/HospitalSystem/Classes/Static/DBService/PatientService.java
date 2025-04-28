package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Patient;

public class PatientService {
    public static int addPatient(String fName, String lName, String status, DatabaseManager dbManager){
        String query = "INSERT INTO Patients (`Doctor_ID`, `FirstName`, `LastName`, `Status`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, DatabaseManager.user_id);
            stmt.setString(2, fName);
            stmt.setString(3, lName);
            stmt.setString(4, status);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating patient failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Patient addition failed: " + e.getMessage());
            return -1;
        }
    }
    public static int updatePatient(int id, String fName, String lName, String status, DatabaseManager dbManager){
        String query = "UPDATE Patients SET FirstName = ?, LastName = ?, Status = ? WHERE Patient_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, status);
            stmt.setInt(4, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating patient failed, no rows affected.");
            }
            return id;
        } catch (SQLException e) {
            System.err.println("Patient update failed: " + e.getMessage());
            return -1;
        }
    }
    public static Patient[] myPatients(DatabaseManager dbManager) {
        String query = "SELECT Patient_ID, FirstName, LastName, Status FROM Patients WHERE Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            stmt.setInt(1, DatabaseManager.user_id);
            ResultSet rs = stmt.executeQuery();

            // First, count the results
            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            // Create array of appropriate size
            Patient[] patients = new Patient[size];
            int index = 0;

            // Populate the array
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("Patient_ID"),
                    "", // password not needed for display
                    rs.getString("FirstName"),
                    rs.getString("LastName")
                );
                patient.status = rs.getString("Status");
                patients[index++] = patient;
            }
            return patients;
        } catch (SQLException e) {
            System.err.println("Failed to retrieve patients: " + e.getMessage());
            return new Patient[0]; // Return empty array on error
        }
    }
    public static void dischargePatient(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM Patients WHERE Patient_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to discharge patient: " + e.getMessage());
        }
    }
    public static int patientCount(DatabaseManager dbManager) {
        String query = "SELECT COUNT(*) AS count FROM Patients";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Failed to count patients: " + e.getMessage());
        }
        return 0;
    }
    public static Patient[] searchPatients(String searchTerm, DatabaseManager dbManager) {
        String query = """
            SELECT Patient_ID, FirstName, LastName, Status 
            FROM Patients 
            WHERE Doctor_ID = ? 
            AND (LOWER(FirstName) LIKE LOWER(?) OR LOWER(LastName) LIKE LOWER(?))
            """;
            
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                
            stmt.setInt(1, DatabaseManager.user_id);
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            
            ResultSet rs = stmt.executeQuery();

            // Count results
            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            Patient[] patients = new Patient[size];
            int index = 0;

            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("Patient_ID"),
                    "", // password not needed
                    rs.getString("FirstName"),
                    rs.getString("LastName")
                );
                patient.status = rs.getString("Status");
                patients[index++] = patient;
            }

            return patients;
        } catch (SQLException e) {
            System.err.println("Patient search failed: " + e.getMessage());
            return new Patient[0];
        }
    }

    //Get count of critical patients
    public static int criticalCount(DatabaseManager dbManager) {
        String query = "SELECT COUNT(*) AS count FROM Patients WHERE Status = 'Critical'";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Failed to count critical patients: " + e.getMessage());
        }
        return 0;
    }
    
}
