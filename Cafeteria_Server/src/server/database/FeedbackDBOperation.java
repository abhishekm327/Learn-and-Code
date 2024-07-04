package server.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.model.Feedback;

public class FeedbackDBOperation {
	
    public void insertFeedback(String foodId, String comment, double rating) throws DatabaseException {
        String query = "INSERT INTO feedback (food_id, comment, rating, feedback_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, foodId);
            statement.setString(2, comment);
            statement.setDouble(3, rating);
            statement.setDate(4, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting feedback, Please try again later");
        }
    }

    public List<Feedback> fetchFeedback() throws DatabaseException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
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
                feedbackList.add(new Feedback(feedbackId, foodId, foodName, comment, rating, feedbackDate));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching feedback. Please try again later");
        }
        return feedbackList;
    }

}
