package server.service;

import java.util.List;

import server.database.NotificationDBOperation;
import server.database.exception.DatabaseException;
import server.model.Notification;

public class NotificationService {
	NotificationDBOperation notificationDBOperation = new NotificationDBOperation();

	public void sendNotification(String message) throws DatabaseException {
		notificationDBOperation.insertNotification(message);
	}

	public void sendNotificationForDiscardMenu(String foodName) throws DatabaseException {
		String message = "Food item \"" + foodName + "\" will be removed from the menu because of low ratings";
		sendNotification(message);
	}

	public List<Notification> getNotification() throws DatabaseException {
		return notificationDBOperation.fetchRecentNotifications();
	}

	public List<Notification> getDiscardMenuNotification() throws DatabaseException {
		return notificationDBOperation.fetchDiscardMenuNotifications();
	}
}
