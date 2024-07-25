package server.model;

import java.sql.Timestamp;

public class Notification {
	private int notificationId;
	private String message;
	private Timestamp notificationDate;

	public Notification(int notificationId, String message, Timestamp notificationDate) {
		this.notificationId = notificationId;
		this.message = message;
		this.notificationDate = notificationDate;
	}

	public int getNotificationId() {
		return notificationId;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getNotificationDate() {
		return notificationDate;
	}
}
