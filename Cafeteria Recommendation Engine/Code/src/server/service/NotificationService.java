package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import server.model.Notification;

public class NotificationService {
	
	public boolean sendNotification(String message) {
	    String insertNotificationQuery = "INSERT INTO notifications (message) VALUES (?)";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement insertNotificationStatement = connection.prepareStatement(insertNotificationQuery)) {
	            insertNotificationStatement.setString(1, message);
	            insertNotificationStatement.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public List<Notification> getNotification() { 
        List<Notification> notificationList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM notifications WHERE notification_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	int notificationId = resultSet.getInt("notification_id");
                String message = resultSet.getString("message");
                Timestamp notificationDate = resultSet.getTimestamp("notification_date");
                notificationList.add(new Notification(notificationId, message, notificationDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificationList;
    }

}
