package com.example.ecommerce.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.adapter.CartItemAdapter;
import com.example.ecommerce.dao.CartDAO;
import com.example.ecommerce.dao.OrderDAO;
import com.example.ecommerce.dao.OrderDetailDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.entity.PurchaseOrder;
import com.example.ecommerce.entity.PurchaseOrderDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private TextView txtViewTotalProduct, txtViewTotalPrice;
    private Button btnCheckOut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    private void updateCartSummary(CartDAO cartDAO, List<Cart> cartList) {
        int totalQuantity = 0;
        long totalPrice = 0;

        totalQuantity = cartDAO.sumOfQuantityInCartOfUser(UserManager.getInstance().getUserId());
        totalPrice = cartDAO.totalPriceInCartOfUser(UserManager.getInstance().getUserId());

        txtViewTotalProduct.setText(String.valueOf(totalQuantity));
        txtViewTotalPrice.setText(String.format("%,d", totalPrice));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        txtViewTotalPrice = view.findViewById(R.id.txtViewTotalPrice);
        txtViewTotalProduct = view.findViewById(R.id.txtViewTotalProduct);
        btnCheckOut = view.findViewById(R.id.btnCheckOut);

        CartDAO cartDAO = new CartDAO(this.getContext());
        cartDAO.open();
        List<Cart> cartList = cartDAO.getCartByUserId(UserManager.getInstance().getUserId());
        updateCartSummary(cartDAO, cartList);

        CartItemAdapter cartItemAdapter = new CartItemAdapter(cartList, () -> {
            updateCartSummary(cartDAO, cartList);
        });
        recyclerViewCart.setAdapter(cartItemAdapter);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        btnCheckOut.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xác nhận đơn hàng")
                    .setMessage("Bạn có chắc chắn muốn tạo đơn hàng cho các sản phẩm đã chọn?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        OrderDAO orderDAO = new OrderDAO(this.getContext());
                        OrderDetailDAO orderDetailDAO = new OrderDetailDAO(this.getContext());

                        orderDAO.open();
                        orderDetailDAO.open();

                        List<Cart> selectedItems = cartDAO.getSelectedCartItems(UserManager.getInstance().getUserId());

                        Map<Integer, List<Cart>> groupedByStore = new HashMap<>();
                        for (Cart cart : selectedItems) {
                            groupedByStore.computeIfAbsent(cart.getProduct().getStore().getId(), k -> new ArrayList<>()).add(cart);
                        }

                        // Duyệt qua từng cửa hàng để tạo Order và OrderDetail
                        for (Map.Entry<Integer, List<Cart>> entry : groupedByStore.entrySet()) {
                            int storeId = entry.getKey();
                            List<Cart> carts = entry.getValue();

                            // Tính tổng giá trị đơn hàng
                            long totalPrice = 0;
                            for (Cart cart : carts) {
                                totalPrice += cart.getQuantity() * Long.parseLong(cart.getProduct().getPrice());
                            }

                            // Tạo đơn hàng (Order)
                            StoreDAO storeDAO = new StoreDAO(this.getContext());
                            storeDAO.open();
                            PurchaseOrder order = new PurchaseOrder(
                                    String.valueOf(totalPrice),
                                    OrderStatus.PENDING.getDisplayName(),
                                    UserManager.getInstance().getCurrentUser(),
                                    storeDAO.getStoreById(storeId)
                            );

                            storeDAO.close();
                            long orderId = orderDAO.insertOrder(order);
                            PurchaseOrder purchaseOrder = orderDAO.getOrderById((int) orderId);

                            // Tạo chi tiết đơn hàng (OrderDetail)
                            for (Cart cart : carts) {
                                PurchaseOrderDetail detail = new PurchaseOrderDetail(
                                        cart.getQuantity(),
                                        cart.getProduct().getPrice(),
                                        cart.getProduct(),
                                        purchaseOrder
                                );
                                boolean result = orderDetailDAO.insertOrderDetail(detail);
                                if (result) {
                                    Log.d("TAG", "Them vao order detail thanh cong");
                                } else {
                                    Log.d("TAG", "Them vao order detail that bai");
                                }
                            }
                        }

                        // Xóa sản phẩm đã chọn khỏi giỏ hàng
                        boolean result = cartDAO.removeSelectedItems(UserManager.getInstance().getUserId());

                        List<Cart> updatedCartItems = cartDAO.getCartByUserId(UserManager.getInstance().getUserId()); // Lấy giỏ hàng mới
                        cartItemAdapter.updateCartItems(updatedCartItems);

                        cartDAO.close();
                        orderDAO.close();

                        orderDetailDAO.close();

                        Toast.makeText(getContext(), "Tạo đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        cartDAO.close();
        return view;
    }
}