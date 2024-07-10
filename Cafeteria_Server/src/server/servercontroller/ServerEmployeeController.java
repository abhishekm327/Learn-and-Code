package server.servercontroller;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import server.database.DatabaseException;
import server.model.FoodMenu;
import server.model.Notification;
import server.model.RolloutMenu;
import server.service.*;

public class ServerEmployeeController {
	private static RolloutMenuService rolloutMenuService = new RolloutMenuService();
    private static FeedbackService feedbackService = new FeedbackService();
    private static FoodMenuService foodMenuService = new FoodMenuService();
    private static NotificationService notificationService = new NotificationService();
    private static ProfileService profileService = new ProfileService();

    public JSONObject handleEmployeeActions(JSONObject jsonRequest) {
        String employeeAction = jsonRequest.getString("employeeAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (employeeAction) {
                case "VIEW_MENU_ITEMS":
                	JSONArray menuArray = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuArray);
                    break;
                case "VIEW_NOTIFICATIONS":
                    List<Notification> notifications = notificationService.getNotification();
                    jsonResponse.put("success", true);
                    jsonResponse.put("notifications", notifications);
                    break;
                case "VIEW_ROLLOUT_MENU":
                    String userId = jsonRequest.getString("userId");
                    List<RolloutMenu> rolloutItems = rolloutMenuService.viewRolloutItemsforSpecificUserProfile(userId);
                    jsonResponse.put("success", true);
                    jsonResponse.put("rolloutMenu", new JSONArray(rolloutItems));
                   // jsonResponse.put("success", true);
                   // jsonResponse.put("rolloutMenu", rolloutMenuService.viewRolloutItems());
                    break;
                case "VOTE_ROLLOUT_ITEMS":
                    String foodId = jsonRequest.getString("foodId");
                    rolloutMenuService.voteForRolloutMenuItem(foodId);
                    jsonResponse.put("success", true);
                    break;
                case "PROVIDE_FEEDBACK":
                	getFeedbackDetail(jsonRequest, jsonResponse);
                    break;
                case "UPDATE_PROFILE":
                	getProfileDetail(jsonRequest, jsonResponse);
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
    
    private static void getFeedbackDetail(JSONObject jsonRequest, JSONObject jsonResponse) throws DatabaseException {
        String foodId = jsonRequest.getString("foodId");
        String comment = jsonRequest.getString("comment");
        double rating = jsonRequest.getDouble("rating");
        String userId = jsonRequest.getString("userId");
        feedbackService.provideFeedback(foodId, comment, rating, userId);
        jsonResponse.put("success", true);  	
    }
    
    private static void getProfileDetail(JSONObject jsonRequest, JSONObject jsonResponse) throws DatabaseException {
        String userId = jsonRequest.getString("userId");
        String foodType = jsonRequest.getString("foodType");
        String spiceLevel = jsonRequest.getString("spiceLevel");
        String foodStyle = jsonRequest.getString("foodStyle");
        String sweet = jsonRequest.getString("sweet");
        profileService.updateProfile(userId, foodType, spiceLevel, foodStyle, sweet);
        jsonResponse.put("success", true);
    }
}
