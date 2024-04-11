package com.tiodev.vegtummy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myrecipe.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.tiodev.vegtummy.Model.Recipe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RecipeUploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private Spinner category;
    private EditText title, ingredients, description;
    private EditText hours, minutes;
    private FloatingActionButton uploadRecipeButton;
    private FloatingActionButton uploadPhotoButton;
    private ImageView selectedImage;
    private Button btnPickTime;
    private TextView textViewPrepTime;


    private Uri selectedImageUri;

    FirebaseStorage storage;
    StorageReference storageReference;

    public RecipeUploadActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_form);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
        FloatingActionButton clearAllButton = findViewById(R.id.clear_text);
        selectedImage = findViewById(R.id.selectedImage);

        // Set back button
        ImageView backButton = findViewById(R.id.imageView2);
        backButton.setOnClickListener(v -> finish());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        clearAllButton.setOnClickListener(v -> clearAll());
    }
    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // This time picker will be in 24-hour format
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    // The format string "%02d:%02d" already does not include AM/PM
                    // This will display time in 24-hour format, e.g., "14:45" for 2:45 PM
                    String format = "%02d:%02d";
                    @SuppressLint("DefaultLocale") String time = String.format(format, hourOfDay, minuteOfHour);

                    // Update the TextView to display the selected time
                    if (textViewPrepTime != null) {
                        textViewPrepTime.setText(time);
                    }
                }, hour, minute, true); // Set to 'true' for 24-hour format

        timePickerDialog.show();
    }

    private void clearAll() {
        category.setSelection(0);
        title.setText("");
        ingredients.setText("");
        description.setText("");
        hours.setText("");
        minutes.setText("");
        selectedImage.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    private void setupButtonListeners() {
        uploadPhotoButton.setOnClickListener(v -> promptImageUploadOptions());
        uploadRecipeButton.setOnClickListener(v -> attemptRecipeUpload());
    }

    private void promptImageUploadOptions() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeUploadActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                openCamera();
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void attemptRecipeUpload() {
        if (!validateRecipeInputs()) {
            return;
        }

        if (selectedImageUri != null) {
            uploadImageAndRecipeDetails();
        } else {
            handleRecipeUpload("default_image_path");
        }
    }

    private boolean validateRecipeInputs() {
        String recipeTitle = title.getText().toString().trim();
        String recipeIngredients = ingredients.getText().toString().trim();
        String recipeDescription = description.getText().toString().trim();
        String selectedCategory = category.getSelectedItem().toString();
        String hoursText = hours.getText().toString().trim();
        String minutesText = minutes.getText().toString().trim();

        StringBuilder missingFields = new StringBuilder();
        if (recipeTitle.isEmpty()) missingFields.append("Title, ");
        if (recipeIngredients.isEmpty()) missingFields.append("Ingredients, ");
        if (recipeDescription.isEmpty()) missingFields.append("Description, ");
        if (selectedCategory.equals("Please choose a category")) missingFields.append("Category, ");
        if (hoursText.isEmpty() && minutesText.isEmpty()) missingFields.append("Cooking Time, ");

        if (missingFields.length() > 0) {
            missingFields.setLength(missingFields.length() - 2);
            Toast.makeText(getApplicationContext(), "Missing fields: " + missingFields.toString(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void uploadImageAndRecipeDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(selectedImageUri)
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int) progress);
                })
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    progressDialog.dismiss();
                    handleRecipeUpload(uri.toString());
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e("RecipeUploadActivity", "Upload Failed: " + e.getMessage());
                    Toast.makeText(RecipeUploadActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void handleRecipeUpload(String imageUrl) {
        Recipe newRecipe = new Recipe(imageUrl,
                title.getText().toString().trim(),
                description.getText().toString().trim(),
                ingredients.getText().toString().trim(),
                category.getSelectedItem().toString(),
                calculateCookingTime(hours.getText().toString().trim(), minutes.getText().toString().trim()));

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
        try {
            int hoursToMinutes = hours.isEmpty() ? 0 : Integer.parseInt(hours) * 60;
            int minutesToInt = minutes.isEmpty() ? 0 : Integer.parseInt(minutes);
            int cookingTime = hoursToMinutes + minutesToInt;
            return (long) cookingTime;
        } catch (Exception e) {
            Log.e("RecipeActivity", "Failed to parse cooking time");
            return null;
        }
    }

    private void navigateBackHome() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(RecipeUploadActivity.this, HomeActivity.class));
            finish();
        }, 2000); // 2-second delay
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                selectedImageUri = FileProvider.getUriForFile(this,
                        "com.example.myrecipe.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.e("RecipeUploadActivity", "Error occurred while creating the file", e);
        }
        return image;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedImageUri != null) {
            outState.putString("cameraImageUri", selectedImageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            selectedImageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                selectedImageUri = data != null ? data.getData() : null;
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // No need to get data from Intent. selectedImageUri is already set.
            }

            if (selectedImageUri != null) {
                selectedImage.setImageURI(selectedImageUri);
            } else {
                Log.e("RecipeUploadActivity", "Selected image URI is null");
            }
        }
    }
}
