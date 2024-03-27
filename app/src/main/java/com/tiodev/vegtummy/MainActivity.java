package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myrecipe.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tiodev.vegtummy.Adapter.Adapter;
import com.tiodev.vegtummy.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recview;
    List<Recipe> dataFinal = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize recyclerView
        recview = findViewById(R.id.recview);

        // Set layout manager to recyclerView
        recview.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display recipes from Firestore
        fetchRecipesFromFirestore();
    }

    private void fetchRecipesFromFirestore() {
        String category = getIntent().getStringExtra("Category");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Recipe> recipes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipes.add(recipe);
                        }
                        // Now that we have the recipes, update the RecyclerView
                        Adapter adapter = new Adapter(recipes, getApplicationContext());
                        recview.setAdapter(adapter);
                    } else {
                        Log.e("MainActivity", "Error fetching documents: ", task.getException());
                        showErrorToast();
                    }
                });
    }

    private void showErrorToast() {
        Toast.makeText(MainActivity.this, "Failed to load recipes. Please try again later.", Toast.LENGTH_LONG).show();
    }
}
