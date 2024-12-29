package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Store;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreDetailBuyerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreDetailBuyerFragment extends Fragment {
    private ImageView imgStore;
    private TextView txtViewStoreName, txtViewStoreAddress;
    private RecyclerView recyclerViewProducts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreDetailBuyerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreDetailBuyerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreDetailBuyerFragment newInstance(String param1, String param2) {
        StoreDetailBuyerFragment fragment = new StoreDetailBuyerFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_detail_buyer, container, false);
        imgStore = view.findViewById(R.id.imgStore_detail);
        txtViewStoreName = view.findViewById(R.id.tvStoreName);
        txtViewStoreAddress = view.findViewById(R.id.tvStoreAddress);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);

        Bundle bundleReceive = getArguments();
        Store store = (Store) bundleReceive.getSerializable("store");
        Glide.with(imgStore.getContext()).load(store.getImage()).into(imgStore);
        txtViewStoreName.setText(store.getName());
        txtViewStoreAddress.setText(store.getAddress());

        ProductDAO productDAO = new ProductDAO(this.getContext());
        productDAO.open();
        List<Product> products = productDAO.getProductsByStore(store.getId());
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                Log.d("Product Check", "Product ID: " + product.getId() + ", Name: " + product.getName());
            }
        } else {
            Log.d("Product Check", "No products found for the store.");
        }

        productDAO.close();

        ProductAdapter productAdapter = new ProductAdapter(products, product -> {
            Bundle bundleProduct = new Bundle();
            bundleProduct.putSerializable("product", product);
            ProductDetailBuyerFragment productDetailBuyerFragment = new ProductDetailBuyerFragment();
            productDetailBuyerFragment.setArguments(bundleProduct);
            replaceFragment(productDetailBuyerFragment);
        }, false);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recyclerViewProducts.setNestedScrollingEnabled(false);
        recyclerViewProducts.setAdapter(productAdapter);
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}