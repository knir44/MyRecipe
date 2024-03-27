package com.tiodev.vegtummy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myrecipe.R; // Ensure this is your correct R class import

import java.util.Objects;

public class RecipeActivity extends AppCompatActivity {

    private boolean isImgCrop = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setupViews();
    }

    private void setupViews() {
        ImageView img = findViewById(R.id.recipe_img);
        TextView txt = findViewById(R.id.title);
        TextView ingredients = findViewById(R.id.ingredients);
        TextView time = findViewById(R.id.time);
        Button stepBtn = findViewById(R.id.steps_btn);
        Button ing_btn = findViewById(R.id.ing_btn);
        ImageView backBtn = findViewById(R.id.back_btn);
        TextView steps = findViewById(R.id.steps_txt);
        ScrollView scrollView = findViewById(R.id.ing_scroll);
        ScrollView scrollView_step = findViewById(R.id.steps);
        ImageView overlay = findViewById(R.id.image_gradient);
        ImageView zoomImage = findViewById(R.id.zoom_image);

        if (getIntent().hasExtra("img")) {
            String imageUrl = getIntent().getStringExtra("img");
            Glide.with(this).load(imageUrl).into(img);
        } else {
            // Handle missing data
        }
        txt.setText(getIntent().getStringExtra("title"));

        String[] ingList = Objects.requireNonNull(getIntent().getStringExtra("ingredients")).split("\n");
        time.setText(ingList[0]);

        for (int i = 1; i < ingList.length; i++) {
            ingredients.append("• " + ingList[i] + "\n");
        }

        steps.setText(getIntent().getStringExtra("des"));

        stepBtn.setOnClickListener(v -> toggleStepView(stepBtn, ing_btn, scrollView, scrollView_step, true));
        ing_btn.setOnClickListener(v -> toggleStepView(stepBtn, ing_btn, scrollView, scrollView_step, false));

        zoomImage.setOnClickListener(view -> toggleImageScale(img, overlay));
        backBtn.setOnClickListener(v -> {
            Log.d("RecipeActivity", "Back button clicked");
            finish();
        });
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

    private void toggleImageScale(ImageView img, ImageView overlay) {
        if (!isImgCrop) {
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            overlay.setImageAlpha(0);
            isImgCrop = true;
        } else {
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            overlay.setImageAlpha(255);
            isImgCrop = false;
        }
    }
}
