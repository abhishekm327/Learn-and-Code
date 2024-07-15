package server.model;

public class FoodMenu {
	private String foodId;
	private String name;
	private int price;
	private String foodType;
	private String foodStyle;
	private String spiceLevel;
	private String sweet;

	public FoodMenu(String foodId, String name, int price, String foodType, String foodStyle, String spiceLevel,
			String sweet) {
		this.foodId = foodId;
		this.name = name;
		this.price = price;
		this.foodType = foodType;
		this.foodStyle = foodStyle;
		this.spiceLevel = spiceLevel;
		this.sweet = sweet;
	}

	public String getFoodId() {
		return foodId;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getFoodType() {
		return foodType;
	}

	public String getFoodStyle() {
		return foodStyle;
	}

	public String getSpiceLevel() {
		return spiceLevel;
	}

	public String getSweet() {
		return sweet;
	}
}
