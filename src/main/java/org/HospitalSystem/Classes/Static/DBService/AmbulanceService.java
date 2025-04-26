package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.HospitalSystem.Classes.Ambulance;
import org.HospitalSystem.Classes.DatabaseManager;

public class AmbulanceService {
    
    public static int addAmbulance(String location, String status, int Doctor_ID, int Patient_ID, DatabaseManager dbManager) {
        String query = "INSERT INTO Ambulance (Location, Status, Doctor_ID, Patient_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, location);
            stmt.setString(2, status);
            if (Doctor_ID == 0) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, Doctor_ID);
            }
            if (Patient_ID == 0) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, Patient_ID);
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ambulance failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating ambulance failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add ambulance: " + e.getMessage());
            return -1;
        }
    }

    public static boolean updateAmbulance(int id, String location, String status, int doctorId, int patientId, DatabaseManager dbManager) {
        String query = "UPDATE Ambulance SET Location = ?, Status = ?, Doctor_ID = ?, Patient_ID = ? WHERE Ambulance_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setString(1, location);
            stmt.setString(2, status);
            
            if (doctorId == 0) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, doctorId);
            }
            
            if (patientId == 0) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, patientId);
            }
            
            stmt.setInt(5, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update ambulance: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteAmbulance(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM Ambulance WHERE Ambulance_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete ambulance: " + e.getMessage());
            return false;
        }
    }

    public static Ambulance[] getAllAmbulances(DatabaseManager dbManager) {
        String query = "SELECT * FROM Ambulance";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = stmt.executeQuery();
            
            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();
            
            Ambulance[] ambulances = new Ambulance[size];
            int index = 0;
            
            while (rs.next()) {
                Ambulance ambulance = new Ambulance(
                    rs.getInt("Ambulance_ID"),
                    rs.getString("Location"),
                    rs.getString("Status")
                );
                
                int doctorId = rs.getInt("Doctor_ID");
                if (!rs.wasNull()) {
                    ambulance.setDoctor(doctorId);
                }
                
                int patientId = rs.getInt("Patient_ID");
                if (!rs.wasNull()) {
                    ambulance.setPatient(patientId);
                }
                
                ambulances[index++] = ambulance;
            }
            
            return ambulances;
        } catch (SQLException e) {
            System.err.println("Failed to get ambulances: " + e.getMessage());
            return new Ambulance[0];
        }
    }

    public static Ambulance[] searchAmbulances(int searchId, DatabaseManager dbManager) {
        String query = "SELECT * FROM Ambulance WHERE Ambulance_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, searchId);
            ResultSet rs = stmt.executeQuery();
            
            // We expect at most one result since we're searching by primary key
            Ambulance[] ambulances = new Ambulance[1];
            
            if (rs.next()) {
                Ambulance ambulance = new Ambulance(
                    rs.getInt("Ambulance_ID"),
                    rs.getString("Location"),
                    rs.getString("Status")
                );
                
                int doctorId = rs.getInt("Doctor_ID");
                if (!rs.wasNull()) {
                    ambulance.setDoctor(doctorId);
                }
                
                int patientId = rs.getInt("Patient_ID");
                if (!rs.wasNull()) {
                    ambulance.setPatient(patientId);
                }
                
                ambulances[0] = ambulance;
            }
            
            return ambulances;
        } catch (SQLException e) {
            System.err.println("Failed to search ambulances: " + e.getMessage());
            return new Ambulance[0];
        }
    }
}
