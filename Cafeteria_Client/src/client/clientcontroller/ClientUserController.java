package client.clientcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;
import client.utils.ConsoleUtils;

public class ClientUserController {
	
	public static boolean authenticateUser(PrintWriter writer, BufferedReader reader) throws IOException {
 
        String userId = ConsoleUtils.getStringInput("Enter User ID: ");
    
        String password = ConsoleUtils.getStringInput("Enter Password: ");

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("action", "AUTHENTICATION");
        jsonRequest.put("userId", userId);
        jsonRequest.put("password", password);

        writer.println(jsonRequest.toString());

        JSONObject jsonResponse = new JSONObject(reader.readLine());
        boolean success = jsonResponse.getBoolean("success");

        if (success) {
            String role = jsonResponse.getString("role");
            System.out.println("Login successful as " + role);
            processRoleSpecificActions(role, writer, reader, userId);
            return true;
        } else {
            System.out.println("Login failed: " + jsonResponse.getString("error"));
            return false;
        }
    }

    private static void processRoleSpecificActions(String role, PrintWriter writer, BufferedReader reader, String userId) throws IOException {
        switch (role) {
            case "Admin":
            	ClientAdminController.handleAdminActions(writer, reader);
                break;
            case "Chef":
            	ClientChefController.handleChefActions(writer, reader);
                break;
            case "Employee":
            	ClientEmployeeController.handleEmployeeActions(writer, reader, userId);
                break;
            default:
                System.out.println("Invalid role.");
        }
    }

}
