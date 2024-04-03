package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myrecipe.R;
import com.tiodev.vegtummy.Adapter.AdapterPopular;
import com.tiodev.vegtummy.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;


public class HomeActivity extends AppCompatActivity {

    ImageView salad, main, drinks, dessert, addRecipe;
    RecyclerView rcview_home;
    LottieAnimationView lottie;
    AdapterPopular adapterPopular;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find views
        salad = findViewById(R.id.salad);
        main = findViewById(R.id.MainDish);
        drinks = findViewById(R.id.Drinks);
        dessert = findViewById(R.id.Dessert);
        rcview_home = findViewById(R.id.rcview_popular);
        lottie = findViewById(R.id.lottie);
        editText = findViewById(R.id.editText);
        addRecipe = findViewById(R.id.add_recipe_btn);

        // Set layout to recyclerView
        rcview_home.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Initialize the adapter with an empty list
        AdapterPopular adapter = new AdapterPopular(new ArrayList<>(), getApplicationContext());
        rcview_home.setAdapter(adapter);

        // Set Popular recipes
        setPopularList();

        // Category buttons- start new activity with intent method "start"
        salad.setOnClickListener(v -> start("Salad","Salad"));
        main.setOnClickListener(v -> start("Main Dish", "Main Dish"));
        drinks.setOnClickListener(v -> start("Drinks", "Drinks"));
        dessert.setOnClickListener(v -> start("Dessert", "Dessert"));

        // Open search activity
        editText.setOnClickListener(v ->{
            Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
        });

        // Open add recipe activity
        addRecipe.setOnClickListener(v -> addRecipe());
    }


    public void setPopularList() {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch recipes marked as 'Popular' from Firestore
        db.collection("recipes")
                .whereEqualTo("category", "Popular")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Recipe> popularRecipes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            popularRecipes.add(recipe);
                        }
                        // Now that we have the popular recipes, update the RecyclerView
                        adapterPopular = new AdapterPopular(popularRecipes, getApplicationContext());
                        rcview_home.setAdapter(adapterPopular);
                        lottie.setVisibility(View.GONE); // Hide the loading animation
                    } else {
                        Log.w("Firestore", "Error getting documents: ", task.getException());
                        lottie.setVisibility(View.GONE); // Hide the loading animation even on failure
                    }
                });
    }

    // Start MainActivity(Recipe list) with intent message
    public void start(String category, String title){
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.putExtra("Category", category);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void addRecipe() {
        Intent intent = new Intent(HomeActivity.this, RecipeUploadActivity.class);
        startActivity(intent);
    }
}