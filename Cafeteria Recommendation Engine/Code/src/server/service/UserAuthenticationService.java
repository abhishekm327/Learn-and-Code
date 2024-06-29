package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.database.DatabaseConnection;

public class UserAuthenticationService {

    public boolean authenticate(String userId, String role, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE user_id = ? AND role = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, role);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return password.equals(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
