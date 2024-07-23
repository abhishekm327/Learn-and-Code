package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class UserDBOperation {

	public JSONObject getUserInfo(String userId) {
		String query = "SELECT user_id, role, password FROM users WHERE user_id = ?";
		JSONObject userInfo = new JSONObject();
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, userId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userInfo.put("role", resultSet.getString("role"));
				userInfo.put("password", resultSet.getString("password"));
				userInfo.put("exist", true);
			} else {
				userInfo.put("exist", false);
			}
		} catch (SQLException exception) {
			userInfo.put("exist", false);
			userInfo.put("error", "Database error. Plesae try again Later");
		}
		return userInfo;
	}
}
