package com.example.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.entity.Category;


import java.util.List;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.CategoryAdminViewHolder> {
    private List<Category> categories;
    private OnItemClickListener listener;

    public CategoryAdminAdapter(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    @NonNull
    @Override
    public CategoryAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catgory_admin, parent, false);
        return new CategoryAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdminViewHolder holder, int position) {
        Category cate = categories.get(position);
        holder.txtView_item_category_name.setText(cate.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(cate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryAdminViewHolder extends RecyclerView.ViewHolder{

        private TextView txtView_item_category_name;
        public CategoryAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView_item_category_name = itemView.findViewById(R.id.txtView_item_category_name);
        }
    }
}
