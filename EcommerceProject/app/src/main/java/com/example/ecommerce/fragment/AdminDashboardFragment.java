package com.example.ecommerce.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.CategoryAdminAdapter;
import com.example.ecommerce.adapter.ProductAdminAdapter;
import com.example.ecommerce.adapter.StoreAdapter;
import com.example.ecommerce.adapter.StoreAdminAdapter;
import com.example.ecommerce.dao.CategoryDAO;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button btnInsert;
    private CardView cardCate, cardStore, cardProduct, cardUser, cardOrder;
    private TextView txtViewTitle;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDashboardFragment newInstance(String param1, String param2) {
        AdminDashboardFragment fragment = new AdminDashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        txtViewTitle = view.findViewById(R.id.txtView_title_recyclerView);
        recyclerView = view.findViewById(R.id.recycler_view_dashboard_admin);

        cardCate = view.findViewById(R.id.card_cate);
        cardStore = view.findViewById(R.id.card_store);
        cardProduct = view.findViewById(R.id.card_product);
        cardUser = view.findViewById(R.id.card_user);
        cardOrder = view.findViewById(R.id.card_order);

        btnInsert = view.findViewById(R.id.btnInsert_adminDashboards);
        btnInsert.setOnClickListener(v -> {
            openInsertFragment("category");
        });
        loadRecyclerViewData("category");
        setUpListeners();
        return view;
    }

    private void setUpListeners() {
        cardCate.setOnClickListener(v -> {
            loadRecyclerViewData("category");
            txtViewTitle.setText("Category");
        });
        cardStore.setOnClickListener(v -> {
            loadRecyclerViewData("store");
            txtViewTitle.setText("Store");
        });
        cardProduct.setOnClickListener(v -> {
            loadRecyclerViewData("product");
            txtViewTitle.setText("Product");

        });
        cardUser.setOnClickListener(v -> {
            loadRecyclerViewData("user");
            txtViewTitle.setText("User");
        });
        cardOrder.setOnClickListener(v -> {
            loadRecyclerViewData("order");
            txtViewTitle.setText("Order");
        });
    }

    private void loadRecyclerViewData(String type) {
        switch (type) {
            case "category":
                List<Category> categories = new ArrayList<>();
                CategoryDAO categoryDAO = new CategoryDAO(this.getContext());
                categoryDAO.open();
                categories = categoryDAO.getCategories();
                CategoryAdminAdapter cateAdapter = new CategoryAdminAdapter(categories, cate -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("cateName", cate.getName());
                    bundle.putInt("cateId", cate.getId());
                    CategoryDetailFragment categoryDetailFragment = new CategoryDetailFragment();
                    categoryDetailFragment.setArguments(bundle);

                    replaceFragment(categoryDetailFragment);
                });

                recyclerView.setAdapter(cateAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                btnInsert.setOnClickListener(v -> openInsertFragment(type));
                categoryDAO.close();
                break;
            case "store":
                List<Store> stores = new ArrayList<>();
                StoreDAO storeDAO = new StoreDAO(this.getContext());
                storeDAO.open();
                stores = storeDAO.getStores();

                StoreAdminAdapter storeAdapter = new StoreAdminAdapter(stores, store -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("storeId", store.getId());
                    bundle.putString("storeName", store.getName());
                    bundle.putString("storeImage", store.getImage());
                    bundle.putString("storeAddress", store.getAddress());
                    bundle.putInt("storeOwnerId", store.getOwner().getId());
                    StoreDetailFragment storeDetailFragment = new StoreDetailFragment();
                    storeDetailFragment.setArguments(bundle);

                    replaceFragment(storeDetailFragment);
                });

                recyclerView.setAdapter(storeAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                btnInsert.setOnClickListener(v -> openInsertFragment(type));
                storeDAO.close();
                break;
            case "product":
                List<Product> products = new ArrayList<>();
                ProductDAO productDAO = new ProductDAO(this.getContext());
                productDAO.open();
                products = productDAO.getProducts();

                ProductAdminAdapter productAdapter = new ProductAdminAdapter(products, product -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product", product);
                    ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                    productDetailFragment.setArguments(bundle);
                    replaceFragment(productDetailFragment);
                });

                recyclerView.setAdapter(productAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                btnInsert.setOnClickListener(v -> {openInsertFragment(type);});
                productDAO.close();
                break;

        }

    }

    private void openInsertFragment(String type) {
        Fragment insertFragment;
        switch (type) {
            case "category":
                insertFragment = new CategoryDetailFragment();
                break;
            case "store":
                insertFragment = new StoreDetailFragment();
                break;
            case "product":
                insertFragment = new ProductDetailFragment();
                break;
            default:
                return;
        }
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, insertFragment)
                .addToBackStack(null)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewData("category");
    }
}