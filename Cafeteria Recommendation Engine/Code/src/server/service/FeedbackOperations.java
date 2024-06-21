package server.service;

import server.model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;

public class FeedbackOperations {

    public void provideFeedback(String foodId, String comment, double rating) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO feedback (food_id, comment, rating, feedback_date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, foodId);
            statement.setString(2, comment);
            statement.setDouble(3, rating);
            statement.setDate(4, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Feedback> fetchFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM feedback";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                String foodName = resultSet.getString("food_name");
                String foodId = resultSet.getString("food_id");
                String comment = resultSet.getString("comment");
                double rating = resultSet.getDouble("rating");
                Date feedbackDate = resultSet.getDate("feedback_date");
                feedbackList.add(new Feedback(feedbackId, foodId, foodName, comment, rating, feedbackDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }   
}
