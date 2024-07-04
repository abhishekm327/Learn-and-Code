package server.servercontroller;

import org.json.JSONObject;
import server.service.UserService;

public class ServerUserController {
	
	 UserService userService = new UserService();
	 ServerAdminController serverAdminController = new ServerAdminController();
	 ServerChefController serverChefController = new ServerChefController();
	 ServerEmployeeController serverEmployeeController  = new ServerEmployeeController();
	
    public JSONObject handleRequest(JSONObject jsonRequest) {
        String action = jsonRequest.getString("action");
        JSONObject jsonResponse = new JSONObject();

        switch (action) {
            case "AUTHENTICATION":
                String userId = jsonRequest.getString("userId");
                String password = jsonRequest.getString("password");
                JSONObject authResponse = userService.authenticate(userId, password);
                boolean isAuthenticated = authResponse.getBoolean("success");
                jsonResponse.put("success", isAuthenticated);
                if (!isAuthenticated) {
                    jsonResponse.put("error", authResponse.getString("error"));
                } else {
                    jsonResponse.put("role", authResponse.getString("role"));
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
