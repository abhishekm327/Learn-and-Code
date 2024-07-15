package server.database;

import server.database.exception.DatabaseException;
import server.model.RolloutMenu;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class RolloutMenuDBOperation {

	RecommendedMenuDBOperation recommendedMenuDBOperation = new RecommendedMenuDBOperation();

	public List<RolloutMenu> fetchRolloutItems() throws DatabaseException {
		List<RolloutMenu> rolloutItems = new ArrayList<>();
		String query = "SELECT rollout_menu.food_id, rollout_menu.food_name, rollout_menu.cooking_date, "
				+ "rollout_menu.rating, rollout_menu.vote, menu.food_type, menu.food_style, menu.spice_level, "
				+ "menu.sweet FROM rollout_menu INNER JOIN menu ON rollout_menu.food_id = menu.food_id "
				+ "ORDER BY rollout_menu.vote DESC";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String foodId = resultSet.getString("food_id");
				String foodName = resultSet.getString("food_name");
				Date cookingDate = resultSet.getDate("cooking_date");
				double rating = resultSet.getDouble("rating");
				int vote = resultSet.getInt("vote");
				String foodType = resultSet.getString("food_type");
				String foodStyle = resultSet.getString("food_style");
				String spiceLevel = resultSet.getString("spice_level");
				String sweet = resultSet.getString("sweet");

				RolloutMenu rolloutMenu = new RolloutMenu(foodId, foodName, cookingDate, rating, vote);
				rolloutMenu.setFoodType(foodType);
				rolloutMenu.setFoodStyle(foodStyle);
				rolloutMenu.setSpiceLevel(spiceLevel);
				rolloutMenu.setSweet(sweet);
				rolloutItems.add(rolloutMenu);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error while fetching rollout items. Please try again later");
		}
		return rolloutItems;
	}

	public void insertRolloutMenuItems(JSONArray items, Date cookingDate) throws DatabaseException {
		try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
			clearPreviousRolloutItems(connection);
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				String foodId = item.getString("foodId");
				JSONObject foodItem = recommendedMenuDBOperation.selectRecommendedItem(connection, foodId);
				insertNewItem(connection, foodItem, cookingDate);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error while inserting rollout menu items. Please try again later");
		}
	}

	public void updateRolloutMenuItemVotes(String foodId) throws DatabaseException {
		String query = "UPDATE rollout_menu SET vote = vote + 1 WHERE food_id = ?";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, foodId);
			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated == 0) {
				throw new DatabaseException("This food_id does not found. Please Enter valid FoodId from rollout menu");
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error while updating rollout menu item votes");
		}
	}

	private void clearPreviousRolloutItems(Connection connection) throws DatabaseException {
		String deleteQuery = "DELETE FROM rollout_menu";
		try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error while clearing Previous Rollout menu item");
		}
	}

	private void insertNewItem(Connection connection, JSONObject foodItem, Date cookingDate) throws DatabaseException {
		String insertQuery = "INSERT INTO rollout_menu (food_id, food_name, cooking_date, rating, vote) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
			insertStatement.setString(1, foodItem.getString("foodId"));
			insertStatement.setString(2, foodItem.getString("foodName"));
			insertStatement.setDate(3, new java.sql.Date(cookingDate.getTime()));
			insertStatement.setDouble(4, foodItem.getDouble("rating"));
			insertStatement.setInt(5, 0);
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error while inserting new item");
		}
	}
}
