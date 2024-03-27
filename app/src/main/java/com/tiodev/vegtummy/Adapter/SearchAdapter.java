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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiodev.vegtummy.R;
import com.tiodev.vegtummy.RecipeActivity;
import com.tiodev.vegtummy.RoomDB.Recipe;

import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.Searchviewholder>{

    List<Recipe> data;
    Context context;


    public SearchAdapter(List<Recipe> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Searchviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new Searchviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Searchviewholder holder, int position) {
        final Recipe temp = data.get(position);

        Glide.with(holder.img.getContext()).load(data.get(position).getImg()).into(holder.img);
        holder.txt.setText(data.get(position).getTitle());
        holder.item.setOnClickListener(v ->{
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra("img", temp.getImg());
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

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Recipe> filterList){
        data = filterList;
        notifyDataSetChanged();
    }

    static class Searchviewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;
        ConstraintLayout item;

        public Searchviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.search_img);
            txt = itemView.findViewById(R.id.search_txt);
            item = itemView.findViewById(R.id.search_item);
        }
    }


}
