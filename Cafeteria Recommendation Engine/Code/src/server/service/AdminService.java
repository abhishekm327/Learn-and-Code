package server.service;

import java.util.List;

import org.json.JSONObject;

import server.model.FoodMenu;
import utils.ConsoleUtils;

public class AdminService {
    private final FoodMenuOperations foodMenuOperations = new FoodMenuOperations();

    public JSONObject handleAdminActions(JSONObject jsonRequest) {
        String adminAction = jsonRequest.getString("adminAction");
        JSONObject jsonResponse = new JSONObject();

        switch (adminAction) {
        	case "VIEW_MENU_ITEMS":
        		List<FoodMenu> menuItems = foodMenuOperations.fetchFoodMenuItems();
        		jsonResponse.put("success", true);
        		jsonResponse.put("menu", menuItems);
            break;
        
            case "ADD_MENU_ITEM":
                String foodId = jsonRequest.getString("foodId");
                String name = jsonRequest.getString("name");
                int price = jsonRequest.getInt("price");
                foodMenuOperations.addFoodMenuItem(new FoodMenu(foodId, name, price));
                jsonResponse.put("success", true);
                break;

            case "UPDATE_MENU_ITEM":
                foodId = jsonRequest.getString("foodId");
                name = jsonRequest.getString("name");
                price = jsonRequest.getInt("price");
                foodMenuOperations.updateFoodMenuItem(new FoodMenu(foodId, name, price));
                jsonResponse.put("success", true);
                break;

            case "DELETE_MENU_ITEM":
                foodId = jsonRequest.getString("foodId");
                foodMenuOperations.deleteFoodMenuItem(foodId);
                jsonResponse.put("success", true);
                break;

            default:
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid admin action");
                break;
        }

        return jsonResponse;
    }
}
