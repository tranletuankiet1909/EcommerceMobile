package com.example.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.PurchaseOrder;

import java.util.List;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderAdminViewHolder> {
    private List<PurchaseOrder> orders;
    private OnItemClickListener listener;

    public OrderAdminAdapter(List<PurchaseOrder> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PurchaseOrder purchaseOrder);
    }

    @NonNull
    @Override
    public OrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
        return new OrderAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdminViewHolder holder, int position) {
        PurchaseOrder order = orders.get(position);
        holder.txtView_item_order_store_name.setText(order.getStore().getName());
        holder.txtView_item_order_buyer_name.setText(order.getBuyer().getUsername());
        holder.txtView_item_order_status.setText(order.getStatus());
        holder.txtView_item_order_totalprice.setText(order.getTotalPrice());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderAdminViewHolder extends RecyclerView.ViewHolder{
        private TextView txtView_item_order_store_name, txtView_item_order_buyer_name, txtView_item_order_totalprice, txtView_item_order_status;

        public OrderAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView_item_order_store_name = itemView.findViewById(R.id.txtView_item_order_store_name);
            txtView_item_order_buyer_name = itemView.findViewById(R.id.txtView_item_order_buyer_name);
            txtView_item_order_totalprice = itemView.findViewById(R.id.txtView_item_order_totalprice);
            txtView_item_order_status = itemView.findViewById(R.id.txtView_item_order_status);
        }
    }
}
