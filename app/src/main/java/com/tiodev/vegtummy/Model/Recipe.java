package com.tiodev.vegtummy.Model;

public class Recipe {
    // Assuming these are the fields you have in your Recipe class
    private String imagePath;
    private String title;
    private String description;
    private String ingredients;
    private String category;
    private int cookingTime; // or String, depending on how you want to handle this

    // No-argument constructor for Firebase
    public Recipe() {
    }

    // Constructor with arguments
    public Recipe(String imagePath, String title, String description, String ingredients, String category, int cookingTime) {
        this.imagePath = imagePath;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.category = category;
        this.cookingTime = cookingTime;
    }

    // Getters and setters for all fields
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
}
