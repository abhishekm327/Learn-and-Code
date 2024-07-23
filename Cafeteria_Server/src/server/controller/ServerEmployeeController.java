package server.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import server.database.exception.DatabaseException;
import server.enums.ActionType;
import server.model.Notification;
import server.model.RolloutMenu;
import server.service.FeedbackService;
import server.service.FoodMenuService;
import server.service.NotificationService;
import server.service.ProfileService;
import server.service.RolloutMenuService;

public class ServerEmployeeController {
	private static RolloutMenuService rolloutMenuService = new RolloutMenuService();
	private static FeedbackService feedbackService = new FeedbackService();
	private static FoodMenuService foodMenuService = new FoodMenuService();
	private static NotificationService notificationService = new NotificationService();
	private static ProfileService profileService = new ProfileService();

	public JSONObject handleEmployeeActions(JSONObject jsonRequest) {
		String employeeActionStr = jsonRequest.getString("employeeAction");
		ActionType employeeAction = ActionType.fromString(employeeActionStr);
		JSONObject jsonResponse = new JSONObject();

		try {
			switch (employeeAction) {
			case VIEW_MENU_ITEMS:
				JSONArray menuArray = foodMenuService.fetchFoodMenuItems();
				jsonResponse.put("success", true);
				jsonResponse.put("menu", menuArray);
				break;
			case VIEW_NOTIFICATIONS:
				List<Notification> notifications = notificationService.getNotification();
				jsonResponse.put("success", true);
				jsonResponse.put("notifications", notifications);
				break;
			case VIEW_ROLLOUT_MENU:
				String userId = jsonRequest.getString("userId");
				List<RolloutMenu> rolloutItems = rolloutMenuService.viewRolloutItemsforSpecificUserProfile(userId);
				jsonResponse.put("success", true);
				jsonResponse.put("rolloutMenu", new JSONArray(rolloutItems));
				break;
			case VOTE_ROLLOUT_ITEMS:
				String foodId = jsonRequest.getString("foodId");
				rolloutMenuService.voteForRolloutMenuItem(foodId);
				jsonResponse.put("success", true);
				break;
			case PROVIDE_FEEDBACK:
				feedbackService.provideFeedback(jsonRequest);
				jsonResponse.put("success", true);
				break;
			case UPDATE_PROFILE:
				profileService.updateProfile(jsonRequest);
				jsonResponse.put("success", true);
				break;
			case DISCARD_MENU_FEEDBACK:
				feedbackService.provideFeedbackForDiscardMenu(jsonRequest);
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
