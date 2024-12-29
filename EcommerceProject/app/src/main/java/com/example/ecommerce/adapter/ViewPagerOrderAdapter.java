package com.example.ecommerce.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommerce.fragment.OngoingOrderFragment;
import com.example.ecommerce.fragment.PendingOrderFragment;
import com.example.ecommerce.fragment.ReviewOrderFragment;
import com.example.ecommerce.fragment.ShippingOrderFragment;

public class ViewPagerOrderAdapter extends FragmentStateAdapter {


    public ViewPagerOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PendingOrderFragment();
            case 1:
                return new OngoingOrderFragment();
            case 2:
                return new ShippingOrderFragment();
            case 3:
                return new ReviewOrderFragment();
            default:
                return new PendingOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
