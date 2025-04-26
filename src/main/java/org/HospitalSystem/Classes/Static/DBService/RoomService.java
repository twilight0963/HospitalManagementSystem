package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Patient;
import org.HospitalSystem.Classes.Room;

public class RoomService {
    public static void addRoom(Room room, DatabaseManager dbManager) {
        String query = "INSERT INTO Rooms (Occupant, Type, Price) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            if (room.occupant_id == 0) {
                stmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(1, room.occupant_id);
            }
            stmt.setString(2, room.type);
            stmt.setInt(3, room.price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to add room: " + e.getMessage());
        }
    }
    public static void fillRoom(Room room, Patient patient, DatabaseManager dbManager){
        String query = "UPDATE Rooms SET Occupant = ? WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, patient.id);
            stmt.setInt(2, room.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to fill room: " + e.getMessage());
        }
    }
    public static void emptyRoom(Room room, DatabaseManager dbManager){
        String query = "UPDATE Rooms SET Occupant = NULL WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, room.id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to empty room: " + e.getMessage());
        }
    }
    public static Room[] getAllRooms(DatabaseManager dbManager) {
        String query = "SELECT * FROM Rooms";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            Room[] rooms = new Room[100];
            int index = 0;
            while (rs.next()) {
                int id = rs.getInt("Room_ID");
                String type = rs.getString("Type");
                int price = rs.getInt("Price");
                int occupantId = rs.getInt("Occupant");
                if (occupantId != 0){
                    rooms[index++] = new Room(id, type, price).setOccupant(occupantId);
                }else{
                    rooms[index++] = new Room(id, type, price);
                }
            }
            return rooms;
        } catch (SQLException e) {
            System.err.println("Failed to retrieve rooms: " + e.getMessage());
            return null;
        }
    }
    public static void emptyRoom(int id, DatabaseManager dbManager) {
        String query = "UPDATE Rooms SET Occupant = NULL WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to empty room: " + e.getMessage());
        }
    }
    public static void removeRoom(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM Rooms WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to remove room: " + e.getMessage());
        }
    }
    public static boolean updateRoom(int id, String type, int occupantId, DatabaseManager dbManager) {
        String query = "UPDATE Rooms SET Type = ?, Occupant = ? WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setString(1, type);
            if (occupantId == 0) {
                stmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(2, occupantId);
            }
            stmt.setInt(3, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Room update failed: " + e.getMessage());
            return false;
        }
    }
    public static Room[] searchRooms(int searchId, DatabaseManager dbManager) {
        String query = "SELECT * FROM Rooms WHERE Room_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, searchId);
            ResultSet rs = stmt.executeQuery();
            
            Room[] rooms = new Room[1];  // Only expect one result for ID search
            if (rs.next()) {
                rooms[0] = new Room(
                    rs.getInt("Room_ID"),
                    rs.getString("Type"),
                    rs.getInt("Price")
                );
                if (rs.getInt("Occupant") != 0) {
                    rooms[0].setOccupant(rs.getInt("Occupant"));
                }
            }
            return rooms;
        } catch (SQLException e) {
            System.err.println("Room search failed: " + e.getMessage());
            return new Room[0];
        }
    }
}
