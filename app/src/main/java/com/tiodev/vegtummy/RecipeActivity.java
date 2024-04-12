package com.tiodev.vegtummy;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myrecipe.R;


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
        ingBtn.setTextColor(getColor(R.color.textOnPrimary));
        stepBtn.setBackground(null);
        stepBtn.setTextColor(getColor(R.color.textColor));

        LottieAnimationView lottieImageLoading = findViewById(R.id.lottie_image_loading);

        // Start with the animation view visible
        lottieImageLoading.setVisibility(View.VISIBLE);


        /* Set the image - if the Uri is invalid or the image cannot be loaded, a placeholder based
        on the category will be displayed */
        String category = getIntent().getStringExtra("category");
        Glide.with(getApplicationContext())
                .load(Uri.parse(getIntent().getStringExtra("img")))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        lottieImageLoading.setVisibility(View.GONE);  // Hide Lottie animation on load failure
                        return false;  // let Glide handle the placeholder
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        lottieImageLoading.setVisibility(View.GONE);  // Hide Lottie animation on resource ready
                        return false;  // let Glide display the image
                    }
                })
                .placeholder(getPlaceholderImage(category))  // Continue using static placeholder if needed
                .error(getPlaceholderImage(category))
                .into(img);

        // Set the title
        title.setText(getIntent().getStringExtra("title"));

        // Set the ingredients
        ingredients.setText(getIntent().getStringExtra("ingredients"));

        // Set the time
        time.setText(getIntent().getStringExtra("cookingTime"));

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
            stepBtn.setTextColor(getColor(R.color.textOnPrimary));
            ing_btn.setBackground(null);
            ing_btn.setTextColor(getColor(R.color.textColor));
            scrollView.setVisibility(View.GONE);
            scrollView_step.setVisibility(View.VISIBLE);
        } else {
            ing_btn.setBackgroundResource(R.drawable.btn_ing);
            ing_btn.setTextColor(getColor(R.color.textOnPrimary));
            stepBtn.setBackground(null);
            stepBtn.setTextColor(getColor(R.color.textColor));
            scrollView.setVisibility(View.VISIBLE);
            scrollView_step.setVisibility(View.GONE);
        }
    }
}
