package com.example.ecommerce.adapter;

import android.content.pm.LauncherActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.entity.Product;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder> {
    private List<Product> products;
    private OnItemClickListener listener;

    public ProductAdminAdapter(List<Product> products, OnItemClickListener listener){
        this.products = products;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    @NonNull
    @Override
    public ProductAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new ProductAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdminViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(holder.imgView_item_product_image.getContext()).load(product.getImage()).into(holder.imgView_item_product_image);
        holder.txtView_item_product_name.setText(product.getName());
        holder.txtView_item_product_price.setText(product.getPrice());
        holder.txtView_item_product_store_name.setText(product.getStore().getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductAdminViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item_product_image;
        TextView txtView_item_product_name, txtView_item_product_store_name, txtView_item_product_price;
        public ProductAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_item_product_image = itemView.findViewById(R.id.imgView_item_product_image);
            txtView_item_product_name = itemView.findViewById(R.id.txtView_item_product_name);
            txtView_item_product_price = itemView.findViewById(R.id.txtView_item_product_price);
            txtView_item_product_store_name = itemView.findViewById(R.id.txtView_item_product_store_name);
        }
    }
}
