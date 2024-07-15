package server.service;

import org.json.JSONObject;
import server.database.UserDBOperation;

public class UserService {

	UserDBOperation userDBOperation = new UserDBOperation();

	public JSONObject authenticate(String userId, String password) {

		JSONObject userInfo = userDBOperation.getUserInfo(userId);
		JSONObject response = new JSONObject();

		if (userInfo.getBoolean("exist")) {
			String dbPassword = userInfo.getString("password");
			if (password.equals(dbPassword)) {
				response.put("success", true);
				response.put("role", userInfo.getString("role"));
			} else {
				response.put("success", false);
				response.put("error", "Invalid password");
			}
		} else {
			response.put("success", false);
			response.put("error", userInfo.has("error") ? userInfo.getString("error") : "Invalid User ID");
		}
		return response;
	}
}
