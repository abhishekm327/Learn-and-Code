package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.model.Profile;

public class ProfileDBOperation {

	public void updateProfile(JSONObject jsonRequest) throws DatabaseException {
		String query = "UPDATE profile SET food_type = ?, food_style = ?, spice_level = ?,  sweet = ? WHERE user_id = ?";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, jsonRequest.getString("foodType"));
			statement.setString(2, jsonRequest.getString("foodStyle"));
			statement.setString(3, jsonRequest.getString("spiceLevel"));
			statement.setString(4, jsonRequest.getString("sweet"));
			statement.setString(5, jsonRequest.getString("userId"));
			statement.executeUpdate();
		} catch (SQLException exception) {
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
