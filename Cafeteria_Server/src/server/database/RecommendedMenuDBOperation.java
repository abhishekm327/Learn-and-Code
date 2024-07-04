package server.database;

import org.json.JSONArray;
import org.json.JSONObject;
import server.model.RecommendedMenu;
import java.sql.*;
import java.util.List;

public class RecommendedMenuDBOperation {

    public void clearPreviousRecommendations() throws DatabaseException {
        String deleteQuery = "DELETE FROM recommended_menu";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting recommended items. Please try again later");
        }
    }
    
    public JSONObject selectRecommendedItem(Connection connection, String foodId) throws DatabaseException {
        String selectQuery = "SELECT food_id, food_name, rating FROM recommended_menu WHERE food_id = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, foodId);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                JSONObject foodItem = new JSONObject();
                foodItem.put("foodId", resultSet.getString("food_id"));
                foodItem.put("foodName", resultSet.getString("food_name"));
                foodItem.put("rating", resultSet.getDouble("rating"));
                return foodItem;
            } else {
                throw new DatabaseException("Food ID not found in Menu: " + foodId);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Error while Selecting from recommended menu item");
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
                recommendedMenuArray.put(menuJson);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching recommended menu. Please try again later");
        }
        return recommendedMenuArray;
    }
    
}

