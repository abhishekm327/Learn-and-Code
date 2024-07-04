package server.servercontroller;

import java.util.List;
import org.json.JSONObject;
import server.database.DatabaseException;
import server.model.FoodMenu;
import server.model.Notification;
import server.service.*;

public class ServerEmployeeController {
    RolloutMenuService rolloutMenuService = new RolloutMenuService();
    FeedbackService feedbackService = new FeedbackService();
    FoodMenuService foodMenuService = new FoodMenuService();
    NotificationService notificationService = new NotificationService();

    public JSONObject handleEmployeeActions(JSONObject jsonRequest) {
        String employeeAction = jsonRequest.getString("employeeAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (employeeAction) {
                case "VIEW_MENU_ITEMS":
                    List<FoodMenu> menuItems = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuItems);
                    break;
                case "VIEW_NOTIFICATIONS":
                    List<Notification> notifications = notificationService.getNotification();
                    jsonResponse.put("success", true);
                    jsonResponse.put("notifications", notifications);
                    break;
                case "VIEW_ROLLOUT_MENU":
                    jsonResponse.put("success", true);
                    jsonResponse.put("rolloutMenu", rolloutMenuService.fetchRolloutItems());
                    break;
                case "VOTE_ROLLOUT_ITEMS":
                    String foodId = jsonRequest.getString("foodId");
                    rolloutMenuService.voteForRolloutMenuItem(foodId);
                    jsonResponse.put("success", true);
                    break;
                case "PROVIDE_FEEDBACK":
                    String food_Id = jsonRequest.getString("foodId");
                    String comment = jsonRequest.getString("comment");
                    double rating = jsonRequest.getDouble("rating");
                    feedbackService.provideFeedback(food_Id, comment, rating);
                    jsonResponse.put("success", true);
                    break;
                default:
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Invalid employee action");
                    break;
            }
        } catch (DatabaseException databaseException) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", databaseException.getMessage());
        } catch (Exception exception) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Error is occuring, Please try again later");
        }

        return jsonResponse;
    }
}
