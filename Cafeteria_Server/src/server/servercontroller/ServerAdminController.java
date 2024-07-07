package server.servercontroller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import server.model.FoodMenu;
import server.service.FoodMenuService;
import server.database.DatabaseException;

public class ServerAdminController {
    FoodMenuService foodMenuService = new FoodMenuService();

    public JSONObject handleAdminActions(JSONObject jsonRequest) {
        String foodId;
        String name;
        int price;
        String foodType;
        String foodStyle;
        String spiceLevel;
        String sweet;
        
        String adminAction = jsonRequest.getString("adminAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (adminAction) {
                case "VIEW_MENU_ITEMS":
                	JSONArray menuArray = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuArray);
                    break;
                case "ADD_MENU_ITEM":
                    foodId = jsonRequest.getString("foodId");
                    name = jsonRequest.getString("name");
                    price = jsonRequest.getInt("price");
                    foodType = jsonRequest.getString("foodType");
                    foodStyle = jsonRequest.getString("foodStyle");
                    spiceLevel = jsonRequest.getString("spiceLevel");
                    sweet = jsonRequest.getString("sweet");
                    foodMenuService.addFoodMenuItem(new FoodMenu(foodId, name, price, foodType, foodStyle, spiceLevel, sweet));
                    jsonResponse.put("success", true);
                    break;
                case "UPDATE_MENU_ITEM":
                    foodId = jsonRequest.getString("foodId");
                    name = jsonRequest.getString("name");
                    price = jsonRequest.getInt("price");
                    foodType = jsonRequest.getString("foodType");
                    foodStyle = jsonRequest.getString("foodStyle");
                    spiceLevel = jsonRequest.getString("spiceLevel");
                    sweet = jsonRequest.getString("sweet");
                    foodMenuService.updateFoodMenuItem(new FoodMenu(foodId, name, price, foodType, foodStyle, spiceLevel, sweet));
                    jsonResponse.put("success", true);
                    break;
                case "DELETE_MENU_ITEM":
                    foodId = jsonRequest.getString("foodId");
                    foodMenuService.deleteFoodMenuItem(foodId);
                    jsonResponse.put("success", true);
                    break;
                default:
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Invalid admin action");
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
