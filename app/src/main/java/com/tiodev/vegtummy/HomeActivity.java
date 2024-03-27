package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.tiodev.vegtummy.Adapter.AdapterPopular;
import com.tiodev.vegtummy.RoomDB.AppDatabase;
import com.tiodev.vegtummy.RoomDB.Recipe;
import com.tiodev.vegtummy.RoomDB.RecipeDao;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    ImageView salad, main, drinks, dessert, addRecipe;
    RecyclerView rcview_home;
    List<Recipe> dataPopular = new ArrayList<>();
    LottieAnimationView lottie;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find views
        salad = findViewById(R.id.salad);
        main = findViewById(R.id.MainDish);
        drinks = findViewById(R.id.Drinks);
        dessert = findViewById(R.id.Desserts);
        rcview_home = findViewById(R.id.rcview_popular);
        lottie = findViewById(R.id.lottie);
        editText = findViewById(R.id.editText);
        addRecipe = findViewById(R.id.add_recipe_btn);

        // Set layout to recyclerView
        rcview_home.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // Set Popular recipes
        setPopularList();

        // Category buttons- start new activity with intent method "start"
        salad.setOnClickListener(v -> start("Salad","Salad"));
        main.setOnClickListener(v -> start("Dish", "Main dish"));
        drinks.setOnClickListener(v -> start("Drinks", "Drinks"));
        dessert.setOnClickListener(v -> start("Desserts", "Dessert"));

        // Open search activity
        editText.setOnClickListener(v ->{
            Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
        });

        // Open add recipe activity
        addRecipe.setOnClickListener(v -> addRecipe());
    }


    public void setPopularList() {

        // Get database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "recipe.db") // Use "recipe.db" here to match the file name in assets
                .allowMainThreadQueries() // Caution: It's generally not recommended to allow main thread queries
                .createFromAsset("database/recipe.db") // This assumes your database file is in the "assets/database" directory
                .fallbackToDestructiveMigration() // This will clear the database on version mismatch. Use with caution.
                .build();
        RecipeDao recipeDao = db.recipeDao();

        // Get all recipes from database
        List<Recipe> recipes = recipeDao.getAll();

        // Filter Popular category from all recipes
        for(int i = 0; i<recipes.size(); i++){
            if(recipes.get(i).getCategory().contains("Popular")){
                dataPopular.add(recipes.get(i));
            }
        }

        // Set popular list to adapter
        AdapterPopular adapter = new AdapterPopular(dataPopular, getApplicationContext());
        rcview_home.setAdapter(adapter);

        // Hide progress animation
        lottie.setVisibility(View.GONE);

    }

    // Start MainActivity(Recipe list) with intent message
    public void start(String p, String title){
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.putExtra("Category", p);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void addRecipe() {
        Intent intent = new Intent(HomeActivity.this, RecipeUploadActivity.class);
        startActivity(intent);
    }
}