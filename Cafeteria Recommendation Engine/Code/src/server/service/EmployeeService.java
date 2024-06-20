package server.service;

import java.util.List;

import org.json.JSONObject;

import server.model.FoodMenu;
import server.model.Notification;
import utils.ConsoleUtils;

public class EmployeeService {
    RolloutMenuOperations rolloutMenuOperations = new RolloutMenuOperations();
    FeedbackOperations feedbackOperations = new FeedbackOperations();
    FoodMenuOperations foodMenuOperations = new FoodMenuOperations();
    NotificationService notificationService = new NotificationService();
    
    public JSONObject handleEmployeeActions(JSONObject jsonRequest) {
        String employeeAction = jsonRequest.getString("employeeAction");
        JSONObject jsonResponse = new JSONObject();

        switch (employeeAction) {
        	case "VIEW_MENU_ITEMS":
        		List<FoodMenu> menuItems = foodMenuOperations.fetchFoodMenuItems();
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
                jsonResponse.put("rolloutMenu", rolloutMenuOperations.fetchRolloutItems());
                break;
            case "VOTE_ROLLOUT_ITEMS":
                String foodId = jsonRequest.getString("foodId");
                rolloutMenuOperations.voteForRolloutMenuItem(foodId);
                jsonResponse.put("success", true);
                break;
            case "PROVIDE_FEEDBACK":
            	String food_Id = jsonRequest.getString("foodId");
                String comment = jsonRequest.getString("comment");
                double rating = jsonRequest.getDouble("rating");
                feedbackOperations.provideFeedback(food_Id, comment, rating);
                jsonResponse.put("success", true);
                break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid employee action");
                break;
        }

        return jsonResponse;
    }
}
