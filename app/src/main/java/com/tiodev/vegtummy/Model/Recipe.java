package com.tiodev.vegtummy.Model;

import android.net.Uri;

public class Recipe {
    private String imagePath;
    private String title;
    private String description;
    private String ingredients;
    private String category;
    private String cookingTime;

    public Recipe() {
        // No-args constructor
    }

    public Recipe(String imagePath, String title, String description, String ingredients, String category, String cookingTime) {
        setImagePath(imagePath);
        setTitle(title);
        setDescription(description);
        setIngredients(ingredients);
        setCategory(category);
        setCookingTime(cookingTime);
    }

    public String getImagePath() {
        return imagePath;
    }

    public Uri getImageUri() {
        return Uri.parse(imagePath);
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category != null ? category : "";
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime != null ? cookingTime : "";
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients != null ? ingredients : "";
    }
}
