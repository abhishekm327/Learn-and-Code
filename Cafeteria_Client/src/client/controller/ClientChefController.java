package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import client.utils.ConsoleUtils;

public class ClientChefController {

	static void handleChefActions(PrintWriter writer, BufferedReader reader) throws IOException {
		boolean exit = false;
		while (!exit) {
			displayMenu();
			int choice = ConsoleUtils.getIntInput("Enter your choice: ");

			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("action", "CHEF_ACTION");

			switch (choice) {
			case 1:
				jsonRequest.put("chefAction", "VIEW_MENU_ITEMS");
				break;
			case 2:
				jsonRequest.put("chefAction", "GENERATE_RECOMMENDED_MENU");
				break;
			case 3:
				jsonRequest.put("chefAction", "VIEW_RECOMMENDED_MENU");
				break;
			case 4:
				addToRolloutmenu(jsonRequest);
				break;
			case 5:
				jsonRequest.put("chefAction", "VIEW_ROLLOUT_MENU");
				break;
			case 6:
				jsonRequest.put("chefAction", "VIEW_FEEDBACK");
				break;
			case 7:
				jsonRequest.put("chefAction", "VIEW_DISCARD_MENU_ITEMS");
				break;
			case 8:
				exit = true;
				continue;
			default:
				System.out.println("Invalid choice.");
				continue;
			}
			sendRequest(writer, jsonRequest);
			handleResponse(writer, reader, choice);
		}
	}

	private static void displayMenu() {
		System.out.println("Chef Actions:");
		System.out.println("1. View Menu Items");
		System.out.println("2. Generate Recommend Menu");
		System.out.println("3. View Recommend Menu");
		System.out.println("4. Add Items to Rollout Menu");
		System.out.println("5. View Rollout Menu");
		System.out.println("6. View Feedback");
		System.out.println("7. View Discard Menu Items");
		System.out.println("8. Logout");
	}

	private static void sendRequest(PrintWriter writer, JSONObject jsonRequest) {
		writer.println(jsonRequest.toString());
	}

	private static void handleResponse(PrintWriter writer, BufferedReader reader, int choice) throws IOException {
		JSONObject jsonResponse = new JSONObject(reader.readLine());
		if (jsonResponse.getBoolean("success")) {
			System.out.println("Action successful.");
			if (choice == 1) {
				ConsoleUtils.printMenuItems(jsonResponse.getJSONArray("menu"));
			} else if (choice == 2) {
				System.out.println("Successfully generated recommended menu");
			} else if (choice == 3) {
				ConsoleUtils.printRecommendedMenu(jsonResponse.getJSONArray("recommendedMenu"));
			} else if (choice == 4) {
				System.out.println("Successfully created rollout menu and sent notification to employees");
			} else if (choice == 5) {
				ConsoleUtils.printRolloutMenuItems(jsonResponse.getJSONArray("rolloutItems"));
			} else if (choice == 6) {
				ConsoleUtils.printFeedback(jsonResponse.getJSONArray("feedback"));
			} else if (choice == 7) {
				ClientDiscardMenuController.handleDiscardMenuItems(jsonResponse.getJSONArray("discardList"), writer,
						reader);
			}
		} else {
			System.out.println("Action failed: " + jsonResponse.getString("error"));
		}
	}

	private static JSONObject addToRolloutmenu(JSONObject jsonRequest) {
		jsonRequest.put("chefAction", "ADD_TO_ROLLOUT_MENU");
		int itemCount = ConsoleUtils.getIntInput("Enter number of items to recommend: ");
		List<JSONObject> items = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			JSONObject item = new JSONObject();
			String foodId = ConsoleUtils.getStringInput("Enter Food Id: ");
			item.put("foodId", foodId);
			items.add(item);
		}
		jsonRequest.put("items", new JSONArray(items));
		String cookingDate = ConsoleUtils.getStringInput("Enter Cooking Date (DD/MM/YYYY): ");
		jsonRequest.put("cookingDate", cookingDate);
		return jsonRequest;
	}
}
