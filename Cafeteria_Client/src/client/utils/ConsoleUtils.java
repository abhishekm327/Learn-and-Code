package client.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return input;
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static double getDoubleInput(String prompt) {
        double input = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextDouble();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return input;
    }

    public static double getRatingInput(String prompt) {
        double rating;
        while (true) {
            rating = getDoubleInput(prompt);
            if (rating >= 1 && rating <= 5) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
            }
        }
        return rating;
    }
    
    public static String getOptionInput(String prompt, String[] options) {
        while (true) {
            System.out.println(prompt);
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            int choice = getIntInput("Enter your choice: ");
            if (choice > 0 && choice <= options.length) {
                return options[choice - 1];
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
	public static void printMenuItems(JSONArray menuItems) {
        System.out.println("Menu Items:");
        for (int i = 0; i < menuItems.length(); i++) {
            JSONObject item = menuItems.getJSONObject(i);
            System.out.println("FoodID: " + item.getString("foodId") + 
            					", Name: " + item.getString("name") + 
            					", Price: " + item.getInt("price"));
        }
    }
	
	public static void printFeedback(JSONArray feedbackList) {
        System.out.println("Feedback List:");
        for (int i = 0; i < feedbackList.length(); i++) {
            JSONObject feedback = feedbackList.getJSONObject(i);
            System.out.println("FoodID: " + feedback.getString("foodId") + 
            					", Name: " + feedback.getString("foodName") + 
            					", Comment: " + feedback.getString("comment") + 
            					", Rating: " + feedback.getDouble("rating") +
            					", Date: " + feedback.getString("feedbackDate"));
        }
    }
	
	public static void printRecommendedMenu(JSONArray recommendedMenuList) {
	    System.out.println("Recommended Menu:");
	    for (int i = 0; i < recommendedMenuList.length(); i++) {
	        JSONObject menu = recommendedMenuList.getJSONObject(i);
	        System.out.println("FoodID: " + menu.getString("foodId") +
	                           ", Name: " + menu.getString("foodName") +
	                           ", Rating: " + menu.getDouble("rating") );
	    }
	}
	
	public static void printRolloutMenuItems(JSONArray rolloutItems) {
        System.out.println("Rollout Items:");
        for (int i = 0; i < rolloutItems.length(); i++) {
            JSONObject item = rolloutItems.getJSONObject(i);
            System.out.println("FoodID: " + item.getString("foodId") + 
            					", Name: " + item.getString("name") + 
            					", Cooking Date: " + item.getString("cookingDate") + 
            					", Rating: " + item.getDouble("rating") +
            					", Vote: "+item.getInt("vote"));
        }
    }
	
	public static void printNotifications(JSONArray notificationList) {
	    System.out.println("Notifications:");
	    for (int i = 0; i < notificationList.length(); i++) {
	        JSONObject notification = notificationList.getJSONObject(i);
	        System.out.println("Message: " + notification.getString("message") + 
	                           ",  Date: " + notification.getString("notificationDate"));
	    }
	}
}
	
