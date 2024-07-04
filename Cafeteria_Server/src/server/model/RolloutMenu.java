package server.model;

import java.util.Date;

public class RolloutMenu {
    private String foodId;
    private String name;
    private Date cookingDate;
    private double rating;
    private int vote;

    public RolloutMenu(String foodId, String name, Date cookingDate, double rating, int vote) {
        this.foodId = foodId;
        this.name = name;
        this.cookingDate = cookingDate;
        this.rating=rating;
        this.vote = vote;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }
    
    public double getRating() {
        return rating;
    }

    public Date getCookingDate() {
        return cookingDate;
    }

    public int getVote() {
        return vote;
    }
}
