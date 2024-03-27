package com.tiodev.vegtummy.Adapter;

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
        final Recipe temp = data.get(position);

        String[] time = temp.getIngredients().split("\n", 2); // Ensure there's a newline to split
        String displayTime = time.length > 0 ? "\uD83D\uDD50 " + time[0] : "No Time Set";
        holder.txt2.setText(displayTime);

        Glide.with(holder.img.getContext())
                .load(temp.getImagePath())
                .placeholder(R.drawable.category_main) // Ensure you have this drawable resource
                .error(R.drawable.category_salad) // Ensure you have this drawable resource
                .into(holder.img);
        holder.txt.setText(temp.getTitle());

        holder.img.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("img", temp.getImagePath());
            intent.putExtra("title", temp.getTitle());
            intent.putExtra("des", temp.getDescription());
            intent.putExtra("ingredients", temp.getIngredients());
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
        TextView txt, txt2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.popular_img);
            txt = itemView.findViewById(R.id.popular_txt);
            txt2 = itemView.findViewById(R.id.popular_time);
        }
    }
}
