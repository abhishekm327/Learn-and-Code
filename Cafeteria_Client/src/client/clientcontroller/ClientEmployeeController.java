package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;
import client.utils.ConsoleUtils;

public class ClientEmployeeController {

    static void handleEmployeeActions(PrintWriter writer, BufferedReader reader) throws IOException {
        boolean exit = false;
        while (!exit) {
        	displayMenu();
            int choice = ConsoleUtils.getIntInput("Enter your choice: ");

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("action", "EMPLOYEE_ACTION");

            switch (choice) {
                case 1:
                    jsonRequest.put("employeeAction", "VIEW_MENU_ITEMS");
                    break;
                case 2:
                    jsonRequest.put("employeeAction", "VIEW_NOTIFICATIONS");
                    break;
                case 3:
                    jsonRequest.put("employeeAction", "VIEW_ROLLOUT_MENU");
                    break;
                case 4:
                    jsonRequest.put("employeeAction", "VOTE_ROLLOUT_ITEMS");
                    String foodId = ConsoleUtils.getStringInput("Enter Food Id: ");
                    jsonRequest.put("foodId", foodId);
                    break;
                case 5:
                    jsonRequest.put("employeeAction", "PROVIDE_FEEDBACK");
                    String food_Id = ConsoleUtils.getStringInput("Enter Food Id: ");
                    String comment = ConsoleUtils.getStringInput("Enter your comment: ");
                    double rating = ConsoleUtils.getRatingInput("Enter Rating (1-5): ");
                    jsonRequest.put("foodId", food_Id);
                    jsonRequest.put("comment", comment);
                    jsonRequest.put("rating", rating);
                    break;
                case 6:
                    exit = true;
                    continue;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            sendRequest(writer, jsonRequest);
            handleResponse(reader, choice);
        }
    }

      private static void displayMenu() {
                System.out.println("Employee Actions:");
                System.out.println("1. View Main Menu");
                System.out.println("2. View Notifications");
                System.out.println("3. View Rollout Menu");
                System.out.println("4. Vote for Rollout Items");
                System.out.println("5. Provide Feedback");
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
                    ConsoleUtils.printNotifications(jsonResponse.getJSONArray("notifications"));
                } else if (choice == 3) {
                    ConsoleUtils.printRolloutMenuItems(jsonResponse.getJSONArray("rolloutMenu"));
                } else if (choice == 4) {
                    System.out.println("Thank you for your vote");
                } else if (choice == 5) {
                    System.out.println("Thank you for your feedback");
                }
            } else {
                System.out.println("Action failed: " + jsonResponse.getString("error"));
            }
      }
}
