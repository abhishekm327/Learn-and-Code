package server.servercontroller;

import org.json.JSONObject;

import server.service.UserAuthenticationService;

public class ServerUserController {
	
	 private final UserAuthenticationService userAuthService = new UserAuthenticationService();
	 private final ServerAdminController serverAdminController = new ServerAdminController();
	 private final ServerChefController serverChefController = new ServerChefController();
	 private final ServerEmployeeController serverEmployeeController  = new ServerEmployeeController();
	
    public JSONObject handleRequest(JSONObject jsonRequest) {
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
                jsonResponse = serverAdminController.handleAdminActions(jsonRequest);
                break;
            case "CHEF_ACTION":
                jsonResponse = serverChefController.handleChefActions(jsonRequest);
                break;
            case "EMPLOYEE_ACTION":
                jsonResponse = serverEmployeeController.handleEmployeeActions(jsonRequest);
                break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Invalid action");
                break;
        }

        return jsonResponse;
    }

}
