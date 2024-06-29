package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.json.JSONObject;

import client.utils.ConsoleUtils;

public class ClientUserController {
	
	public static boolean authenticateUser(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("Select your role:");
        System.out.println("1. Admin");
        System.out.println("2. Chef");
        System.out.println("3. Employee");

        int roleChoice = ConsoleUtils.getIntInput("Enter your role: ");
        String role;
        switch (roleChoice) {
            case 1:
                role = "Admin";
                break;
            case 2:
                role = "Chef";
                break;
            case 3:
                role = "Employee";
                break;
            default:
                System.out.println("Invalid role selection.");
                return false;
        }

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("action", "AUTHENTICATION");
        jsonRequest.put("userId", userId);
        jsonRequest.put("password", password);
        jsonRequest.put("role", role);

        writer.println(jsonRequest.toString());

        JSONObject jsonResponse = new JSONObject(reader.readLine());
        boolean success = jsonResponse.getBoolean("success");

        if (success) {
            System.out.println("Login successful as " + role);
            processRoleSpecificActions(role, scanner, writer, reader);
            return true;
        } else {
            System.out.println("Login failed: " + jsonResponse.getString("error"));
            return false;
        }
    }

    private static void processRoleSpecificActions(String role, Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        switch (role) {
            case "Admin":
            	ClientAdminController.handleAdminActions(writer, reader);
                break;
            case "Chef":
            	ClientChefController.handleChefActions(scanner, writer, reader);
                break;
            case "Employee":
            	ClientEmployeeController.handleEmployeeActions(writer, reader);
                break;
            default:
                System.out.println("Invalid role.");
        }
    }

}
