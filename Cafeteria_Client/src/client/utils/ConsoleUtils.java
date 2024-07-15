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
		System.out.println("----------------------------------------------------");
		System.out.printf("%-10s %-25s %-10s%n", "FoodID", "Name", "Price");
		System.out.println("----------------------------------------------------");

		for (int i = 0; i < menuItems.length(); i++) {
			JSONObject item = menuItems.getJSONObject(i);
			System.out.format("%-10s %-25s %-10d%n", item.getString("foodId"), item.getString("name"),
					item.getInt("price"));
		}
		System.out.println("----------------------------------------------------");
	}

	public static void printFeedback(JSONArray feedbackList) {
		System.out.println(
				"------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-10s %-20s %-35s %-10s %-15s %-10s%n", "FoodID", "Name", "Comment", "Rating", "Date",
				"UserID");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------");

		for (int i = 0; i < feedbackList.length(); i++) {
			JSONObject feedback = feedbackList.getJSONObject(i);
			System.out.format("%-10s %-20s %-35s %-10.1f %-15s %-10s%n", feedback.getString("foodId"),
					feedback.getString("foodName"), feedback.getString("comment"), feedback.getDouble("rating"),
					feedback.getString("feedbackDate"), feedback.getString("userId"));
		}
		System.out.println(
				"------------------------------------------------------------------------------------------------------------");
	}

	public static void printRecommendedMenu(JSONArray recommendedMenuList) {
		System.out.println("-------------------------------------------------------------------------");
		System.out.printf("%-10s %-25s %-10s %-25s%n", "FoodID", "Name", "Rating", "Comments");
		System.out.println("-------------------------------------------------------------------------");

		for (int i = 0; i < recommendedMenuList.length(); i++) {
			JSONObject menu = recommendedMenuList.getJSONObject(i);
			System.out.format("%-10s %-25s %-10.1f %-25s%n", menu.getString("foodId"), menu.getString("foodName"),
					menu.getDouble("rating"), menu.getString("comment"));
		}
		System.out.println("-------------------------------------------------------------------------");
	}

	public static void printRolloutMenuItems(JSONArray rolloutItems) {
		System.out.println("--------------------------------------------------------------------------------");
		System.out.printf("%-10s %-25s %-20s %-10s%n", "FoodID", "Name", "Cooking Date", "Vote");
		System.out.println("--------------------------------------------------------------------------------");

		for (int i = 0; i < rolloutItems.length(); i++) {
			JSONObject item = rolloutItems.getJSONObject(i);
			System.out.format("%-10s %-25s %-20s %-10d%n", item.getString("foodId"), item.getString("name"),
					item.getString("cookingDate"), item.getInt("vote"));
		}
		System.out.println("--------------------------------------------------------------------------------");
	}

	public static void printNotifications(JSONArray notificationList) {
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.printf("%-60s %-25s%n", "Message", "Date");
		System.out.println("-----------------------------------------------------------------------------------");

		for (int i = 0; i < notificationList.length(); i++) {
			JSONObject notification = notificationList.getJSONObject(i);
			System.out.format("%-60s %-25s%n", notification.getString("message"),
					notification.getString("notificationDate"));
			System.out.println("-----------------------------------------------------------------------------------");
		}
	}

	public static void displayDiscardMenuItems(JSONArray discardList) {
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.printf("%-10s %-25s %-10s %-30s%n", "FoodID", "Name", "Rating", "Sentiments");
		System.out.println("----------------------------------------------------------------------------------------");

		for (int i = 0; i < discardList.length(); i++) {
			JSONObject item = discardList.getJSONObject(i);
			String sentiments = item.getJSONArray("comments").join(", ");
			System.out.format("%-10s %-25s %-10.1f %-30s%n", item.getString("foodId"), item.getString("foodName"),
					item.getDouble("rating"), sentiments);
		}
		System.out.println("----------------------------------------------------------------------------------------");
	}
}
