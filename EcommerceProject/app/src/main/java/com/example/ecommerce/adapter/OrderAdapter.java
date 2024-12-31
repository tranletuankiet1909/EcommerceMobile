package com.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.dao.InvoiceDAO;
import com.example.ecommerce.dao.OrderDAO;
import com.example.ecommerce.dao.OrderDetailDAO;
import com.example.ecommerce.entity.Invoice;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.PayMethod;
import com.example.ecommerce.entity.PurchaseOrder;
import com.example.ecommerce.entity.PurchaseOrderDetail;
import com.example.ecommerce.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<PurchaseOrder> orders;
    private Context context;

    public OrderAdapter(List<PurchaseOrder> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PurchaseOrder order = orders.get(position);
        holder.txtView_storeName_order.setText(order.getStore().getName());
        holder.txtView_totalPrice_order.setText(order.getTotalPrice());

        OrderDetailDAO orderDetailDAO = new OrderDetailDAO(this.context);
        orderDetailDAO.open();
        List<PurchaseOrderDetail> purchaseOrderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getId());
        orderDetailDAO.close();

        holder.recyclerView_orderItem.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView_orderItem.setNestedScrollingEnabled(false);
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(purchaseOrderDetails);
        holder.recyclerView_orderItem.setAdapter(orderDetailAdapter);

        // set Adapter cho spinner payMethod
        List<String> payMethodDisplayNames = new ArrayList<>();
        for (PayMethod payMethod: PayMethod.values()) {
            payMethodDisplayNames.add(payMethod.getDisplayName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.context,
                android.R.layout.simple_spinner_item,
                payMethodDisplayNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerPayMethod.setAdapter(adapter);


        holder.btnPay.setOnClickListener(v -> {
            // Tạo hóa đơn
            String selectedPayMethod = (String) holder.spinnerPayMethod.getSelectedItem();
            Invoice invoice = new Invoice(selectedPayMethod, order);
            InvoiceDAO invoiceDAO = new InvoiceDAO(this.context);
            invoiceDAO.open();
            boolean result = invoiceDAO.insertInvoice(invoice);
            OrderDAO orderDAO= new OrderDAO(this.context);
            orderDAO.open();
            if (result) {
                //Cập nhật trạng thái đơn hàng
                order.setStatus(OrderStatus.ONGOING.getDisplayName());
                boolean resultUpdate = orderDAO.updateOrder(order);
                if (resultUpdate) {
                    orders.remove(order);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(this.context, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this.context, "Tạo hóa đơn thất bại", Toast.LENGTH_SHORT).show();
            }
            orderDAO.close();
            invoiceDAO.close();
        });

        if (order.getStatus().equals(OrderStatus.PENDING.getDisplayName())) {
            holder.btnPay.setVisibility(View.VISIBLE);
        } else if (order.getStatus().equals(OrderStatus.DONE.getDisplayName())) {
            holder.btnReview.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.GONE);
        } else {
            holder.btnPay.setVisibility(View.GONE);
            holder.btnReview.setVisibility(View.GONE);
        }



        // Set up spinner
        InvoiceDAO invoiceDAO = new InvoiceDAO(this.context);
        invoiceDAO.open();
        Invoice existingInvoice = invoiceDAO.getInvoiceOfOrder(order.getId());
        invoiceDAO.close();

        if (existingInvoice != null) {
            int selectedIndex = payMethodDisplayNames.indexOf(existingInvoice.getPayMethod());
            if (selectedIndex != -1) {
                holder.spinnerPayMethod.setSelection(selectedIndex);
            }
            holder.spinnerPayMethod.setEnabled(false); // Ngăn chỉnh sửa phương thức thanh toán
        } else {
            holder.spinnerPayMethod.setEnabled(true); // Cho phép chọn nếu chưa có hóa đơn
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtView_storeName_order, txtView_totalPrice_order;
        RecyclerView recyclerView_orderItem;
        Button btnPay, btnReview;
        Spinner spinnerPayMethod;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView_storeName_order = itemView.findViewById(R.id.txtView_storeName_order);
            txtView_totalPrice_order = itemView.findViewById(R.id.txtView_totalPrice_order);
            recyclerView_orderItem = itemView.findViewById(R.id.recyclerView_orderItem);
            btnPay = itemView.findViewById(R.id.btnPay);
            btnReview = itemView.findViewById(R.id.btnReview);
            spinnerPayMethod = itemView.findViewById(R.id.spinnerPayMethod);
        }
    }
}
