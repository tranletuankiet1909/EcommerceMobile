package com.example.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.entity.PurchaseOrderDetail;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>{
    private List<PurchaseOrderDetail> orderDetails;

    public OrderDetailAdapter(List<PurchaseOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        PurchaseOrderDetail purchaseOrderDetail = orderDetails.get(position);
        holder.txtView_productName_orderDetail.setText(purchaseOrderDetail.getProduct().getName());
        holder.txtView_productQuantity_orderDetail.setText( String.valueOf(purchaseOrderDetail.getQuantity()) );
        holder.txtView_totalPrice_orderDetail.setText(purchaseOrderDetail.getPrice());
        Glide.with(holder.imgView_productImage_orderDetail.getContext()).load(purchaseOrderDetail.getProduct().getImage()).into(holder.imgView_productImage_orderDetail);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

//    public void updateData(List<PurchaseOrderDetail> newOrderDetails) {
//        this.orderDetails.clear();
//        this.orderDetails.addAll(newOrderDetails);
//        notifyDataSetChanged();
//    }


    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_productImage_orderDetail;
        TextView txtView_productName_orderDetail, txtView_productQuantity_orderDetail, txtView_totalPrice_orderDetail;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_productImage_orderDetail = itemView.findViewById(R.id.imgView_productImage_orderDetail);
            txtView_productName_orderDetail = itemView.findViewById(R.id.txtView_productName_orderDetail);
            txtView_productQuantity_orderDetail = itemView.findViewById(R.id.txtView_productQuantity_orderDetail);
            txtView_totalPrice_orderDetail = itemView.findViewById(R.id.txtView_totalPrice_orderDetail);
        }
    }
}
