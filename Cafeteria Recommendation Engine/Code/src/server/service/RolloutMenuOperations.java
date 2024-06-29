package server.service;

import server.database.DatabaseConnection;
import server.model.FoodMenu;
import server.model.RolloutMenu;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RolloutMenuOperations {
    NotificationService notificationService = new NotificationService();

    public List<RolloutMenu> fetchRolloutItems() {
        List<RolloutMenu> recommendedItems = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM rollout_menu ORDER BY vote DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String foodId = resultSet.getString("food_id");
                String foodName = resultSet.getString("food_name");
                Date cookingDate = resultSet.getDate("cooking_date");
                double rating = resultSet.getDouble("rating");
                int vote = resultSet.getInt("vote");
                recommendedItems.add(new RolloutMenu(foodId, foodName, cookingDate, rating, vote));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendedItems;
    }
    
    public boolean addItemsToRolloutMenu(JSONArray items, String cookingDateString) {	
    	java.sql.Date cookingDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = format.parse(cookingDateString);
            cookingDate = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } 
        
        String deleteQuery = "DELETE FROM rollout_menu";
        String selectQuery = "SELECT food_id, food_name, rating FROM recommended_menu WHERE food_id = ?";
        String insertQuery = "INSERT INTO rollout_menu (food_id, food_name, cooking_date, rating, vote) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            deleteStatement.executeUpdate();

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String foodId = item.getString("foodId");

                selectStatement.setString(1, foodId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    String foodName = resultSet.getString("food_name");
                    double rating = resultSet.getDouble("rating");

                    insertStatement.setString(1, foodId);
                    insertStatement.setString(2, foodName);
                    insertStatement.setDate(3, cookingDate);
                    insertStatement.setDouble(4, rating);
                    insertStatement.setInt(5, 0); 
                    insertStatement.addBatch();
                } else {
                    return false;
                }
            }

            insertStatement.executeBatch();
            String message = "New rollout menu has been generated. Please vote for your favorite items!";
            notificationService.sendNotification(message);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    public void voteForRolloutMenuItem(String foodId) {
        String query = "UPDATE rollout_menu SET vote = vote + 1 WHERE food_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, foodId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
