package server.service;

import server.database.DatabaseException;

public class DiscardMenuService {
	
	NotificationService notificationService = new NotificationService();
	
    public void notificationForDiscardMenu(String foodName) throws DatabaseException {
    	String message = "Food item " + foodName + " will be remove from the menu because of less Ratings";
    	notificationService.sendNotification(message);
    }

    public void askForDetailedFeedback(String foodName) throws DatabaseException {
        
    	String message = "We are trying to improve your experience with " 
    					+ foodName + ". Please provide your feedback and help us.\n"+
    					"Q1. What didn't you like about " + foodName + "?\n"+
    					"Q2. How would you like " + foodName + " to taste?\n" +
    					"Q3. Share your mom's recipe.";
    	notificationService.sendNotification(message);
    }

}
