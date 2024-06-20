package server.model;

public class FoodMenu {
    private String foodId;
    private String name;
    private int price;

    public FoodMenu(String foodId, String name, int price) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
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
}
