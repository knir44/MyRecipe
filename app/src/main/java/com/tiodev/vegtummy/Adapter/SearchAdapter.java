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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Recipe> data;
    private final Context context;

    public SearchAdapter(List<Recipe> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = data.get(position);

        Glide.with(holder.img.getContext())
                .load(recipe.getImagePath())
                .placeholder(R.drawable.ic_launcher_foreground) // Use your actual placeholder drawable resource name
                .error(R.drawable.pizza_sample) // Use your actual error drawable resource name
                .into(holder.img);

        holder.title.setText(recipe.getTitle());

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

    // This method is updated to replace filterList, for clarity
    @SuppressLint("NotifyDataSetChanged")
    public void updateRecipes(List<Recipe> updatedRecipes) {
        this.data = updatedRecipes;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.search_img);
            title = itemView.findViewById(R.id.search_txt);
        }
    }
}
