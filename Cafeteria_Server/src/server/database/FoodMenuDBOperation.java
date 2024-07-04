package server.database;

import server.model.FoodMenu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodMenuDBOperation {
    public List<FoodMenu> fetchFoodMenuItems() throws DatabaseException {
        List<FoodMenu> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String foodId = resultSet.getString("food_id");
                String name = resultSet.getString("food_name");
                int price = resultSet.getInt("price");
                menuItems.add(new FoodMenu(foodId, name, price));
            }
        } catch (SQLException e) {
        	throw new DatabaseException("Error while fetching the FoodMenu Items. Please try again later");
        }
        return menuItems;
    }

    public void addFoodMenuItem(FoodMenu item) throws DatabaseException {
        String query = "INSERT INTO menu (food_id, food_name, price) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getFoodId());
            statement.setString(2, item.getName());
            statement.setInt(3, item.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
        	throw new DatabaseException("Error in Adding FoodMenu Item -  This FoodId Already exist");
        }
    }

    public void updateFoodMenuItem(FoodMenu item) throws DatabaseException {
        String query = "UPDATE menu SET food_name = ?, price = ? WHERE food_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getPrice());
            statement.setString(3, item.getFoodId());
            statement.executeUpdate();
        } catch (SQLException e) {
        	throw new DatabaseException("Error in Updating FoodMenu Item. Please try again later");
        }
    }

    public void deleteFoodMenuItem(String foodId) throws DatabaseException  {
        String query = "DELETE FROM menu WHERE food_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, foodId);
            statement.executeUpdate();
        } catch (SQLException e) {
        	throw new DatabaseException("Error in Deleting FoodMenu Item. Please try again later");
        }
    }

}
