package com.tiodev.vegtummy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.tiodev.vegtummy.Model.Recipe;
import com.example.myrecipe.R;
import com.tiodev.vegtummy.RecipeActivity;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<Recipe> data;
    private final Context context;

    public Adapter(List<Recipe> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_design, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Recipe> newRecipes) {
        this.data.clear(); // Clear the existing data
        this.data.addAll(newRecipes); // Add all new data
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateRecipes(List<Recipe> newRecipes) {
        this.data.clear(); // Clear the existing data
        this.data.addAll(newRecipes); // Add all the new data
        notifyDataSetChanged(); // Notify the adapter of the change
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current recipe from the data
        Recipe recipe = data.get(position);

        // Set title
        holder.title.setText(recipe.getTitle());

        // Set time
        holder.time.setText(recipe.getCookingTime());

        // Set image
        int placeholderImage = getPlaceholderImage(recipe.getCategory());
        Glide.with(holder.img.getContext())
                .load(recipe.getImagePath())
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .into(holder.img);

        // Add event listeners
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("img", recipe.getImagePath().toString());
            intent.putExtra("description", recipe.getDescription());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("cookingTime", recipe.getCookingTime());
            intent.putExtra("category", recipe.getCategory());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    private int getPlaceholderImage(String category) {
        switch (category) {
            case "Salad":
                return R.drawable.category_salad;
            case "Main Dish":
                return R.drawable.category_main;
            case "Drinks":
                return R.drawable.catergory_drinks;
            case "Dessert":
                return R.drawable.category_dessert;
            default:
                return R.drawable.default_placeholder;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // When showing all the recipes in a category
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.itemTitle);
            time = itemView.findViewById(R.id.time);
        }
    }
}
