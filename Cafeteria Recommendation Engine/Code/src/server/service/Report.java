package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.DatabaseConnection;


public class Report {
	 
	 public JSONArray getReports() {
		 String query = "SELECT * FROM recommended_menu ORDER BY rating DESC";
		    JSONArray reportArray = new JSONArray();

		    try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(query);
		         ResultSet resultSet = statement.executeQuery()) {

		        while (resultSet.next()) {
		            JSONObject reportJson = new JSONObject();
		            reportJson.put("foodId", resultSet.getString("food_id"));
		            reportJson.put("foodName", resultSet.getString("food_name"));
		            reportJson.put("rating", resultSet.getDouble("rating"));
		            reportJson.put("comments", resultSet.getString("comment"));
		            reportArray.put(reportJson);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return reportArray;
		}

}
