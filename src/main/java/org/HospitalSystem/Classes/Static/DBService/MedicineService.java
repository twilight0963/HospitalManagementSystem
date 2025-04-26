package org.HospitalSystem.Classes.Static.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Medicine;

public class MedicineService {
    public static int addMedicine(String name, String iconPath, double defaultDosage, double price, DatabaseManager dbManager) {
        String query = "INSERT INTO Inventory (Name, Icon_Path, Default_Dosage, Price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, iconPath);
            stmt.setDouble(3, defaultDosage);
            stmt.setDouble(4, price);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating medicine failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating medicine failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add medicine: " + e.getMessage());
            return -1;
        }
    }

    public static boolean updateMedicine(int id, String name, String iconPath, double defaultDosage, double price, DatabaseManager dbManager) {
        String query = "UPDATE Inventory SET Name = ?, Icon_Path = ?, Default_Dosage = ?, Price = ? WHERE Medicine_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, iconPath);
            stmt.setDouble(3, defaultDosage);
            stmt.setDouble(4, price);
            stmt.setInt(5, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update medicine: " + e.getMessage());
            return false;
        }
    }

    public static Medicine[] searchMedicines(String name, DatabaseManager dbManager) {
        String query = "SELECT * FROM Inventory WHERE LOWER(Name) LIKE LOWER(?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            Medicine[] medicines = new Medicine[size];
            int index = 0;

            while (rs.next()) {
                medicines[index++] = new Medicine(
                    rs.getInt("Medicine_ID"),
                    rs.getString("Name"),
                    rs.getDouble("Default_Dosage"),
                    rs.getDouble("Price")
                );
            }

            return medicines;
        } catch (SQLException e) {
            System.err.println("Medicine search failed: " + e.getMessage());
            return new Medicine[0];
        }
    }

    public static boolean deleteMedicine(int id, DatabaseManager dbManager) {
        String query = "DELETE FROM Inventory WHERE Medicine_ID = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete medicine: " + e.getMessage());
            return false;
        }
    }

    public static Medicine[] getAllMedicines(DatabaseManager dbManager) {
        String query = "SELECT * FROM Inventory ORDER BY Name";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = stmt.executeQuery();

            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();

            Medicine[] medicines = new Medicine[size];
            int index = 0;

            while (rs.next()) {
                medicines[index++] = new Medicine(
                    rs.getInt("Medicine_ID"),
                    rs.getString("Name"),
                    rs.getDouble("Default_Dosage"),
                    rs.getDouble("Price")
                );
            }

            return medicines;
        } catch (SQLException e) {
            System.err.println("Failed to get medicines: " + e.getMessage());
            return new Medicine[0];
        }
    }
}