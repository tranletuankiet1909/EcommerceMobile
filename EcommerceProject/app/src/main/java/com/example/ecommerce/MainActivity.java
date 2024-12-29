package com.example.ecommerce;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.databinding.ActivityMainBinding;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.fragment.AdminDashboardFragment;
import com.example.ecommerce.fragment.CartFragment;
import com.example.ecommerce.fragment.HomeFragment;
import com.example.ecommerce.fragment.ProfileFragment;
import com.example.ecommerce.fragment.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    User currentUser = UserManager.getInstance().getCurrentUser();
    String role = currentUser.getRole();
    Boolean isAdmin = role.equals(Role.ADMIN.getDisplayName());
    Boolean isBuyer = role.equals(Role.BUYER.getDisplayName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (isAdmin) {
            replaceFragment(new AdminDashboardFragment());
        } else if (isBuyer) {
            replaceFragment(new HomeFragment());
        }
        setUpBottomNavigation();


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                if (isAdmin) {
                    replaceFragment(new AdminDashboardFragment());
                } else if (isBuyer) {
                    replaceFragment(new HomeFragment());
                }
            } else if (itemId == R.id.wishlist) {
                replaceFragment(new WishlistFragment());
            } else if (itemId == R.id.cart) {
                replaceFragment(new CartFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });


    }

    private void setUpBottomNavigation() {
        if (isAdmin) {
            binding.bottomNavigationView.getMenu().findItem(R.id.cart).setVisible(false);
            binding.bottomNavigationView.getMenu().findItem(R.id.wishlist).setVisible(false);
        } else {
            binding.bottomNavigationView.getMenu().findItem(R.id.cart).setVisible(true);
            binding.bottomNavigationView.getMenu().findItem(R.id.wishlist).setVisible(true);
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment);
        fragmentTransaction.commit();
        setUpBottomNavigation();
    }
}