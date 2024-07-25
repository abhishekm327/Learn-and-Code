package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import server.database.exception.DatabaseException;
import server.model.Notification;

public class NotificationDBOperation {

	public void insertNotification(String message) throws DatabaseException {
		String query = "INSERT INTO notifications (message) VALUES (?)";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, message);
			statement.executeUpdate();
		} catch (SQLException exception) {
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
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching notifications. Please try again later");
		}
		return notificationList;
	}

	public List<Notification> fetchDiscardMenuNotifications() throws DatabaseException {
		List<Notification> notificationList = new ArrayList<>();
		String query = "SELECT * FROM notifications WHERE notification_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND message LIKE '%removed%low ratings%' ORDER BY notification_date DESC";
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int notificationId = resultSet.getInt("notification_id");
				String message = resultSet.getString("message");
				Timestamp notificationDate = resultSet.getTimestamp("notification_date");
				notificationList.add(new Notification(notificationId, message, notificationDate));
			}
		} catch (SQLException exception) {
			throw new DatabaseException("Error while fetching notifications. Please try again later");
		}
		return notificationList;
	}
}
