package server.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.enums.ActionType;
import server.model.FoodMenu;
import server.model.Notification;
import server.service.FoodMenuService;
import server.service.NotificationService;

public class ServerAdminController {
	private static FoodMenuService foodMenuService = new FoodMenuService();
	private static NotificationService notificationService = new NotificationService();

	public JSONObject handleAdminActions(JSONObject jsonRequest) {
		String adminActionStr = jsonRequest.getString("adminAction");
		ActionType adminAction = ActionType.fromString(adminActionStr);
		JSONObject jsonResponse = new JSONObject();

		try {
			switch (adminAction) {
			case VIEW_MENU_ITEMS:
				JSONArray menuArray = foodMenuService.fetchFoodMenuItems();
				jsonResponse.put("success", true);
				jsonResponse.put("menu", menuArray);
				break;
			case ADD_MENU_ITEM:
				addMenuItem(jsonRequest, jsonResponse);
				break;
			case UPDATE_MENU_ITEM:
				updateMenuItem(jsonRequest, jsonResponse);
				break;
			case DELETE_MENU_ITEM:
				String foodId = jsonRequest.getString("foodId");
				foodMenuService.deleteFoodMenuItem(foodId);
				jsonResponse.put("success", true);
				break;
			case VIEW_DISCARD_MENU_NOTIFICATIONS:
				List<Notification> notifications = notificationService.getDiscardMenuNotification();
				jsonResponse.put("success", true);
				jsonResponse.put("notifications", notifications);
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

	private static void addMenuItem(JSONObject jsonRequest, JSONObject jsonResponse) throws DatabaseException {
		String foodId = jsonRequest.getString("foodId");
		String name = jsonRequest.getString("name");
		int price = jsonRequest.getInt("price");
		String foodType = jsonRequest.getString("foodType");
		String foodStyle = jsonRequest.getString("foodStyle");
		String spiceLevel = jsonRequest.getString("spiceLevel");
		String sweet = jsonRequest.getString("sweet");
		foodMenuService.addFoodMenuItem(new FoodMenu(foodId, name, price, foodType, foodStyle, spiceLevel, sweet));
		jsonResponse.put("success", true);
	}

	private static void updateMenuItem(JSONObject jsonRequest, JSONObject jsonResponse) throws DatabaseException {
		String foodId = jsonRequest.getString("foodId");
		String name = jsonRequest.getString("name");
		int price = jsonRequest.getInt("price");
		String foodType = jsonRequest.getString("foodType");
		String foodStyle = jsonRequest.getString("foodStyle");
		String spiceLevel = jsonRequest.getString("spiceLevel");
		String sweet = jsonRequest.getString("sweet");
		foodMenuService.updateFoodMenuItem(new FoodMenu(foodId, name, price, foodType, foodStyle, spiceLevel, sweet));
		jsonResponse.put("success", true);
	}
}
