package com.tiodev.vegtummy.inventory;

import com.example.myrecipe.R;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static List<Category> getCategoryList(){
        List<Category> categoryList = new ArrayList<>();

        Category popular = new Category("Popular", R.drawable.catergory_drinks);
        Category salad = new Category("Salad", R.drawable.category_salad);
        Category mainDish = new Category("Main Dish", R.drawable.category_main);
        Category drinks = new Category("Drinks", R.drawable.catergory_drinks);
        Category dessert = new Category("Dessert", R.drawable.category_dessert);

        categoryList.add(popular);
        categoryList.add(salad);
        categoryList.add(mainDish);
        categoryList.add(drinks);
        categoryList.add(dessert);

        return categoryList;
    }
}
