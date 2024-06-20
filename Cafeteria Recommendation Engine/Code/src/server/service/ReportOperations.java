package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import server.model.Report;

public class ReportOperations {
	
	 public boolean generateReport() {
	        Map<String, Report> reportMap = new HashMap<>();

	        try (Connection connection = DatabaseConnection.getConnection()) {
	        	String deleteQuery = "DELETE FROM reports";
	            String selectQuery = "SELECT food_id, food_name, comment, rating FROM feedback";
	            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
	            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	            deleteStatement.executeUpdate();
	            ResultSet resultSet = selectStatement.executeQuery();

	            while (resultSet.next()) {
	                String foodId = resultSet.getString("food_id");
	                String foodName = resultSet.getString("food_name");
	                String comment = resultSet.getString("comment");
	                double rating = resultSet.getDouble("rating");

	                Report report = reportMap.getOrDefault(foodId, new Report(foodId, foodName));
	                report.addComment(comment);
	                report.addRating(rating);
	                reportMap.put(foodId, report);
	            }

	            selectStatement.close();

	            String insertQuery = "INSERT INTO reports (food_id, food_name, comments, average_rating, sentiment_analysis) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
	            for (Report report : reportMap.values()) {
	                String combinedComments = String.join(" | ", report.getComments());
	                String sentiment = SentimentAnalysis.analyzeSentiment(combinedComments);
	                report.setSentimentAnalysis(sentiment);
	            	
	                insertStatement.setString(1, report.getFoodId());
	                insertStatement.setString(2, report.getFoodName());
	                insertStatement.setString(3, combinedComments);
	                insertStatement.setDouble(4, report.getAverageRating());
	                insertStatement.setString(5, report.getSentimentAnalysis());
	                insertStatement.addBatch();
	            }

	            insertStatement.executeBatch();
	            insertStatement.close();

	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 
	 public JSONArray getReports() {
		 String query = "SELECT food_id, food_name, comments, average_rating, sentiment_analysis FROM reports ORDER BY average_rating DESC";
		    JSONArray reportArray = new JSONArray();

		    try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(query);
		         ResultSet resultSet = statement.executeQuery()) {

		        while (resultSet.next()) {
		            JSONObject reportJson = new JSONObject();
		            reportJson.put("foodId", resultSet.getString("food_id"));
		            reportJson.put("foodName", resultSet.getString("food_name"));
		            reportJson.put("comments", resultSet.getString("comments"));
		            reportJson.put("averageRating", resultSet.getDouble("average_rating"));
		            reportJson.put("sentimentAnalysis", resultSet.getString("sentiment_analysis"));
		            reportArray.put(reportJson);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return reportArray;
		}

}
