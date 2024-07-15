package server.database;

import server.database.exception.DatabaseException;
import server.model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDBOperation {

	public void insertNotification(String message) throws DatabaseException {
		String query = "INSERT INTO notifications (message) VALUES (?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, message);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error while inserting notification. Please try again later");
		}
	}

	public List<Notification> fetchRecentNotifications() throws DatabaseException {
		List<Notification> notificationList = new ArrayList<>();
		String query = "SELECT * FROM notifications WHERE notification_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY notification_date DESC";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int notificationId = resultSet.getInt("notification_id");
				String message = resultSet.getString("message");
				Timestamp notificationDate = resultSet.getTimestamp("notification_date");
				notificationList.add(new Notification(notificationId, message, notificationDate));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error while fetching notifications. Please try again later");
		}
		return notificationList;
	}
}
