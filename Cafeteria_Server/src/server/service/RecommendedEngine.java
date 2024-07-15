package server.service;

import org.json.JSONArray;
import server.database.RecommendedMenuDBOperation;
import server.database.exception.DatabaseException;
import server.database.FeedbackDBOperation;
import server.model.Feedback;
import server.model.RecommendedMenu;
import java.util.*;

public class RecommendedEngine {
	private final FeedbackDBOperation feedbackDBOperation = new FeedbackDBOperation();
	private final RecommendedMenuDBOperation recommendedMenuDBOperation = new RecommendedMenuDBOperation();
	private final Map<String, RecommendedMenu> recommendMap = new HashMap<>();

	public boolean generateRecommendedMenu() throws DatabaseException {
		recommendedMenuDBOperation.clearPreviousRecommendations();
		calculateResultantScores();
		List<RecommendedMenu> topItems = getTopRecommendedItems();
		recommendedMenuDBOperation.insertRecommendedItems(topItems);
		return true;
	}

	public JSONArray getRecommendedMenu() throws DatabaseException {
		return recommendedMenuDBOperation.fetchRecommendedMenu();
	}

	private void calculateResultantScores() throws DatabaseException {
		populateRecommendMapFromFeedback();
		for (RecommendedMenu recommendedMenu : recommendMap.values()) {
			String combinedComments = String.join(" | ", recommendedMenu.getComments());
			double sentimentScore = SentimentAnalysis.analyzeSentiment(combinedComments);
			recommendedMenu.setSentimentAnalysis(sentimentScore);

			double averageRating = recommendedMenu.getAverageRating();
			double resultantScore = (averageRating + sentimentScore) / 2;
			recommendedMenu.setResultantScore(resultantScore);
		}
	}

	private void populateRecommendMapFromFeedback() throws DatabaseException {
		List<Feedback> feedbackList = feedbackDBOperation.fetchFeedback();

		for (Feedback feedback : feedbackList) {
			String foodId = feedback.getFoodId();
			String foodName = feedback.getFoodName();
			String comment = feedback.getComment();
			double rating = feedback.getRating();

			RecommendedMenu recommendedMenu = recommendMap.getOrDefault(foodId, new RecommendedMenu(foodId, foodName));
			recommendedMenu.addComment(comment);
			recommendedMenu.addRating(rating);
			recommendMap.put(foodId, recommendedMenu);
		}
	}

	private List<RecommendedMenu> getTopRecommendedItems() {
		List<RecommendedMenu> recommendedList = new ArrayList<>(recommendMap.values());
		for (int i = 0; i < recommendedList.size() - 1; i++) {
			for (int j = 0; j < recommendedList.size() - i - 1; j++) {
				if (recommendedList.get(j).getResultantScore() < recommendedList.get(j + 1).getResultantScore()) {
					RecommendedMenu temp = recommendedList.get(j);
					recommendedList.set(j, recommendedList.get(j + 1));
					recommendedList.set(j + 1, temp);
				}
			}
		}
		return recommendedList.subList(0, Math.min(10, recommendedList.size()));
	}

	public List<RecommendedMenu> getDiscardMenuItems() throws DatabaseException {
		List<RecommendedMenu> discardList = new ArrayList<>();
		populateRecommendMapFromFeedback();
		for (RecommendedMenu menu : recommendMap.values()) {
			if (menu.getAverageRating() < 2 || containsHighlyNegativeSentiments(menu.getComments())) {
				discardList.add(menu);
			}
		}
		return discardList;
	}

	private boolean containsHighlyNegativeSentiments(List<String> comments) {
		double totalScore = 0;
		for (String comment : comments) {
			double sentimentScore = SentimentAnalysis.analyzeSentiment(comment);
			totalScore += sentimentScore;
		}
		double averageScore = totalScore / comments.size();

		if (averageScore < 2) {
			return true;
		}
		return false;
	}
}
