package server.model;

import java.util.ArrayList;
import java.util.List;

public class RecommendedMenu {
	private final String foodId;
    private final String foodName;
    private final List<String> comments;
    private final List<Double> ratings;
    private double sentimentAnalysis;
    private double resultantScore;

    public RecommendedMenu (String foodId, String foodName) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.comments = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void addRating(double rating) {
        ratings.add(rating);
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public List<String> getComments() {
        return comments;
    }

    public double getAverageRating() {
    	if(ratings.isEmpty()) {
    		return 0;
    	}
    	
    	double sum =0;
    	for(double rate : ratings) {
    		sum+=rate;	
    	}
    	
    	return (sum/ratings.size());
    }
    
    public double getResultantScore() {
        return resultantScore;
    }
    
    public void setResultantScore(double resultantScore) {
        this.resultantScore = resultantScore;
    }
    
    public double getSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public void setSentimentAnalysis(double sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }
}

    
