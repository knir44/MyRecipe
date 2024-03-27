//package com.tiodev.vegtummy.RoomDB;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "recipe")
//public class Recipe {
//
//    @PrimaryKey(autoGenerate = true)
//    public int uid;
//
//    @NonNull
//    public String img; // Stores either a URI or a path to the image
//
//    @NonNull
//    public String title;
//
//    @NonNull
//    public String description;
//
//    @NonNull
//    public String ingredients;
//
//    @NonNull
//    public String category;
//
//    public Recipe(@NonNull String img, @NonNull String title, @NonNull String description, @NonNull String ingredients, @NonNull String category) {
//        this.img = img;
//        this.title = title;
//        this.description = description;
//        this.ingredients = ingredients;
//        this.category = category;
//    }
//
//    @NonNull
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(@NonNull String img) {
//        this.img = img;
//    }
//
//    @NonNull
//    public String getTitle() { // Corrected method name
//        return title;
//    }
//
//    public void setTitle(@NonNull String title) { // Corrected method name
//        this.title = title;
//    }
//
//    @NonNull
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(@NonNull String description) {
//        this.description = description;
//    }
//
//    @NonNull
//    public String getIngredients() {
//        return ingredients;
//    }
//
//    public void setIngredients(@NonNull String ingredients) {
//        this.ingredients = ingredients;
//    }
//
//    @NonNull
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(@NonNull String category) {
//        this.category = category;
//    }
//}
