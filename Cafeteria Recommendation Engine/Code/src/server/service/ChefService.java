package server.service;

import server.model.FoodMenu;
import server.model.RolloutMenu;
import server.model.Feedback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class ChefService {
    FoodMenuOperations foodMenuOperations = new FoodMenuOperations();
    RecommendedMenuOperations recommendedMenuOperations = new RecommendedMenuOperations();
    RolloutMenuOperations rolloutMenuOperations = new RolloutMenuOperations();
    FeedbackOperations feedbackOperations = new FeedbackOperations();
    ReportOperations reportOperations = new ReportOperations();


    public JSONObject handleChefActions(JSONObject jsonRequest) {
        String chefAction = jsonRequest.getString("chefAction");
        JSONObject jsonResponse = new JSONObject();

        switch (chefAction) {
    		case "VIEW_MENU_ITEMS":
    			List<FoodMenu> menuItems = foodMenuOperations.fetchFoodMenuItems();
    			jsonResponse.put("success", true);
    			jsonResponse.put("menu", menuItems);
    			break;
            case "GENERATE_RECOMMENDED_MENU":
                boolean recommendedMenuGenerated = recommendedMenuOperations.generateRecommendedMenu();
                jsonResponse.put("success", recommendedMenuGenerated);
                break;
            case "VIEW_RECOMMENDED_MENU":
                JSONArray recommendedMenuArray = recommendedMenuOperations.getRecommendedMenu();
                jsonResponse.put("success", true);
                jsonResponse.put("recommendedMenu", recommendedMenuArray);
                break;
            case "ADD_TO_ROLLOUT_MENU":
            	JSONArray items = jsonRequest.getJSONArray("items");
            	String cookingDate = jsonRequest.getString("cookingDate");
                boolean rolloutMenuUpdated = rolloutMenuOperations.addItemsToRolloutMenu(items, cookingDate);
                jsonResponse.put("success", rolloutMenuUpdated);
                break;
            case "VIEW_ROLLOUT_MENU":
            	List<RolloutMenu> rolloutItems = rolloutMenuOperations.fetchRolloutItems();
                jsonResponse.put("success", true);
                jsonResponse.put("rolloutItems", rolloutItems);
                break;
            case "VIEW_FEEDBACK":
                List<Feedback> feedbackList = feedbackOperations.fetchFeedback();
                jsonResponse.put("success", true);
                jsonResponse.put("feedback", feedbackList);
                break;
            case "GENERATE_REPORT":
            	boolean reportGenerated = reportOperations.generateReport();
                jsonResponse.put("success", reportGenerated);
                break;
            case "VIEW_REPORT":
            	 JSONArray reportArray = reportOperations.getReports();
                 jsonResponse.put("success", true);
                 jsonResponse.put("report", reportArray);
                 break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid chef action");
                break;
        }

        return jsonResponse;
    }
}
