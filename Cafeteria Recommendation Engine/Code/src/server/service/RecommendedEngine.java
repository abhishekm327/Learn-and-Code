package server.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import server.model.Feedback;
import server.model.RecommendedMenu;

public class RecommendedEngine {
	
	FeedbackOperations feedbackOperations = new FeedbackOperations();
	private Map<String, RecommendedMenu> recommendMap = new HashMap<>();

    public boolean generateRecommendedMenu() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            clearPreviousRecommendations(connection);
            calculateResultantScores();
            List<RecommendedMenu> topItems = getTopRecommendedItems();
            insertRecommendedItems(connection, topItems);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	public JSONArray getRecommendedMenu() {
	    String query = "SELECT food_id, food_name, rating, comment, rating FROM recommended_menu ORDER BY rating DESC LIMIT 10";
	    JSONArray recommendedMenuArray = new JSONArray();

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            JSONObject menuJson = new JSONObject();
	            menuJson.put("foodId", resultSet.getString("food_id"));
	            menuJson.put("foodName", resultSet.getString("food_name"));
	            menuJson.put("rating", resultSet.getDouble("rating"));
	            recommendedMenuArray.put(menuJson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return recommendedMenuArray;
	}
    
    private void clearPreviousRecommendations(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM recommended_menu";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.executeUpdate();
        }
    }
    
    private void calculateResultantScores() {
    	List<Feedback> feedbackList = feedbackOperations.fetchFeedback();
    	
    	for(Feedback feedback : feedbackList) {
    		String foodId = feedback.getFoodId();
            String foodName = feedback.getFoodName();
            String comment = feedback.getComment();
            double rating = feedback.getRating();

            RecommendedMenu recommendedMenu = recommendMap.getOrDefault(foodId, new RecommendedMenu(foodId, foodName));
            recommendedMenu.addComment(comment);
            recommendedMenu.addRating(rating);
            recommendMap.put(foodId, recommendedMenu);	
    	}
    	
        for (RecommendedMenu recommendedMenu : recommendMap.values()) {
            String combinedComments = String.join(" | ", recommendedMenu.getComments());
            double sentimentScore = SentimentAnalysis.analyzeSentiment(combinedComments);
            recommendedMenu.setSentimentAnalysis(sentimentScore);

            double averageRating = recommendedMenu.getAverageRating();
            double resultantScore = (averageRating + sentimentScore) / 2;
            recommendedMenu.setResultantScore(resultantScore);
        }
    }
    
    private List<RecommendedMenu> getTopRecommendedItems() {
        List<RecommendedMenu> recommendedList = new ArrayList<>(recommendMap.values());        
        Collections.sort(recommendedList, new Comparator<RecommendedMenu>() {
            @Override
            public int compare(RecommendedMenu firstMenu, RecommendedMenu nextMenu) {
                return Double.compare(nextMenu.getResultantScore(), firstMenu.getResultantScore());
            }
        });

        List<RecommendedMenu> topItems = recommendedList.subList(0, Math.min(10, recommendedList.size()));
        return topItems;
    }
    
    private void insertRecommendedItems(Connection connection, List<RecommendedMenu> topItems) throws SQLException {
        String insertQuery = "INSERT INTO recommended_menu (food_id, food_name, comment, rating) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            for (RecommendedMenu recommendedMenu : topItems) {
                insertStatement.setString(1, recommendedMenu.getFoodId());
                insertStatement.setString(2, recommendedMenu.getFoodName());
                insertStatement.setString(3, String.join(" | ", recommendedMenu.getComments()));
                insertStatement.setDouble(4, recommendedMenu.getAverageRating());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        }
    }
	
	public List<RecommendedMenu> getDiscardMenuItems() {
        List<RecommendedMenu> discardList = new ArrayList<>();
        for (RecommendedMenu menu : recommendMap.values()) {
            if (menu.getAverageRating() < 2 || containsHighlyNegativeSentiments(menu.getComments())) {
                discardList.add(menu);
            }
        }
        return discardList;
    }

    private boolean containsHighlyNegativeSentiments(List<String> comments) {
        for (String comment : comments) {
            double sentimentScore = SentimentAnalysis.analyzeSentiment(comment);
            if (sentimentScore <= 2) {
                return true;
            }
        }
        return false;
    }

}
