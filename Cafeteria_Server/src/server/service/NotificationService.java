package server.service;

import server.database.DatabaseException;
import server.database.NotificationDBOperation;
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
}
