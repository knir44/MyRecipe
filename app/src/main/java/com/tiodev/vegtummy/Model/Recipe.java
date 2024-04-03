package com.tiodev.vegtummy.Model;

import android.util.Log;

import androidx.annotation.NonNull;

public class Recipe {
    // Assuming these are the fields you have in your Recipe class
    private String imagePath;
    private String title;
    private String description;
    private String ingredients;
    private String category;
    private Long cookingTime;

    // No-argument constructor for Firebase
    public Recipe() {
    }

    // Constructor with arguments
    public Recipe(String imagePath, String title, String description, String ingredients, String category, Long cookingTime) {
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

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                // Other fields...
                "cookingTime=" + cookingTime +
                '}';
    }

    public Long getCookingTime() {
        return this.cookingTime;
    }


    public void setCookingTime(Long cookingTime) {
        this.cookingTime = cookingTime;
    }
}
