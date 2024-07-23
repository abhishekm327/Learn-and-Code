package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.model.Feedback;

public class FeedbackDBOperation {

	public void insertFeedback(JSONObject jsonRequest) throws DatabaseException {
		String query = "INSERT INTO feedback (food_id, comment, rating, feedback_date, user_id) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, jsonRequest.getString("foodId"));
			statement.setString(2, jsonRequest.getString("comment"));
			statement.setDouble(3, jsonRequest.getDouble("rating"));
			statement.setDate(4, new java.sql.Date(new Date().getTime()));
			statement.setString(5, jsonRequest.getString("userId"));
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException integrityException) {
			throw new DatabaseException("This food_id does not found. Please Enter valid FoodId");
		} catch (SQLException exception) {
			throw new DatabaseException("Error while inserting feedback, Please try again later");
		}
	}

	public List<Feedback> fetchFeedback() throws DatabaseException {
		List<Feedback> feedbackList = new ArrayList<>();
		String query = "SELECT * FROM feedback WHERE feedback_date >= NOW() - INTERVAL 1 MONTH;";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int feedbackId = resultSet.getInt("feedback_id");
				String foodId = resultSet.getString("food_id");
				String foodName = resultSet.getString("food_name");
				String comment = resultSet.getString("comment");
				double rating = resultSet.getDouble("rating");
				Date feedbackDate = resultSet.getDate("feedback_date");
				String userId = resultSet.getString("user_id");
				feedbackList.add(new Feedback(feedbackId, foodId, foodName, comment, rating, feedbackDate, userId));
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching feedback. Please try again later");
		}
		return feedbackList;
	}

	public void insertDiscardMenuFeedback(JSONObject jsonRequest) throws DatabaseException {
		String query = "INSERT INTO discard_menu_feedback (food_id, disliked_aspects, preferred_taste, mom_recipe, user_id) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, jsonRequest.getString("foodId"));
			statement.setString(2, jsonRequest.getString("dislikedAspects"));
			statement.setString(3, jsonRequest.getString("preferredTaste"));
			statement.setString(4, jsonRequest.getString("momRecipe"));
			statement.setString(5, jsonRequest.getString("userId"));
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException integrityException) {
			throw new DatabaseException("This food_id does not found. Please Enter valid FoodId");
		} catch (SQLException exception) {
			throw new DatabaseException("Error while inserting Discard Menu feedback, Please try again later");
		}
	}

	public JSONArray fetchDiscardMenuFeedback() throws DatabaseException {
		String query = "SELECT * FROM discard_menu_feedback WHERE feedback_date >= NOW() - INTERVAL 1 MONTH;";
		JSONArray feedbackArray = new JSONArray();
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				JSONObject feedbackJson = new JSONObject();
				feedbackJson.put("foodId", resultSet.getString("food_id"));
				feedbackJson.put("foodName", resultSet.getString("food_name"));
				feedbackJson.put("dislikedAspects", resultSet.getString("disliked_aspects"));
				feedbackJson.put("preferredTaste", resultSet.getString("preferred_taste"));
				feedbackJson.put("momRecipe", resultSet.getString("mom_recipe"));
				feedbackJson.put("userId", resultSet.getString("user_id"));
				feedbackJson.put("feedbackDate", resultSet.getDate("feedback_date"));
				feedbackArray.put(feedbackJson);
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching the FoodMenu Items. Please try again later");
		}
		return feedbackArray;
	}
}
