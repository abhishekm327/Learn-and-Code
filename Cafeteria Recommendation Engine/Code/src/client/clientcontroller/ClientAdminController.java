package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

import client.utils.ConsoleUtils;

public class ClientAdminController {
	
	static void handleAdminActions(PrintWriter writer, BufferedReader reader) throws IOException {
		
		String foodId;
		String foodName;
		int price;
		boolean exit = false;
        while (!exit) {
        System.out.println("Admin Actions:");
        System.out.println("1. View Main Menu Item");
        System.out.println("2. Add Menu Item");
        System.out.println("3. Update Menu Item");
        System.out.println("4. Delete Menu Item");
        System.out.println("5. Logout");

        int choice = ConsoleUtils.getIntInput("Enter your choice: ");

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("action", "ADMIN_ACTION");

        switch (choice) {
        	case 1:
        		jsonRequest.put("adminAction", "VIEW_MENU_ITEMS");
                break;
        	case 2:
        		foodId = ConsoleUtils.getStringInput("Enter Food Id: ");
                foodName = ConsoleUtils.getStringInput("Enter Food name: ");
                price = ConsoleUtils.getIntInput("Enter Food price: ");
                jsonRequest.put("adminAction", "ADD_MENU_ITEM");
                jsonRequest.put("foodId", foodId);
                jsonRequest.put("name", foodName);
                jsonRequest.put("price", price);
                break;
            case 3:
            	foodId = ConsoleUtils.getStringInput("Enter Food Id: ");
                foodName = ConsoleUtils.getStringInput("Enter Food name: ");
                price = ConsoleUtils.getIntInput("Enter Food price: ");
                jsonRequest.put("adminAction", "UPDATE_MENU_ITEM");
                jsonRequest.put("foodId", foodId);
                jsonRequest.put("name", foodName);
                jsonRequest.put("price", price);
                break;
            case 4:
            	foodId = ConsoleUtils.getStringInput("Enter Food ID: ");
                jsonRequest.put("adminAction", "DELETE_MENU_ITEM");
                jsonRequest.put("foodId", foodId);
                break;
            case 5 :
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
        	else if(choice == 2) {
        		System.out.println("Successfully added food item to menu");
        	}
        	else if(choice == 3) {
        		System.out.println("Successfully Updated food item in menu");
        	}
        	else if(choice == 4) {
        		System.out.println("Successfully Deleted food item in menu");
        	}
            
        } else {
            System.out.println("Action failed: " + jsonResponse.getString("error"));
        }
      }
    }
}
