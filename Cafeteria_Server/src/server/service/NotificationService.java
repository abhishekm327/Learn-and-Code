package server.service;

import server.database.NotificationDBOperation;
import server.database.exception.DatabaseException;
import server.model.Notification;
import java.util.List;

public class NotificationService {
	NotificationDBOperation notificationDBOperation = new NotificationDBOperation();

	public void sendNotification(String message) throws DatabaseException {
		notificationDBOperation.insertNotification(message);
	}

	public List<Notification> getNotification() throws DatabaseException {
		return notificationDBOperation.fetchRecentNotifications();
	}

	public void sendNotificationForDiscardMenu(String foodName) throws DatabaseException {
		String message = "Food item " + foodName + " will be remove from the menu because of less Ratings";
		sendNotification(message);
	}
}
