package com.example.ecommerce.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.entity.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private List<Store> stores;
    private Context context;
    private OnStoreClickListener listener;

    public StoreAdapter(List<Store> storeList, Context context, OnStoreClickListener listener) {
        this.stores = storeList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store s = stores.get(position);
        Picasso.get().load(s.getImage()).into(holder.imgViewStoreImage);
        holder.txtViewStoreName.setText(s.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoreClick(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView txtViewStoreName;
        private ImageView imgViewStoreImage;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewStoreName = itemView.findViewById(R.id.storeName);
            imgViewStoreImage = itemView.findViewById(R.id.storeImage);
        }

    }
    public interface OnStoreClickListener {
        void onStoreClick(Store store);
    }


}

