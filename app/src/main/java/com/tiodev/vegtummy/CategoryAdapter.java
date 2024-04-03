package com.tiodev.vegtummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myrecipe.R;
import com.tiodev.vegtummy.inventory.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList!=null ? categoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);

        TextView txtName = rootView.findViewById(R.id.name);
        TextView image = rootView.findViewById(R.id.image);

        txtName.setText(categoryList.get(position).getName());
        image.setText(categoryList.get(position).getImage());

        return rootView;
    }
}
