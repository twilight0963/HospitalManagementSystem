package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.HospitalSystem.Classes.Appointment;
import org.HospitalSystem.Classes.DatabaseManager;

public class AppointmentService {
    public static int addAppointment(int patientId, LocalDateTime startTime, 
                                   LocalDateTime endTime, String description, DatabaseManager dbManager) {
        String query = "INSERT INTO Appointments (Doctor_ID, Patient_ID, StartTime, EndTime, Description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, DatabaseManager.user_id);
            stmt.setInt(2, patientId);
            stmt.setTimestamp(3, Timestamp.valueOf(startTime));
            stmt.setTimestamp(4, Timestamp.valueOf(endTime));
            stmt.setString(5, description);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating appointment failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Appointment addition failed: " + e.getMessage());
            return -1;
        }
    }

    public static boolean updateAppointment(int id, int patientId, LocalDateTime startTime, 
                                          LocalDateTime endTime, String description, DatabaseManager dbManager) {
        String query = "UPDATE Appointments SET Patient_ID = ?, StartTime = ?, EndTime = ?, Description = ? WHERE Appointment_ID = ? AND Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, patientId);
            stmt.setTimestamp(2, Timestamp.valueOf(startTime));
            stmt.setTimestamp(3, Timestamp.valueOf(endTime));
            stmt.setString(4, description);
            stmt.setInt(5, id);
            stmt.setInt(6, DatabaseManager.user_id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Appointment update failed: " + e.getMessage());
            return false;
        }
    }

    public static Appointment[] getMyAppointments(DatabaseManager dbManager) {
        String query = """
            SELECT * FROM Appointments 
            WHERE Doctor_ID = ? AND EndTime > NOW() 
            ORDER BY StartTime ASC
            """;
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            stmt.setInt(1, DatabaseManager.user_id);
            ResultSet rs = stmt.executeQuery();

            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            Appointment[] appointments = new Appointment[size];
            int index = 0;

            while (rs.next()) {
                appointments[index++] = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getInt("Doctor_ID"),
                    rs.getInt("Patient_ID"),
                    rs.getTimestamp("StartTime").toLocalDateTime(),
                    rs.getTimestamp("EndTime").toLocalDateTime(),
                    rs.getString("Description")
                );
            }

            return appointments;
        } catch (SQLException e) {
            System.err.println("Failed to retrieve appointments: " + e.getMessage());
            return new Appointment[0];
        }
    }

    public static boolean deleteAppointment(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM Appointments WHERE Appointment_ID = ? AND Doctor_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, DatabaseManager.user_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete appointment: " + e.getMessage());
            return false;
        }
    }
}