package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Prescription;

public class PrescriptionService {
    
    public static Prescription[] getPatientPrescriptions(int patientId, DatabaseManager dbManager) {
        String query = """
            SELECT p.*, i.Name as MedicineName 
            FROM prescription p
            JOIN Inventory i ON p.Medicine_ID = i.Medicine_ID
            WHERE p.Patient_ID = ? AND p.End_Date > NOW()
            ORDER BY p.End_Date DESC
        """;
        
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            Prescription[] prescriptions = new Prescription[size];
            int index = 0;

            while (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("Prescription_ID"),
                    rs.getInt("Patient_ID"),
                    rs.getInt("Medicine_ID"),
                    rs.getInt("Doctor_ID"),
                    rs.getDouble("Dosage"),
                    rs.getTimestamp("End_Date").toLocalDateTime()
                );
                prescription.medicineName = rs.getString("MedicineName");
                prescriptions[index++] = prescription;
            }

            return prescriptions;
        } catch (SQLException e) {
            System.err.println("Failed to get prescriptions: " + e.getMessage());
            return new Prescription[0];
        }
    }

    public static int addPrescription(int patientId, int medicineId, double dosage, 
                                    LocalDateTime endDate, DatabaseManager dbManager) {
        String query = """
            INSERT INTO prescription (Patient_ID, Medicine_ID, Doctor_ID, Dosage, End_Date) 
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, medicineId);
            stmt.setInt(3, DatabaseManager.user_id);  // Current logged-in doctor
            stmt.setDouble(4, dosage);
            stmt.setTimestamp(5, Timestamp.valueOf(endDate));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating prescription failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating prescription failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add prescription: " + e.getMessage());
            return -1;
        }
    }

    public static boolean deletePrescription(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM prescription WHERE Prescription_ID = ? AND Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, DatabaseManager.user_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete prescription: " + e.getMessage());
            return false;
        }
    }
}