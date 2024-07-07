package server.model;

public class Profile {
    private String userId;
    private String foodType;
    private String foodStyle;
    private String spiceLevel;
    private String sweet;

    public Profile(String userId, String foodType, String foodStyle, String spiceLevel, String sweet) {
        this.userId = userId;
        this.foodType = foodType;
        this.foodStyle = foodStyle;
        this.spiceLevel = spiceLevel;
        this.sweet = sweet;
    }

    public String getUserId() {
        return userId;
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

