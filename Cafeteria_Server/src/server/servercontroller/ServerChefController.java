package server.servercontroller;

import server.model.FoodMenu;
import server.model.RecommendedMenu;
import server.model.RolloutMenu;
import server.database.DatabaseException;
import server.model.Feedback;
import server.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class ServerChefController {
    FoodMenuService foodMenuService = new FoodMenuService();
    RecommendedEngine recommendedEngine = new RecommendedEngine();
    RolloutMenuService rolloutMenuService = new RolloutMenuService();
    FeedbackService feedbackService = new FeedbackService();
    DiscardMenuService discardMenuService = new DiscardMenuService();

    public JSONObject handleChefActions(JSONObject jsonRequest) {
        String chefAction = jsonRequest.getString("chefAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (chefAction) {
                case "VIEW_MENU_ITEMS":
                    List<FoodMenu> menuItems = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuItems);
                    break;
                case "GENERATE_RECOMMENDED_MENU":
                    boolean recommendedMenuGenerated = recommendedEngine.generateRecommendedMenu();
                    jsonResponse.put("success", recommendedMenuGenerated);
                    break;
                case "VIEW_RECOMMENDED_MENU":
                    JSONArray recommendedMenuArray = recommendedEngine.getRecommendedMenu();
                    jsonResponse.put("success", true);
                    jsonResponse.put("recommendedMenu", recommendedMenuArray);
                    break;
                case "ADD_TO_ROLLOUT_MENU":
                    JSONArray items = jsonRequest.getJSONArray("items");
                    String cookingDate = jsonRequest.getString("cookingDate");
                    boolean rolloutMenuUpdated = rolloutMenuService.addItemsToRolloutMenu(items, cookingDate);
                    jsonResponse.put("success", rolloutMenuUpdated);
                    break;
                case "VIEW_ROLLOUT_MENU":
                    List<RolloutMenu> rolloutItems = rolloutMenuService.fetchRolloutItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("rolloutItems", rolloutItems);
                    break;
                case "VIEW_FEEDBACK":
                    List<Feedback> feedbackList = feedbackService.viewFeedback();
                    jsonResponse.put("success", true);
                    jsonResponse.put("feedback", feedbackList);
                    break;
                case "VIEW_DISCARD_MENU_ITEMS":
                    List<RecommendedMenu> discardList = recommendedEngine.getDiscardMenuItems();
                    JSONArray discardArray = new JSONArray();
                    for (RecommendedMenu item : discardList) {
                        JSONObject jsonItem = new JSONObject();
                        jsonItem.put("foodId", item.getFoodId());
                        jsonItem.put("foodName", item.getFoodName());
                        jsonItem.put("rating", item.getAverageRating());
                        jsonItem.put("comments", new JSONArray(item.getComments()));
                        discardArray.put(jsonItem);
                    }
                    jsonResponse.put("success", true);
                    jsonResponse.put("discardList", discardArray);
                    break;
                case "REMOVE_DISCARD_FOOD_ITEM":
                    String foodItemNameToRemove = jsonRequest.getString("foodItemName");
                    discardMenuService.notificationForDiscardMenu(foodItemNameToRemove);
                    jsonResponse.put("success", true);
                    break;
                case "GET_DETAILED_FEEDBACK":
                    String foodItemNameForFeedback = jsonRequest.getString("foodItemName");
                    discardMenuService.askForDetailedFeedback(foodItemNameForFeedback);
                    jsonResponse.put("success", true);
                    break;
                default:
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Invalid chef action");
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
