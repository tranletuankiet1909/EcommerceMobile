package com.example.ecommerce.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.adapter.WishlistAdapter;
import com.example.ecommerce.dao.FavoriteDAO;
import com.example.ecommerce.dao.ProductDAO;
import com.example.ecommerce.entity.Favorite;
import com.example.ecommerce.entity.Product;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment {
    private RecyclerView recycler_wishlist;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
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
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recycler_wishlist = view.findViewById(R.id.recycler_view_wishlist);

        List<Favorite> wishlist = new ArrayList<>();
        FavoriteDAO favoriteDAO = new FavoriteDAO(this.getContext());
        favoriteDAO.open();
        wishlist = favoriteDAO.getWishListByUserId(UserManager.getInstance().getUserId());
        favoriteDAO.close();
        if (!wishlist.isEmpty()) {
            List<Product> products = new ArrayList<>();
            for (Favorite f: wishlist) {
                products.add(f.getProduct());
            }

            ProductAdapter mAdapter = new ProductAdapter(products, product -> {

            }, true);
            recycler_wishlist.setAdapter(mAdapter);
            recycler_wishlist.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        } else {
            Toast.makeText(this.getContext(), "Hiện chưa có sản phẩm nào được yêu thích", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}