package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import server.model.Profile;

public class ProfileDBOperation {
	
  /*  public void insertProfile(String userId, String foodType, String foodStyle, String spiceLevel, String sweet) throws DatabaseException {
        String query = "INSERT INTO profile (user_id, food_type, food_style, spice_level, sweet) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            statement.setString(2, foodType);
            statement.setString(3, foodStyle);
            statement.setString(4, spiceLevel);
            statement.setString(5, sweet);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating Profile, Please try again later");
        }
    } */
	
    public void updateProfile(String userId, String foodType, String spiceLevel, String foodStyle, String sweet) throws DatabaseException {
        String query = "UPDATE profile SET food_type = ?, spice_level = ?, food_style = ?, sweet = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, foodType);
            statement.setString(2, spiceLevel);
            statement.setString(3, foodStyle);
            statement.setString(4, sweet);
            statement.setString(5, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating profile. Please try again later");
        }
    }
    
    public Profile fetchProfileByUserId(String userId) throws DatabaseException {
        String query = "SELECT * FROM profile WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String foodType = resultSet.getString("food_type");
                    String foodStyle = resultSet.getString("food_style");
                    String spiceLevel = resultSet.getString("spice_level");
                    String sweet = resultSet.getString("sweet");
                    return new Profile(userId, foodType, foodStyle, spiceLevel, sweet);
                } else {
                    throw new DatabaseException("Profile not found for user_id: " + userId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching profile. Please try again later");
        }
    }

}
