package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecommendedMenuOperations {
	
	public boolean generateRecommendedMenu() {
	    String deleteQuery = "DELETE FROM recommended_menu";
	    String selectQuery = "SELECT food_id, food_name, average_rating FROM reports ORDER BY average_rating DESC LIMIT 10";
	    String insertQuery = "INSERT INTO recommended_menu (food_id, food_name, rating, recommended_date) VALUES (?, ?, ?, ?)";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
	         PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	         PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

	        deleteStatement.executeUpdate();

	        ResultSet resultSet = selectStatement.executeQuery();
	        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

	        while (resultSet.next()) {
	            insertStatement.setString(1, resultSet.getString("food_id"));
	            insertStatement.setString(2, resultSet.getString("food_name"));
	            insertStatement.setDouble(3, resultSet.getDouble("average_rating"));
	            insertStatement.setDate(4, currentDate);
	            insertStatement.addBatch();
	        }

	        insertStatement.executeBatch();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public JSONArray getRecommendedMenu() {
	    String query = "SELECT food_id, food_name, rating, recommended_date FROM recommended_menu";
	    JSONArray recommendedMenuArray = new JSONArray();

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            JSONObject menuJson = new JSONObject();
	            menuJson.put("foodId", resultSet.getString("food_id"));
	            menuJson.put("foodName", resultSet.getString("food_name"));
	            menuJson.put("rating", resultSet.getDouble("rating"));
	            menuJson.put("date", resultSet.getDate("recommended_date").toString());
	            recommendedMenuArray.put(menuJson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return recommendedMenuArray;
	}

}
