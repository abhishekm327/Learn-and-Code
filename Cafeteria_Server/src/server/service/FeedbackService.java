package server.service;

import server.database.FeedbackDBOperation;
import server.database.exception.DatabaseException;
import server.model.Feedback;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedbackService {
	FeedbackDBOperation feedbackDBOperation = new FeedbackDBOperation();
	NotificationService notificationService = new NotificationService();

	public void provideFeedback(JSONObject jsonRequest) throws DatabaseException {
		feedbackDBOperation.insertFeedback(jsonRequest);
	}

	public List<Feedback> viewFeedback() throws DatabaseException {
		List<Feedback> feedbackList = feedbackDBOperation.fetchFeedback();
		return feedbackList;
	}

	public void askDetailedFeedbackForDiscardMenu(String foodName) throws DatabaseException {
		String message = "We are trying to improve your experience with " + foodName
				+ ".\nPlease provide your feedback and help us.\n" + "Q1. What didn't you like about " + foodName
				+ "?\n" + "Q2. How would you like " + foodName + " to taste?\n" + "Q3. Share your mom's recipe.";
		notificationService.sendNotification(message);
	}
	
	public void provideFeedbackForDiscardMenu(JSONObject jsonRequest) throws DatabaseException {
		feedbackDBOperation.insertDiscardMenuFeedback(jsonRequest);
	}
	
	public JSONArray viewDiscardMenuFeedback() throws DatabaseException {
		return feedbackDBOperation.fetchDiscardMenuFeedback();
	}
}
