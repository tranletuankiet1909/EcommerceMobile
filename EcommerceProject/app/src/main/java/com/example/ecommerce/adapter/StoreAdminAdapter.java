package com.example.ecommerce.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.entity.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdminAdapter extends RecyclerView.Adapter<StoreAdminAdapter.StoreAdminViewHolder> {
    private List<Store> stores;
    private OnItemClickListener listener;

    public StoreAdminAdapter(List<Store> stores, OnItemClickListener listener) {
        this.stores = stores;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Store store);
    }

    @NonNull
    @Override
    public StoreAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_admin, parent, false);
        return new StoreAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdminViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.txtView_item_storeName.setText(store.getName());
        holder.txtView_item_storeAddress.setText(store.getAddress());
        Glide.with(holder.imgView_item_storeImage.getContext()).load(store.getImage()).into(holder.imgView_item_storeImage);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(store);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class StoreAdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgView_item_storeImage;
        private TextView txtView_item_storeName;
        private TextView txtView_item_storeAddress;

        public StoreAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_item_storeImage = itemView.findViewById(R.id.imgView_item_store_image);
            txtView_item_storeName = itemView.findViewById(R.id.txtView_item_store_name);
            txtView_item_storeAddress = itemView.findViewById(R.id.txtView_item_store_address);
        }
    }
}
