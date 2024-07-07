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
    RolloutMenuService rolloutMenuService = new RolloutMenuService();
    FeedbackService feedbackService = new FeedbackService();
    FoodMenuService foodMenuService = new FoodMenuService();
    NotificationService notificationService = new NotificationService();
    ProfileService profileService = new ProfileService();

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
                    String food_Id = jsonRequest.getString("foodId");
                    String comment = jsonRequest.getString("comment");
                    double rating = jsonRequest.getDouble("rating");
                    feedbackService.provideFeedback(food_Id, comment, rating);
                    jsonResponse.put("success", true);
                    break;
                case "UPDATE_PROFILE":
                    String user_Id = jsonRequest.getString("userId");
                    String foodType = jsonRequest.getString("foodType");
                    String spiceLevel = jsonRequest.getString("spiceLevel");
                    String foodStyle = jsonRequest.getString("foodStyle");
                    String sweet = jsonRequest.getString("sweet");
                    profileService.updateProfile(user_Id, foodType, spiceLevel, foodStyle, sweet);
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
