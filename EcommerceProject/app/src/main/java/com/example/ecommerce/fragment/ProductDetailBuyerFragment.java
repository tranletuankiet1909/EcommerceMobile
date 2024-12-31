package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.dao.CartDAO;
import com.example.ecommerce.dao.FavoriteDAO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailBuyerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailBuyerFragment extends Fragment {
    private ImageView imgProduct, imgView_store_product;
    private TextView txtViewProductName, txtViewProductPrice,
            txtViewProductDescription, txtViewProductQuantity,
            txtView_storeName_product, txtView_storeAddress_product;
    private RecyclerView recyclerViewComments;
    private Button btnAddToCart;
    private CardView card_store;
    private ImageButton imageButton_favorite;
    private static final String TAG = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductDetailBuyerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailBuyerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailBuyerFragment newInstance(String param1, String param2) {
        ProductDetailBuyerFragment fragment = new ProductDetailBuyerFragment();
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
    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail_buyer, container, false);
        imgProduct = view.findViewById(R.id.imgProduct);
        imgView_store_product = view.findViewById(R.id.imgView_store_product);
        txtViewProductName = view.findViewById(R.id.text_viewProductName);
        txtViewProductDescription = view.findViewById(R.id.text_viewProductDescription);
        txtViewProductPrice = view.findViewById(R.id.text_viewProductPrice);
        txtViewProductQuantity = view.findViewById(R.id.text_viewProductQuantity);
        txtView_storeName_product = view.findViewById(R.id.txtView_storeName_product);
        txtView_storeAddress_product = view.findViewById(R.id.txtView_storeAddress_product);

        imageButton_favorite = view.findViewById(R.id.button_favorite_productDetail);
        card_store = view.findViewById(R.id.card_store_product);
        recyclerViewComments = view.findViewById(R.id.recyclerViewComments);
        btnAddToCart = view.findViewById(R.id.btn_addToCart);

        Product product;
        Bundle bundle = getArguments();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            txtViewProductName.setText(product.getName());
            txtViewProductPrice.setText(product.getPrice());
            txtViewProductDescription.setText(product.getDescription());
            txtViewProductQuantity.setText(String.valueOf(product.getQuantity()));
            Glide.with(imgProduct.getContext()).load(product.getImage()).into(imgProduct);
            Glide.with(imgView_store_product.getContext()).load(product.getStore().getImage()).into(imgView_store_product);
            txtView_storeName_product.setText(product.getStore().getName());
            txtView_storeAddress_product.setText(product.getStore().getAddress());
        } else {
            product = new Product();
        }
        card_store.setOnClickListener(v -> {
            Bundle bundleStore = new Bundle();
            bundleStore.putSerializable("store", product.getStore());
            StoreDetailBuyerFragment storeDetailBuyerFragment = new StoreDetailBuyerFragment();
            storeDetailBuyerFragment.setArguments(bundleStore);
            replaceFragment(storeDetailBuyerFragment);
        });
        FavoriteDAO favoriteDAO = new FavoriteDAO(this.getContext());
        favoriteDAO.open();
        boolean isFavorite = favoriteDAO.isFavorite(product.getId(), UserManager.getInstance().getUserId());

        if (isFavorite) {
            imageButton_favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            imageButton_favorite.setImageResource(R.drawable.ic_outlined_favorite_24);
        }

        imageButton_favorite.setOnClickListener(v -> {
            int userId = UserManager.getInstance().getUserId();
            int productId = product.getId();

            if (favoriteDAO.isFavorite(productId, userId)) {
                boolean result = favoriteDAO.deleteProductFromWishList(productId, userId);
                if (result) {
                    imageButton_favorite.setImageResource(R.drawable.ic_outlined_favorite_24);
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean result = favoriteDAO.insertProductToWishlist(productId, userId);
                Log.d(TAG, "userId: " + String.valueOf(userId) + " productId: " + String.valueOf(productId));
                if (result) {
                    imageButton_favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                } else {
                    Toast.makeText(getContext(), "Thêm vào yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            Cart cart = new Cart(1, true, UserManager.getInstance().getCurrentUser(), product);
            CartDAO cartDAO = new CartDAO(this.getContext());
            cartDAO.open();
            if (cartDAO.isExistProductInCartOfUser(UserManager.getInstance().getCurrentUser().getId(), product.getId())) {
                Cart newCart = cartDAO.getCartByUserAndProduct(UserManager.getInstance().getCurrentUser().getId(), product.getId());
                newCart.setQuantity(newCart.getQuantity()+1);
                boolean result = cartDAO.updateCart(newCart);
                if (result) {
                    Toast.makeText(this.getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean result = cartDAO.insertCart(cart);
                if (result) {
                    Toast.makeText(this.getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            cartDAO.close();
        });

        return view;
    }
}