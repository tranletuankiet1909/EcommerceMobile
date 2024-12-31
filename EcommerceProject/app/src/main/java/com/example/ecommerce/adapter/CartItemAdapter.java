package com.example.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.dao.CartDAO;
import com.example.ecommerce.entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<Cart> carts;
    private OnCartChangeListener cartChangeListener;
    public CartItemAdapter(List<Cart> carts, OnCartChangeListener listener) {
        this.carts = carts;
        this.cartChangeListener = listener;
    }

    public void updateCartItems(List<Cart> updatedCarts) {
        this.carts = updatedCarts; // Cập nhật danh sách giỏ hàng
        notifyDataSetChanged(); // Thông báo adapter làm mới giao diện
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.cb_cart_item.setChecked(cart.getSelected());
        Glide.with(holder.imgView_cart_item_product.getContext()).load(cart.getProduct().getImage()).into(holder.imgView_cart_item_product);
        holder.txtView_cart_item_storeName.setText(cart.getProduct().getStore().getName());
        holder.txtView_cart_item_productName.setText(cart.getProduct().getName());
        holder.txtView_cart_item_productPrice.setText(cart.getProduct().getPrice());
        holder.txtView_cart_item_quantity.setText(String.valueOf(cart.getQuantity()));
        holder.btn_cart_item_minus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.txtView_cart_item_quantity.getText().toString());
            if (quantity > 1) {
                quantity--;
            }
            holder.txtView_cart_item_quantity.setText(String.valueOf(quantity));
            CartDAO cartDAO = new CartDAO(v.getContext());
            cartDAO.open();
            cart.setQuantity(quantity);
            boolean result = cartDAO.updateCart(cart);
            if (result) {
                Toast.makeText(v.getContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
            }
            cartDAO.close();
            notifyItemChanged(position);
            if (cartChangeListener != null) {
                cartChangeListener.onCartChanged();
            }
        });

        holder.btn_cart_item_plus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.txtView_cart_item_quantity.getText().toString());
            quantity++;
            holder.txtView_cart_item_quantity.setText(String.valueOf(quantity));
            CartDAO cartDAO = new CartDAO(v.getContext());
            cartDAO.open();
            cart.setQuantity(quantity);
            boolean result = cartDAO.updateCart(cart);
            if (result) {
                Toast.makeText(v.getContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
            }
            cartDAO.close();
            notifyItemChanged(position);
            if (cartChangeListener != null) {
                cartChangeListener.onCartChanged();
            }
        });

        holder.cb_cart_item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cart.setSelected(isChecked);
            CartDAO cartDAO = new CartDAO(holder.itemView.getContext());
            cartDAO.open();
            boolean result = cartDAO.updateCart(cart);
            if (!result) {
                Toast.makeText(holder.itemView.getContext(), "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
            if (cartChangeListener != null) {
                cartChangeListener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtView_cart_item_storeName, txtView_cart_item_productName,
                txtView_cart_item_productPrice, txtView_cart_item_quantity;
        CheckBox cb_cart_item;
        ImageView imgView_cart_item_product;
        Button btn_cart_item_minus, btn_cart_item_plus;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView_cart_item_storeName = itemView.findViewById(R.id.txtView_cart_item_storeName);
            txtView_cart_item_productName = itemView.findViewById(R.id.txtView_cart_item_productName);
            txtView_cart_item_productPrice = itemView.findViewById(R.id.txtView_cart_item_productPrice);
            txtView_cart_item_quantity = itemView.findViewById(R.id.txtView_cart_item_quantity);
            cb_cart_item = itemView.findViewById(R.id.cb_cart_item);
            imgView_cart_item_product = itemView.findViewById(R.id.imgView_cart_item_product);
            btn_cart_item_minus = itemView.findViewById(R.id.btn_cart_item_minus);
            btn_cart_item_plus = itemView.findViewById(R.id.btn_cart_item_plus);

        }
    }
}
