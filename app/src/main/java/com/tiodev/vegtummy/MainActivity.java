package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Adapter adapter; // Declare the adapter at class level to access it within fetchRecipesFromFirestore()
    List<Recipe> dataFinal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize recyclerView
        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list and set it to recyclerView
        adapter = new Adapter(new ArrayList<>(), getApplicationContext());
        recview.setAdapter(adapter);

        // Fetch and display recipes from Firestore
        fetchRecipesFromFirestore();
    }

    private void fetchRecipesFromFirestore() {
        String category = getIntent().getStringExtra("Category");
        if (category == null) {
            Log.e("MainActivity", "No category specified.");
            showErrorToast("No category specified. Please try again.");
            finish();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Recipe> recipes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipes.add(recipe);
                        }
                        // Update the adapter with the fetched recipes
                        adapter.updateData(recipes);
                    } else {
                        Log.e("MainActivity", "Error fetching documents: ", task.getException());
                        showErrorToast("Failed to load recipes. Please try again later.");
                    }
                });
    }

    // Update the showErrorToast method to accept a message
    private void showErrorToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
