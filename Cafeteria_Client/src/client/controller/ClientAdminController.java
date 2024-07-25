package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import client.utils.ConsoleUtils;

public class ClientAdminController {

	static void handleAdminActions(PrintWriter writer, BufferedReader reader) throws IOException {
		boolean exit = false;
		while (!exit) {
			displayMenu();
			int choice = ConsoleUtils.getIntInput("Enter your choice: ");

			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("action", "ADMIN_ACTION");

			switch (choice) {
			case 1:
				jsonRequest.put("adminAction", "VIEW_MENU_ITEMS");
				break;
			case 2:
				addMenuItem(jsonRequest);
				break;
			case 3:
				updateMenuItem(jsonRequest);
				break;
			case 4:
				deleteMenuItem(jsonRequest);
				break;
			case 5:
				jsonRequest.put("adminAction", "VIEW_DISCARD_MENU_NOTIFICATIONS");
				break;
			case 6:
				exit = true;
				continue;
			default:
				System.out.println("Invalid choice.");
				continue;
			}
			sendRequest(writer, jsonRequest);
			handleResponse(reader, choice);
		}
	}

	private static void displayMenu() {
		System.out.println("Admin Actions:");
		System.out.println("1. View Main Menu Item");
		System.out.println("2. Add Menu Item");
		System.out.println("3. Update Menu Item");
		System.out.println("4. Delete Menu Item");
		System.out.println("5. View Discard Menu Notification");
		System.out.println("6. Logout");
	}

	private static void sendRequest(PrintWriter writer, JSONObject jsonRequest) {
		writer.println(jsonRequest.toString());
	}

	private static void handleResponse(BufferedReader reader, int choice) throws IOException {
		JSONObject jsonResponse = new JSONObject(reader.readLine());
		if (jsonResponse.getBoolean("success")) {
			System.out.println("Action successful.");
			if (choice == 1) {
				ConsoleUtils.printMenuItems(jsonResponse.getJSONArray("menu"));
			} else if (choice == 2) {
				System.out.println("Successfully added food item to menu");
				System.out.println("-----------------------------------------------------");
			} else if (choice == 3) {
				System.out.println("Successfully updated food item in menu");
				System.out.println("-----------------------------------------------------");
			} else if (choice == 4) {
				System.out.println("Successfully deleted food item in menu");
				System.out.println("-----------------------------------------------------");
			} else if (choice == 5) {
				ConsoleUtils.printNotifications(jsonResponse.getJSONArray("notifications"));
			}
		} else {
			System.out.println("Action failed: " + jsonResponse.getString("error"));
			System.out.println("-----------------------------------------------------------");
		}
	}

	private static JSONObject addMenuItem(JSONObject jsonRequest) {
		return gatherFoodItemDetails(jsonRequest, "ADD_MENU_ITEM");
	}

	private static JSONObject updateMenuItem(JSONObject jsonRequest) {
		return gatherFoodItemDetails(jsonRequest, "UPDATE_MENU_ITEM");
	}

	private static JSONObject deleteMenuItem(JSONObject jsonRequest) {
		String foodId = ConsoleUtils.getStringInput("Enter Food ID: ");
		jsonRequest.put("adminAction", "DELETE_MENU_ITEM");
		jsonRequest.put("foodId", foodId);
		return jsonRequest;
	}

	private static JSONObject gatherFoodItemDetails(JSONObject jsonRequest, String adminAction) {
		String foodId = ConsoleUtils.getStringInput("Enter Food Id: ");
		String foodName = ConsoleUtils.getStringInput("Enter Food name: ");
		int price = ConsoleUtils.getIntInput("Enter Food price: ");

		String foodType = ConsoleUtils.getOptionInput("Please select the Food Type:",
				new String[] { "Vegetarian", "Non-Vegetarian", "Eggetarian" });

		String spiceLevel = ConsoleUtils.getOptionInput("Please select the Spice Level:",
				new String[] { "High", "Medium", "Low" });

		String foodStyle = ConsoleUtils.getOptionInput("Please select the Food Style:",
				new String[] { "North Indian", "South Indian", "Other" });

		String sweet = ConsoleUtils.getOptionInput("Is it Sweet?", new String[] { "Yes", "No" });

		jsonRequest.put("adminAction", adminAction);
		jsonRequest.put("foodId", foodId);
		jsonRequest.put("name", foodName);
		jsonRequest.put("price", price);
		jsonRequest.put("foodType", foodType);
		jsonRequest.put("spiceLevel", spiceLevel);
		jsonRequest.put("foodStyle", foodStyle);
		jsonRequest.put("sweet", sweet);
		return jsonRequest;
	}
}
