package org.HospitalSystem.Classes.Static.DBService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Exceptions.AuthError;

public class AuthService {
    public static boolean login(int id, String password, DatabaseManager dbManager) throws AuthError {
        String query = "SELECT Doctor_ID FROM Doctors WHERE Doctor_ID = ? AND Password = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DatabaseManager.user_id = id;
                return true;
            }
            throw new AuthError();
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
            throw new AuthError();
        }
    }
}