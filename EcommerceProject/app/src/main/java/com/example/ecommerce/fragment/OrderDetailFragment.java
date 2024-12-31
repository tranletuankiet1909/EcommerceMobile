package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.adapter.OrderDetailAdapter;
import com.example.ecommerce.dao.CategoryDAO;
import com.example.ecommerce.dao.OrderDAO;
import com.example.ecommerce.dao.OrderDetailDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.PayMethod;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.PurchaseOrder;
import com.example.ecommerce.entity.PurchaseOrderDetail;
import com.example.ecommerce.entity.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {
    private EditText edt_order_buyer_name, edt_order_store_name, edt_order_totalPrice;
    private RecyclerView recyclerView_orderDetail_admin;
    private Spinner spinner_status_in_order;
    private Button btn_order_edit, btn_order_delete, btn_order_exit, btn_order_done;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        edt_order_buyer_name = view.findViewById(R.id.edt_order_buyer_name);
        edt_order_store_name = view.findViewById(R.id.edt_order_store_name);
        edt_order_totalPrice = view.findViewById(R.id.edt_order_totalPrice);
        recyclerView_orderDetail_admin = view.findViewById(R.id.recyclerView_orderDetail_admin);
        spinner_status_in_order = view.findViewById(R.id.spinner_status_in_order);
        btn_order_edit = view.findViewById(R.id.btn_order_edit);
        btn_order_delete = view.findViewById(R.id.btn_order_delete);
        btn_order_exit = view.findViewById(R.id.btn_order_exit);
        btn_order_done = view.findViewById(R.id.btn_order_done);


        // set up adapter cho spinner
        List<String> orderStatusDisplayNames = new ArrayList<>();
        for (OrderStatus orderStatus: OrderStatus.values()) {
            orderStatusDisplayNames.add(orderStatus.getDisplayName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                orderStatusDisplayNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status_in_order.setAdapter(adapter);

        // khi chuyen tu admin sang
        Bundle bundleOrder = getArguments();
        PurchaseOrder order;
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO(this.getContext());
        orderDetailDAO.open();
        if (bundleOrder != null) {
            order = (PurchaseOrder) bundleOrder.getSerializable("order");
            String buyerName = order.getBuyer().getUsername();
            String storeName = order.getStore().getName();
            String totalPrice = order.getTotalPrice();
            String status = order.getStatus();
            List<PurchaseOrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getId());
            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderDetails);
            recyclerView_orderDetail_admin.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView_orderDetail_admin.setAdapter(orderDetailAdapter);
            recyclerView_orderDetail_admin.setNestedScrollingEnabled(false);

            edt_order_buyer_name.setText(buyerName);
            edt_order_store_name.setText(storeName);
            edt_order_totalPrice.setText(totalPrice);

            edt_order_store_name.setEnabled(false);
            edt_order_buyer_name.setEnabled(false);
            edt_order_totalPrice.setEnabled(false);
            spinner_status_in_order.setEnabled(false);

            int selectedIndex = orderStatusDisplayNames.indexOf(status);
            if (selectedIndex != -1) {
                spinner_status_in_order.setSelection(selectedIndex);
            }

        } else {
            order = new PurchaseOrder();
        }

        // Set event click button edit
        btn_order_edit.setOnClickListener(v -> {
            spinner_status_in_order.setEnabled(true);

            btn_order_edit.setVisibility(View.GONE);
            btn_order_delete.setVisibility(View.GONE);
            btn_order_done.setVisibility(View.VISIBLE);
            btn_order_exit.setVisibility(View.VISIBLE);
        });

        btn_order_exit.setOnClickListener(v -> {
            int selectedIndex = orderStatusDisplayNames.indexOf(order.getStatus());
            if (selectedIndex != -1) {
                spinner_status_in_order.setSelection(selectedIndex);
            }
            spinner_status_in_order.setEnabled(false);
            btn_order_done.setVisibility(View.GONE);
            btn_order_exit.setVisibility(View.GONE);
            btn_order_edit.setVisibility(View.VISIBLE);
            btn_order_delete.setVisibility(View.VISIBLE);
        });

        btn_order_done.setOnClickListener(v -> {
            OrderDAO orderDAO = new OrderDAO(this.getContext());
            orderDAO.open();
            order.setStatus(spinner_status_in_order.getSelectedItem().toString());
            boolean result = orderDAO.updateOrder(order);
            if (result) {
                Toast.makeText(this.getContext(), "Cập nhật trạng thái đơn hàng thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getContext(), "Cập nhật trang thái đơn hàng thất bại", Toast.LENGTH_SHORT).show();
            }
            spinner_status_in_order.setEnabled(false);
            orderDAO.close();
        });
        return view;
    }
}