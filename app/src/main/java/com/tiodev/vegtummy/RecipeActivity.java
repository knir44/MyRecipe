package com.tiodev.vegtummy;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myrecipe.R; // Ensure this is your correct R class import
import com.squareup.picasso.Picasso;
import com.tiodev.vegtummy.Model.Recipe;


public class RecipeActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setupViews();
    }

    @SuppressLint("SetTextI18n")
    private void setupViews() {
        // Initializing components
        ImageView img = findViewById(R.id.recipe_img);
        ImageView backBtn = findViewById(R.id.back_btn);

        TextView title = findViewById(R.id.title);
        TextView ingredients = findViewById(R.id.ingredients_txt);
        TextView time = findViewById(R.id.time);
        TextView steps = findViewById(R.id.steps_txt);

        Button stepBtn = findViewById(R.id.steps_btn);
        Button ingBtn = findViewById(R.id.ing_btn);

        ScrollView scrollView = findViewById(R.id.ing_scroll);
        ScrollView scrollView_step = findViewById(R.id.steps);

        // Set the ingBtn as the selected default
        ingBtn.setBackgroundResource(R.drawable.btn_ing);
        ingBtn.setTextColor(getColor(R.color.white));
        stepBtn.setBackground(null);
        stepBtn.setTextColor(getColor(R.color.black));

        /* Set the image - if the Uri is invalid or the image cannot be loaded, a placeholder based
        on the category will be displayed */
        String category = getIntent().getStringExtra("category");
        Glide.with(getApplicationContext())
                .load(Uri.parse(getIntent().getStringExtra("img")))
                .placeholder(getPlaceholderImage(category))
                .error(getPlaceholderImage(category))
                .into(img);

        // Set the title
        title.setText(getIntent().getStringExtra("title"));

        // Set the ingredients
        ingredients.setText(getIntent().getStringExtra("ingredients"));

        // Set the time
        long cookingTime = getIntent().getLongExtra("cookingTime", -1);
        if(cookingTime != -1) {
            time.setText(cookingTime + " minutes");
        } else {
            time.setText("N/A");
        }

        // Set the instructions for the recipe
        steps.setText(getIntent().getStringExtra("description"));

        // Add event listeners
        stepBtn.setOnClickListener(v -> toggleStepView(stepBtn, ingBtn, scrollView, scrollView_step, true));
        ingBtn.setOnClickListener(v -> toggleStepView(stepBtn, ingBtn, scrollView, scrollView_step, false));
        backBtn.setOnClickListener(v -> finish());
    }

    private int getPlaceholderImage(String category) {
        if (category == null) {
            return R.drawable.default_placeholder;
        }
        switch (category) {
            case "Salad":
                return R.drawable.category_salad;
            case "Main Dish":
                return R.drawable.category_main;
            case "Drinks":
                return R.drawable.catergory_drinks;
            case "Dessert":
                return R.drawable.category_dessert;
            case "Popular":
                return R.drawable.popular_placeholder;
            default:
                return R.drawable.default_placeholder;
        }
    }

    private void toggleStepView(Button stepBtn, Button ing_btn, ScrollView scrollView, ScrollView scrollView_step, boolean showSteps) {
        if (showSteps) {
            stepBtn.setBackgroundResource(R.drawable.btn_ing);
            stepBtn.setTextColor(getColor(R.color.white));
            ing_btn.setBackground(null);
            ing_btn.setTextColor(getColor(R.color.black));
            scrollView.setVisibility(View.GONE);
            scrollView_step.setVisibility(View.VISIBLE);
        } else {
            ing_btn.setBackgroundResource(R.drawable.btn_ing);
            ing_btn.setTextColor(getColor(R.color.white));
            stepBtn.setBackground(null);
            stepBtn.setTextColor(getColor(R.color.black));
            scrollView.setVisibility(View.VISIBLE);
            scrollView_step.setVisibility(View.GONE);
        }
    }
}
