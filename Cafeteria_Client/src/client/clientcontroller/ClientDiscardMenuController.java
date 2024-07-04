package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import client.utils.ConsoleUtils;

public class ClientDiscardMenuController {
	
	 public static void handleDiscardMenuItems(JSONArray discardList, PrintWriter writer, BufferedReader reader) throws IOException {
	        displayDiscardMenuItems(discardList);
	        boolean exit = false;
	        while (!exit) {
	        int choice = getUserChoice();
	        if(choice==3) {
	        	exit=true;
	        	continue;
	        }
	        JSONObject jsonRequest = createJsonRequest(choice);

	        if (jsonRequest != null) {
	            sendRequest(jsonRequest, writer);
	        }
	        handleResponse(reader, choice);
	        }
	    }

	    private static void displayDiscardMenuItems(JSONArray discardList) {
	        System.out.println("Discard Menu Item List:");
	        for (int i = 0; i < discardList.length(); i++) {
	            JSONObject item = discardList.getJSONObject(i);
	            System.out.println("Food Id: " + item.getString("foodId"));
	            System.out.println("Food Item: " + item.getString("foodName"));
	            System.out.println("Rating: " + item.getDouble("rating"));
	            System.out.println("Sentiments: " + item.getJSONArray("comments").join(", "));
	            System.out.println();
	        }
	    }

	    private static int getUserChoice() {
	        System.out.println("Select the Option:");
	        System.out.println("1) Remove the Food Item from Menu List");
	        System.out.println("2) Get Detailed Feedback");
	        System.out.println("3) Exit");

	        return ConsoleUtils.getIntInput("Enter your choice: ");
	    }

	    private static JSONObject createJsonRequest(int choice) {
	        JSONObject jsonRequest = new JSONObject();
	        jsonRequest.put("action", "CHEF_ACTION");

	        switch (choice) {
	            case 1:
	                String foodItemName = ConsoleUtils.getStringInput("Enter the food item name to remove: ");
	                jsonRequest.put("chefAction", "REMOVE_DISCARD_FOOD_ITEM");
	                jsonRequest.put("foodItemName", foodItemName);
	                return jsonRequest;
	            case 2:
	                String feedbackFoodItemName = ConsoleUtils.getStringInput("Enter the food item name to get detailed feedback: ");
	                jsonRequest.put("chefAction", "GET_DETAILED_FEEDBACK");
	                jsonRequest.put("foodItemName", feedbackFoodItemName);
	                return jsonRequest;
	            default:
	                System.out.println("Invalid option.");
	                return null;
	        }
	    }

	    private static void sendRequest(JSONObject jsonRequest, PrintWriter writer) throws IOException {
	        writer.println(jsonRequest.toString());
	    }
	    
	    private static void handleResponse(BufferedReader reader, int choice) throws IOException {
	        JSONObject jsonResponse = new JSONObject(reader.readLine());
	        if (jsonResponse.getBoolean("success")) {
	            System.out.println("Notification sent Successfully");
	        } else {
	            System.out.println("Action failed: " + jsonResponse.getString("error"));
	        }
	    }

}
