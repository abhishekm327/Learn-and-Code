package server;

import java.io.*;
import java.net.Socket;

import org.json.JSONObject;

import server.service.*;

public class CafeteriaServerThread extends Thread {
    private final Socket socket;
    private final UserAuthenticationService userAuthService = new UserAuthenticationService();
    private final AdminService adminService = new AdminService();
    private final ChefService chefService = new ChefService();
    private final EmployeeService employeeService = new EmployeeService();

    public CafeteriaServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                JSONObject jsonRequest = new JSONObject(request);
                JSONObject jsonResponse = handleRequest(jsonRequest);
                writer.println(jsonResponse.toString());
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JSONObject handleRequest(JSONObject jsonRequest) {
        String action = jsonRequest.getString("action");
        JSONObject jsonResponse = new JSONObject();

        switch (action) {
            case "AUTHENTICATION":
                String userId = jsonRequest.getString("userId");
                String role = jsonRequest.getString("role");
                String password = jsonRequest.getString("password");
                boolean isAuthenticated = userAuthService.authenticate(userId, role, password);
                jsonResponse.put("success", isAuthenticated);
                if (!isAuthenticated) {
                    jsonResponse.put("error", "Invalid credentials");
                } else {
                    jsonResponse.put("role", role);
                }
                break;
            case "ADMIN_ACTION":
                jsonResponse = adminService.handleAdminActions(jsonRequest);
                break;
            case "CHEF_ACTION":
                jsonResponse = chefService.handleChefActions(jsonRequest);
                break;
            case "EMPLOYEE_ACTION":
                jsonResponse = employeeService.handleEmployeeActions(jsonRequest);
                break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid action");
                break;
        }

        return jsonResponse;
    }
}
