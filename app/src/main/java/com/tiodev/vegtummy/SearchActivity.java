package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myrecipe.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tiodev.vegtummy.Adapter.Adapter;
import com.tiodev.vegtummy.Model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    EditText search;
    ImageView back_btn;
    RecyclerView rcview;
    Adapter adapter;
    List<Recipe> allRecipes = new ArrayList<>(); // Holds all fetched recipes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search);
        back_btn = findViewById(R.id.back_to_home);
        rcview = findViewById(R.id.rcview);

        rcview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(new ArrayList<>(), getApplicationContext()); // Initially empty list
        rcview.setAdapter(adapter);

        back_btn.setOnClickListener(v -> finish());

        fetchAllRecipesFromFirestore(); // Fetch all recipes from Firestore

        setupSearchFunctionality(); // Setup the search functionality
    }

    private void fetchAllRecipesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Recipe recipe = document.toObject(Recipe.class);
                    allRecipes.add(recipe);
                }
                // Initially, display all recipes or a specific category if needed
                adapter.updateRecipes(allRecipes); // Make sure you have this method in your adapter
            } else {
                // Handle error
            }
        });
    }

    private void setupSearchFunctionality() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    // Filter the searched item from all recipes
    public void filter(String text) {
        List<Recipe> filteredList = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (recipe.getTitle().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(recipe);
            }
        }
        adapter.updateRecipes(filteredList); // Update adapter with filtered list
    }
}
