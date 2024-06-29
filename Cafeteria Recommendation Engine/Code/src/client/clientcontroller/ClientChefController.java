package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import client.utils.ConsoleUtils;

public class ClientChefController {
	
	static void handleChefActions(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
		
		boolean exit = false;
        while (!exit) {
        System.out.println("Chef Actions:");
        System.out.println("1. View Menu Items");
        System.out.println("2. Generate Recommend Menu");
        System.out.println("3. View Recommend Menu");
        System.out.println("4. Add Items to Rollout Menu");
        System.out.println("5. View Rollout Menu");
        System.out.println("6. View Feedback");
        System.out.println("7. View Report");
        System.out.println("8. Logout");
        
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
                break;
            case 5:
                jsonRequest.put("chefAction", "VIEW_ROLLOUT_MENU");
                break; 
            case 6:
                jsonRequest.put("chefAction", "VIEW_FEEDBACK");
                break;
            case 7:
                jsonRequest.put("chefAction", "VIEW_REPORT");
                break;
            case 8 :
            	exit = true;
                continue;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        writer.println(jsonRequest.toString());

        JSONObject jsonResponse = new JSONObject(reader.readLine());
        if (jsonResponse.getBoolean("success")) {
            System.out.println("Action successful.");
            if(choice == 1) {
            	ConsoleUtils.printMenuItems(jsonResponse.getJSONArray("menu"));
        	}
            else if (choice == 2) {
            	System.out.println("Successfully Generated Recommended Menu");
            }
            else if (choice == 3) {
            	ConsoleUtils.printRecommendedMenu(jsonResponse.getJSONArray("recommendedMenu"));
            }
            else if (choice == 4) {
            	System.out.println("Successfully Created Rollout Menu and Sent Notification to Employees");
            }
            else if(choice == 5) {
            	ConsoleUtils.printRolloutMenuItems(jsonResponse.getJSONArray("rolloutItems"));
        	}
            else if(choice == 6) {
            	ConsoleUtils.printFeedback(jsonResponse.getJSONArray("feedback"));
        	}
            else if(choice == 7) {
            	ConsoleUtils.printReport(jsonResponse.getJSONArray("report"));
        	}
        } else {
            System.out.println("Action failed: " + jsonResponse.getString("error"));
        }
    }
  }
}
