package com.example.ecommerce.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce.LoginActivity;
import com.example.ecommerce.OrdersActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.UserManager;
import com.example.ecommerce.entity.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User currentUser = UserManager.getInstance().getCurrentUser();
    private CircularImageView circularImageView;
    private TextView txtViewUsername, txtViewEmail;
    private Button btnLogout;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout mPendingOrderLayout = view.findViewById(R.id.layout_pending);
        LinearLayout mOngoingOrderLayout = view.findViewById(R.id.layout_ongoing);
        LinearLayout mShippingOrderLayout = view.findViewById(R.id.layout_shipping);
        LinearLayout mReviewOrderLayout = view.findViewById(R.id.layout_review);

        circularImageView = view.findViewById(R.id.avatar_profile);
        txtViewUsername = view.findViewById(R.id.textView_username_profile);
        txtViewEmail = view.findViewById(R.id.textView_email_profile);
        btnLogout = view.findViewById(R.id.button_logout);

//        Picasso.get().load(currentUser.getAvatar()).into(circularImageView);
        Glide.with(circularImageView.getContext()).load(currentUser.getAvatar()).into(circularImageView);
        txtViewUsername.setText(currentUser.getUsername());
        txtViewEmail.setText(currentUser.getEmail());

        // Logout
        btnLogout.setOnClickListener(v -> {
            UserManager.getInstance().setCurrentUser(null);
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        mPendingOrderLayout.setOnClickListener(v -> {
            Intent i = new Intent(this.getActivity(), OrdersActivity.class);
            startActivity(i);
        });
        mOngoingOrderLayout.setOnClickListener(v -> {
            Intent i = new Intent(this.getActivity(), OrdersActivity.class);
            startActivity(i);
        });
        mShippingOrderLayout.setOnClickListener(v -> {
            Intent i = new Intent(this.getActivity(), OrdersActivity.class);
            startActivity(i);
        });
        mReviewOrderLayout.setOnClickListener(v -> {
            Intent i = new Intent(this.getActivity(), OrdersActivity.class);
            startActivity(i);
        });

        return view;
    }
}