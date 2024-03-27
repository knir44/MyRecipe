package com.tiodev.vegtummy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


    public void updateRecipes(List<Recipe> newRecipes) {
        this.data.clear(); // Clear the existing data
        this.data.addAll(newRecipes); // Add all the new data
        notifyDataSetChanged(); // Notify the adapter of the change
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = data.get(position);

        holder.title.setText(recipe.getTitle());
        Glide.with(holder.img.getContext())
                .load(recipe.getImagePath())
                .placeholder(R.drawable.category_salad) // Assuming you have a default placeholder image
                .error(R.drawable.category_salad) // Assuming you have a default error image
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("img", recipe.getImagePath());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("des", recipe.getDescription());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.txt1);
        }
    }
}
