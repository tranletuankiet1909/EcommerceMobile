package com.example.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.entity.Product;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private List<Product> wishList;

    public WishlistAdapter(List<Product> wishList) {
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_product_view, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Product p = wishList.get(position);
//        holder.mProductImageView.setImageResource(p.getImage());
        holder.mtxtViewProductName.setText(p.getName());
        holder.mtxtViewProductPrice.setText(p.getPrice());
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }


    class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mProductImageView;
        private TextView mtxtViewProductName;
        private TextView mtxtViewProductPrice;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mProductImageView = itemView.findViewById(R.id.image_product_view);
            mtxtViewProductName = itemView.findViewById(R.id.textView_ProductName);
            mtxtViewProductPrice = itemView.findViewById(R.id.textView_ProductPrice);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Tam thoi chua thuc hien", Toast.LENGTH_SHORT).show();
        }
    }
}
