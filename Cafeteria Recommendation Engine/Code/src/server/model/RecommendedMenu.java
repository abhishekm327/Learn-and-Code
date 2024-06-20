package server.model;

import java.util.Date;

public class RecommendedMenu {
    private String foodId;
    private String foodName;
    private Date recommendedDate;
    private Double rating;

    public RecommendedMenu(String foodId, String foodName, Double rating, Date recommendedDate) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.recommendedDate = recommendedDate;
        this.rating = rating;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getfoodName() {
        return foodName;
    }
    
    public double getRating() {
        return rating;
    }

    public Date getRecommendedDate() {
        return recommendedDate;
    }    
}

    
