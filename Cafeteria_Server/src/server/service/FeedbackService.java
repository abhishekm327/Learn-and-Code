package server.service;

import server.database.DatabaseException;
import server.database.FeedbackDBOperation;
import server.model.Feedback;
import java.util.List;

public class FeedbackService {
    FeedbackDBOperation feedbackDBOperation = new FeedbackDBOperation();

    public void provideFeedback(String foodId, String comment, double rating) throws DatabaseException {
    	feedbackDBOperation .insertFeedback(foodId, comment, rating);
    }

    public List<Feedback> viewFeedback() throws DatabaseException {
    	List<Feedback> feedbackList = feedbackDBOperation .fetchFeedback();
    	return feedbackList;
    }
}
