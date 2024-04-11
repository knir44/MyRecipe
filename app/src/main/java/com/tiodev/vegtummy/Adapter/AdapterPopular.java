package com.tiodev.vegtummy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class AdapterPopular extends RecyclerView.Adapter<AdapterPopular.MyViewHolder> {

    private final List<Recipe> data;
    private final Context context;

    public AdapterPopular(List<Recipe> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Recipe recipe = data.get(position);

        // Display cooking time
        Log.d("AdapterPopular", "Cooking time: " + recipe.getCookingTime());
        holder.cookingTime.setText(recipe.getCookingTime());

        // Display Image
        Glide.with(holder.img.getContext())
                .load(recipe.getImagePath())
                .placeholder(R.drawable.popular_placeholder) // Ensure you have this drawable resource
                .error(R.drawable.popular_placeholder) // Ensure you have this drawable resource
                .into(holder.img);

        // Set Title
        holder.title.setText(recipe.getTitle());

        // Add event listeners
        holder.img.setOnClickListener(v -> {
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, cookingTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.popular_img);
            title = itemView.findViewById(R.id.popular_txt);
            cookingTime = itemView.findViewById(R.id.popular_time);
        }
    }
}
