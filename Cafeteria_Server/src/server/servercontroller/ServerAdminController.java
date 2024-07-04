package server.servercontroller;

import java.util.List;
import org.json.JSONObject;
import server.model.FoodMenu;
import server.service.FoodMenuService;
import server.database.DatabaseException;

public class ServerAdminController {
    FoodMenuService foodMenuService = new FoodMenuService();

    public JSONObject handleAdminActions(JSONObject jsonRequest) {
        String adminAction = jsonRequest.getString("adminAction");
        JSONObject jsonResponse = new JSONObject();

        try {
            switch (adminAction) {
                case "VIEW_MENU_ITEMS":
                    List<FoodMenu> menuItems = foodMenuService.fetchFoodMenuItems();
                    jsonResponse.put("success", true);
                    jsonResponse.put("menu", menuItems);
                    break;
                case "ADD_MENU_ITEM":
                    String foodId = jsonRequest.getString("foodId");
                    String name = jsonRequest.getString("name");
                    int price = jsonRequest.getInt("price");
                    foodMenuService.addFoodMenuItem(new FoodMenu(foodId, name, price));
                    jsonResponse.put("success", true);
                    break;
                case "UPDATE_MENU_ITEM":
                    foodId = jsonRequest.getString("foodId");
                    name = jsonRequest.getString("name");
                    price = jsonRequest.getInt("price");
                    foodMenuService.updateFoodMenuItem(new FoodMenu(foodId, name, price));
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
