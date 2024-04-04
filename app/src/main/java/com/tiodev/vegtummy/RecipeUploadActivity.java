package com.tiodev.vegtummy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecipe.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiodev.vegtummy.Model.Recipe;

public class RecipeUploadActivity extends AppCompatActivity {

    private Spinner category;
    private EditText title, ingredients, description, hours, minutes;
    private Button uploadPhotoButton, uploadRecipeButton;
    private ImageView selectedImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // To hold the image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_form);

        // Initialize UI components and set up listeners
        initializeUI();
        setupButtonListeners();
    }

    private void initializeUI() {
        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        ingredients = findViewById(R.id.ingredients_txt);
        description = findViewById(R.id.description);
        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minutes);
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        uploadRecipeButton = findViewById(R.id.uploadRecipeButton);
        selectedImage = findViewById(R.id.selectedImage);

        // Populate the spinner with categories from your resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        uploadPhotoButton.setOnClickListener(v -> {
            // Intent to pick an image from the gallery
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        uploadRecipeButton.setOnClickListener(v -> handleRecipeUpload());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (selectedImage == null) {
                Log.e("RecipeUploadActivity", "selectedImage is null");
            } else {
                selectedImage.setImageURI(selectedImageUri);
            }
        }
    }

    private void handleRecipeUpload() {
        // Extract text from EditText fields
        String recipeTitle = title.getText().toString().trim();
        String recipeDescription = description.getText().toString().trim();
        String recipeIngredients = ingredients.getText().toString().trim();
        String selectedCategory = category.getSelectedItem().toString();
        String hoursText = hours.getText().toString().trim();
        String minutesText = minutes.getText().toString().trim();

        // Validation logic
        StringBuilder missingFields = new StringBuilder();
        if (recipeTitle.isEmpty()) missingFields.append("Title, ");
        if (recipeIngredients.isEmpty()) missingFields.append("Ingredients, ");
        if (recipeDescription.isEmpty()) missingFields.append("Description, ");
        if (selectedCategory.equals("Please choose a category")) missingFields.append("Category, ");
        if (hoursText.isEmpty() && minutesText.isEmpty()) missingFields.append("Cooking Time, ");

        if (missingFields.length() > 0) {
            // Remove the last comma and space
            missingFields.setLength(missingFields.length() - 2);
            Toast.makeText(getApplicationContext(), "Missing fields: " + missingFields.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Check if image has been uploaded
        if (selectedImage != null && selectedImageUri != null) {
            selectedImage.setImageURI(selectedImageUri);
        } else {
            Log.e("RecipeUploadActivity", "Selected image view or URI is null");
        }

        // For the demonstration, the image URI is directly used.
        // In a real app scenario, consider uploading the image to Firebase Storage and storing the URL in Firestore.
        String imagePath = selectedImageUri != null ? selectedImageUri.toString() : "default_image_path";

        Recipe newRecipe = new Recipe(imagePath, recipeTitle, recipeDescription, recipeIngredients, selectedCategory, calculateCookingTime(hoursText, minutesText));
        insertRecipeIntoFirestore(newRecipe);
    }

    private void insertRecipeIntoFirestore(Recipe recipe) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes").add(recipe)
                .addOnSuccessListener(documentReference -> {
                    Log.d("RecipeUpload", "Recipe added successfully");
                    Toast.makeText(RecipeUploadActivity.this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
                    navigateBackHome();
                })
                .addOnFailureListener(e -> {
                    Log.e("RecipeUpload", "Failed to add recipe", e);
                    Toast.makeText(RecipeUploadActivity.this, "Failed to add recipe", Toast.LENGTH_LONG).show();
                });
    }

    private Long calculateCookingTime(String hours, String minutes) {
        try{
            int hoursToMinutes = hours.isEmpty() ? 0 : Integer.parseInt(hours) * 60;
            int minutesToInt = minutes.isEmpty() ? 0 : Integer.parseInt(minutes);
            int cookingTime = hoursToMinutes + minutesToInt;
            return (long) cookingTime;
        }
        catch (Exception e){
            Log.e("RecipeActivity", "Failed to parse cooking time");
            return null;
        }
    }

    private void navigateBackHome() {
        // Use a delay before navigating back to allow the user to see the success message
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(RecipeUploadActivity.this, HomeActivity.class));
            finish();            // Delay before navigating back to allow the user to see the success message
        }, 2000); // 2-second delay
    }
}

