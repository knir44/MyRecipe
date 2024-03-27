package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiodev.vegtummy.Adapter.Adapter;
import com.tiodev.vegtummy.RoomDB.AppDatabase;
import com.tiodev.vegtummy.RoomDB.Recipe;
import com.tiodev.vegtummy.RoomDB.RecipeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    RecyclerView recview;
    boolean connected = false;
    List<Recipe> dataFinal = new ArrayList<>();
    ImageView back;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        back = findViewById(R.id.imageView2);
        title = findViewById(R.id.title);
        recview = (RecyclerView)findViewById(R.id.recview);

        // Set layout manager to recyclerView
        recview.setLayoutManager(new LinearLayoutManager(this));

        // Set recipe category title
        title.setText(getIntent().getStringExtra("title"));

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

        // Filter category from recipes
        for(int i = 0; i<recipes.size(); i++){
            if(recipes.get(i).getCategory().contains(Objects.requireNonNull(getIntent().getStringExtra("Category")))){
                dataFinal.add(recipes.get(i));
            }
        }

        // Set category list to adapter
        Adapter adapter = new Adapter(dataFinal, getApplicationContext());
        recview.setAdapter(adapter);

        back.setOnClickListener(v -> finish());
    }
}
