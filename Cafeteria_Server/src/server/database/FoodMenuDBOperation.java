package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.model.FoodMenu;

public class FoodMenuDBOperation {
	public JSONArray fetchFoodMenuItems() throws DatabaseException {
		String query = "SELECT * FROM menu";
		JSONArray menuArray = new JSONArray();
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				JSONObject menuJson = new JSONObject();
				menuJson.put("foodId", resultSet.getString("food_id"));
				menuJson.put("name", resultSet.getString("food_name"));
				menuJson.put("price", resultSet.getInt("price"));
				menuArray.put(menuJson);
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching the FoodMenu Items. Please try again later");
		}
		return menuArray;
	}

	public void addFoodMenuItem(FoodMenu item) throws DatabaseException {
		String query = "INSERT INTO menu (food_id, food_name, price, food_type, food_style, spice_level, sweet) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, item.getFoodId());
			statement.setString(2, item.getName());
			statement.setInt(3, item.getPrice());
			statement.setString(4, item.getFoodType());
			statement.setString(5, item.getFoodStyle());
			statement.setString(6, item.getSpiceLevel());
			statement.setString(7, item.getSweet());
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException integrityException) {
			throw new DatabaseException("Error in Adding FoodMenu Item -  This FoodId Already exist");
		} catch (SQLException exception) {
			throw new DatabaseException("Error while Adding FoodMenu Item. Please try again later");
		}
	}

	public void updateFoodMenuItem(FoodMenu item) throws DatabaseException {
		String query = "UPDATE menu SET food_name = ?, price = ?, food_type=?, food_style=?, spice_level=?, sweet=? WHERE food_id = ?";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, item.getName());
			statement.setInt(2, item.getPrice());
			statement.setString(3, item.getFoodType());
			statement.setString(4, item.getFoodStyle());
			statement.setString(5, item.getSpiceLevel());
			statement.setString(6, item.getSweet());
			statement.setString(7, item.getFoodId());
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 0) {
				throw new DatabaseException("This foodId does not found. Please Enter valid FoodId from Menu");
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while Updating FoodMenu Item. Please try again later");
		}
	}

	public void deleteFoodMenuItem(String foodId) throws DatabaseException {
		String query = "DELETE FROM menu WHERE food_id = ?";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, foodId);
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted == 0) {
				throw new DatabaseException("This foodId does not found. Please Enter valid FoodId from Menu");
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while Deleting FoodMenu Item. Please try again later");
		}
	}

	public JSONObject selectMenuItem(Connection connection, String foodId) throws DatabaseException {
		String selectQuery = "SELECT food_id, food_name FROM menu WHERE food_id = ?";
		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setString(1, foodId);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				JSONObject foodItem = new JSONObject();
				foodItem.put("foodId", resultSet.getString("food_id"));
				foodItem.put("foodName", resultSet.getString("food_name"));
				return foodItem;
			} else {
				throw new DatabaseException("FoodID " + foodId + " does not found in Menu. Please Enter valid FoodId");
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while Selecting from menu item. Please try again later");
		}
	}
}
