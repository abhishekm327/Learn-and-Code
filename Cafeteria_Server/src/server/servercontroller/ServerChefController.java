package server.servercontroller;
 
import server.model.RecommendedMenu;
import server.model.RolloutMenu;
import server.database.DatabaseException;
import server.model.Feedback;
import server.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class ServerChefController {
	private static FoodMenuService foodMenuService = new FoodMenuService();
    private static RecommendedEngine recommendedEngine = new RecommendedEngine();
    private static RolloutMenuService rolloutMenuService = new RolloutMenuService();
    private static FeedbackService feedbackService = new FeedbackService();
    private static DiscardMenuService discardMenuService = new DiscardMenuService();

    public JSONObject handleChefActions(JSONObject jsonRequest) {
        String chefAction = jsonRequest.getString("chefAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (chefAction) {
                case "VIEW_MENU_ITEMS":
                	JSONArray menuArray = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuArray);
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
                    List<RolloutMenu> rolloutItems = rolloutMenuService.viewAllRolloutItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("rolloutItems", rolloutItems);
                    break;
                case "VIEW_FEEDBACK":
                    List<Feedback> feedbackList = feedbackService.viewFeedback();
                    jsonResponse.put("success", true);
                    jsonResponse.put("feedback", feedbackList);
                    break;
                case "VIEW_DISCARD_MENU_ITEMS":
                	viewDiscardMenu(jsonResponse);
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
    
    private static void viewDiscardMenu(JSONObject jsonResponse) throws DatabaseException {
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
    }
}
