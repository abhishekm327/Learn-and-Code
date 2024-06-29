package server.service;

import server.database.DatabaseConnection;
import server.model.FoodMenu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodMenuOperations {
	NotificationService notificationService = new NotificationService();

    public List<FoodMenu> fetchFoodMenuItems() {
        List<FoodMenu> menuItems = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM menu";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String foodId = resultSet.getString("food_id");
                String name = resultSet.getString("food_name");
                int price = resultSet.getInt("price");
                menuItems.add(new FoodMenu(foodId, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public void addFoodMenuItem(FoodMenu item) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO menu (food_id, food_name, price) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getFoodId());
            statement.setString(2, item.getName());
            statement.setInt(3, item.getPrice());
            statement.executeUpdate();
            String message = "Successfully added new item " +item.getName()+ " to the menu";
            notificationService.sendNotification(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFoodMenuItem(FoodMenu item) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE menu SET food_name = ?, price = ? WHERE food_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getName());
            statement.setInt(2, item.getPrice());
            statement.setString(3, item.getFoodId());
            statement.executeUpdate();
            String message = "Successfully updated the item " +item.getName()+ " in the menu";
            notificationService.sendNotification(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFoodMenuItem(String foodId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM menu WHERE food_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, foodId);
            statement.executeUpdate();
            String message = "Successfully deleted the Fooditem Id is " + foodId + " in the menu";
            notificationService.sendNotification(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
