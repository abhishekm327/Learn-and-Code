package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.model.RecommendedMenu;

public class RecommendedMenuDBOperation {

	public void clearPreviousRecommendations() throws DatabaseException {
		String deleteQuery = "DELETE FROM recommended_menu";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
			deleteStatement.executeUpdate();
		} catch (SQLException exception) {
			throw new DatabaseException("Error while clearing previous recommendations");
		}
	}

	public void insertRecommendedItems(List<RecommendedMenu> topItems) throws DatabaseException {
		String insertQuery = "INSERT INTO recommended_menu (food_id, food_name, comment, rating) VALUES (?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
			for (RecommendedMenu recommendedMenu : topItems) {
				insertStatement.setString(1, recommendedMenu.getFoodId());
				insertStatement.setString(2, recommendedMenu.getFoodName());
				insertStatement.setString(3, String.join(" | ", recommendedMenu.getComments()));
				insertStatement.setDouble(4, recommendedMenu.getAverageRating());
				insertStatement.addBatch();
			}
			insertStatement.executeBatch();
		} catch (SQLException exception) {
			throw new DatabaseException("Error while inserting recommended items. Please try again later");
		}
	}

	public JSONArray fetchRecommendedMenu() throws DatabaseException {
		String query = "SELECT food_id, food_name, rating, comment FROM recommended_menu ORDER BY rating DESC LIMIT 10";
		JSONArray recommendedMenuArray = new JSONArray();
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				JSONObject menuJson = new JSONObject();
				menuJson.put("foodId", resultSet.getString("food_id"));
				menuJson.put("foodName", resultSet.getString("food_name"));
				menuJson.put("rating", resultSet.getDouble("rating"));
				menuJson.put("comment", resultSet.getString("comment"));
				recommendedMenuArray.put(menuJson);
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching recommended menu. Please try again later");
		}
		return recommendedMenuArray;
	}
}
