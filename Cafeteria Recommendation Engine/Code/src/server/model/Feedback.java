package server.model;

import java.util.Date;

public class Feedback {
    private int feedbackId;
    private String foodId;
    private String foodName;
    private String comment;
    private double rating;
    private Date feedbackDate;

    public Feedback(int feedbackId, String foodId, String foodName, String comment, double rating, Date feedbackDate) {
        this.feedbackId = feedbackId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.comment = comment;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getComment() {
        return comment;
    }

    public double getRating() {
        return rating;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }
}
