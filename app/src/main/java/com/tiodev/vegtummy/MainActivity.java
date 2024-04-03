package com.tiodev.vegtummy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
    Adapter adapter;

    // Registering an ActivityResultLauncher for the "All Files Access" permission
    private final ActivityResultLauncher<Intent> manageAllFilesAccessLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !android.os.Environment.isExternalStorageManager()) {
                            Toast.makeText(MainActivity.this, "Access to all files is not granted.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Permission granted, or not needed (below Android 11)
                            initializeUI();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!android.os.Environment.isExternalStorageManager()) {
                // Launching the "Manage All Files Access" settings
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                manageAllFilesAccessLauncher.launch(intent);
            } else {
                // All Files Access already granted
                initializeUI();
            }
        } else {
            // For Android 10 and below, where this permission isn't applicable
            initializeUI();
        }
    }

    private void initializeUI() {
        // Initialize recyclerView
        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list and set it to recyclerView
        adapter = new Adapter(new ArrayList<>(), this); // Use 'this' for the context to avoid potential issues
        recview.setAdapter(adapter);

        // set title from the intent
        TextView titleTextView = findViewById(R.id.title);
        String title = getIntent().getStringExtra("title");
        if (title != null && !title.isEmpty()) {
            titleTextView.setText(title);
        }

        // Set back button
        ImageView backButton = findViewById(R.id.imageView2);
        backButton.setOnClickListener(v -> finish());

        // Fetch and display recipes from Firestore
        fetchRecipesFromFirestore();
    }

    private void fetchRecipesFromFirestore() {
        // Get the current category based on the page im currently in
        String category = getIntent().getStringExtra("Category");
        // if there is no category just display an error message and exit
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
                        // Get all the recipes from a certain category
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
