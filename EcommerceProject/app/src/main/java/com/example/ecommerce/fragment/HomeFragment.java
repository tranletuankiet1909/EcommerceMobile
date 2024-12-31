package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.BannerAdapter;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.adapter.StoreAdapter;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.dao.StoreDAO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.Store;
import com.example.ecommerce.entity.User;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView brandRecyclerView;
    private List<Product> productList;
    private List<Store> storeList;
    private List<Integer> bannerImages;
    private ProductAdapter productAdapter;
    private StoreAdapter storeAdapter;


    private ViewPager2 bannerCarousel;
    private ConstraintLayout storeImageContainer;
    private ImageView selectedStoreImage;
    private ImageView closeButton;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setNestedScrollingEnabled(false);

        brandRecyclerView = view.findViewById(R.id.brandRecyclerView);
        brandRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        brandRecyclerView.setNestedScrollingEnabled(false);

        MaterialSearchBar searchBar = view.findViewById(R.id.searchBar_home);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    // Nếu tìm kiếm bị hủy, hiển thị danh sách đầy đủ sản phẩm
                    productAdapter.updateProductList(productList);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchProducts(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        // Hiển thị danh sách sản phẩm
        ProductDAO productDAO = new ProductDAO(getContext());
        productDAO.open();
        productList = productDAO.getProducts();
        productDAO.close();

        productAdapter = new ProductAdapter(productList, product -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            ProductDetailBuyerFragment productDetailBuyerFragment = new ProductDetailBuyerFragment();
            productDetailBuyerFragment.setArguments(bundle);
            replaceFragment(productDetailBuyerFragment);
        }, false);
        recyclerView.setAdapter(productAdapter);



        // Hiển thị danh sách cửa hàng
        StoreDAO storeDAO = new StoreDAO(getContext());
        storeDAO.open();
        storeList = storeDAO.getStores();
        storeDAO.close();


        storeImageContainer = view.findViewById(R.id.storeImageContainer);
        selectedStoreImage = view.findViewById(R.id.selectedStoreImage);
        closeButton = view.findViewById(R.id.closeButton);

        storeAdapter = new StoreAdapter(storeList, this.getContext(), s -> {
            // Hiển thị container store image
            storeImageContainer.setVisibility(View.VISIBLE);
            Picasso.get().load(s.getImage()).into(selectedStoreImage);
            // Lọc sản phẩm theo cửa hàng
            filterProductsByStore(s);
            // Ẩn RecyclerView hiển thị tất cả các cửa hàng
            brandRecyclerView.setVisibility(View.GONE);
        });
        brandRecyclerView.setAdapter(storeAdapter);

        closeButton.setOnClickListener(v -> {
            // Ẩn container store image
            storeImageContainer.setVisibility(View.GONE);

            // Hiển thị lại danh sách tất cả các cửa hàng
            brandRecyclerView.setVisibility(View.VISIBLE);

            // Hiển thị lại danh sách đầy đủ các sản phẩm
            productAdapter.updateProductList(productList);
        });


        // Hiển thị banner
        bannerCarousel = view.findViewById(R.id.bannerCarousel_home);

        // Danh sách hình ảnh
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.img); // Thay bằng hình ảnh của bạn
        bannerImages.add(R.drawable.img);
        bannerImages.add(R.drawable.img);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerCarousel.setAdapter(bannerAdapter);

        // Cấu hình AutoScroll (nếu muốn carousel tự động chạy)
        autoScrollCarousel();

        return view;
    }
    private void filterProductsByStore(Store store) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : productList) {
            if (product.getStore().getId() == store.getId()) {
                filteredProducts.add(product);
            }
        }
        productAdapter.updateProductList(filteredProducts);
        Toast.makeText(this.getContext(), "Đang hiển thị sản phẩm từ: " + store.getName(), Toast.LENGTH_SHORT).show();
    }

    private void autoScrollCarousel() {
        bannerCarousel.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = bannerCarousel.getCurrentItem();
                int nextItem = currentItem + 1 < bannerImages.size() ? currentItem + 1 : 0;
                bannerCarousel.setCurrentItem(nextItem, true);
                // Lặp lại
                bannerCarousel.postDelayed(this, 3000);
            }
        }, 3000);
    }
    private void searchProducts(String query) {
        // Làm sạch danh sách cũ
        List<Product> filteredList = new ArrayList<>();

        // Lọc danh sách dựa trên từ khóa
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }

        // Kiểm tra kết quả
        if (filteredList.isEmpty()) {
            Toast.makeText(this.getContext(), "Không tìm thấy sản phẩm phù hợp!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), "Tìm thấy " + filteredList.size() + " sản phẩm!", Toast.LENGTH_SHORT).show();
        }

        // Cập nhật danh sách hiển thị
        productAdapter.updateProductList(filteredList);
    }
    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}