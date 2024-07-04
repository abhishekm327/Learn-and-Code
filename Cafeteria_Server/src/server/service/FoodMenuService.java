package server.service;

import server.database.DatabaseException;
import server.database.FoodMenuDBOperation;
import server.model.FoodMenu;
import java.util.List;

public class FoodMenuService {
	FoodMenuDBOperation foodMenuDBOperation = new FoodMenuDBOperation();
	NotificationService notificationService = new NotificationService();

    public List<FoodMenu> fetchFoodMenuItems() throws DatabaseException {
        return foodMenuDBOperation .fetchFoodMenuItems();
    }

    public void addFoodMenuItem(FoodMenu item) throws DatabaseException {
    	foodMenuDBOperation.addFoodMenuItem(item);
        String message = "Successfully added new item " + item.getName() + " to the menu";
        notificationService.sendNotification(message);
    }

    public void updateFoodMenuItem(FoodMenu item) throws DatabaseException {
    	foodMenuDBOperation.updateFoodMenuItem(item);
        String message = "Successfully updated the item " + item.getName() + " in the menu";
        notificationService.sendNotification(message);
    }

    public void deleteFoodMenuItem(String foodId) throws DatabaseException {
    	foodMenuDBOperation.deleteFoodMenuItem(foodId);
        String message = "Successfully deleted the Fooditem Id is " + foodId + " in the menu";
        notificationService.sendNotification(message);
    }
}
