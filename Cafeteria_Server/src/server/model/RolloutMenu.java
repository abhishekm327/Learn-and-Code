package server.model;

import java.util.Date;

public class RolloutMenu {
	private String foodId;
	private String name;
	private Date cookingDate;
	private double rating;
	private int vote;
	private String foodType;
	private String foodStyle;
	private String spiceLevel;
	private String sweet;

	public RolloutMenu(String foodId, String name, Date cookingDate, double rating, int vote) {
		this.foodId = foodId;
		this.name = name;
		this.cookingDate = cookingDate;
		this.rating = rating;
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

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getFoodStyle() {
		return foodStyle;
	}

	public void setFoodStyle(String foodStyle) {
		this.foodStyle = foodStyle;
	}

	public String getSpiceLevel() {
		return spiceLevel;
	}

	public void setSpiceLevel(String spiceLevel) {
		this.spiceLevel = spiceLevel;
	}

	public String getSweet() {
		return sweet;
	}

	public void setSweet(String sweet) {
		this.sweet = sweet;
	}
}
