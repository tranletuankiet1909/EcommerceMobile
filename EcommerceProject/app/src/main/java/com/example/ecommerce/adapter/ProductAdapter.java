package com.example.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private OnItemClickListener listener;
    private boolean showFavoriteButton;

    public ProductAdapter(List<Product> products, OnItemClickListener listener, boolean showFavoriteButton) {
        this.products = products;
        this.listener = listener;
        this.showFavoriteButton = showFavoriteButton;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_product_view, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);
        Picasso.get().load(p.getImage()).into(holder.mProductImageView);
        holder.mtxtViewProductName.setText(p.getName());
        holder.mtxtViewProductPrice.setText(p.getPrice()+" VND");
        holder.btnFavorite.setVisibility(showFavoriteButton ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView mProductImageView;
        private TextView mtxtViewProductName;
        private TextView mtxtViewProductPrice;
        private ImageButton btnFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mProductImageView = itemView.findViewById(R.id.image_product_view);
            mtxtViewProductName = itemView.findViewById(R.id.textView_ProductName);
            mtxtViewProductPrice = itemView.findViewById(R.id.textView_ProductPrice);
            btnFavorite = itemView.findViewById(R.id.button_favorite);

        }

    }

    public void updateProductList(List<Product> newProductList) {
        this.products = newProductList;
        notifyDataSetChanged(); // Làm mới RecyclerView
    }

    public void setShowFavoriteButton(boolean showFavoriteButton) {
        this.showFavoriteButton = showFavoriteButton;
        notifyDataSetChanged(); // Làm mới RecyclerView để áp dụng thay đổi
    }
}
