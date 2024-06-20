package server.model;

import java.util.List;
import java.util.ArrayList;

public class Report {
	
	private final String foodId;
    private final String foodName;
    private final List<String> comments;
    private final List<Double> ratings;
    private String sentimentAnalysis;

    public Report(String foodId, String foodName) {
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
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
    
    public String getSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public void setSentimentAnalysis(String sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }
}

