package com.example.ecommerce.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ecommerce.fragment.ShippingOrderFragment;

import java.util.Arrays;
import java.util.List;

public class OrderPagerAdapter extends FragmentStateAdapter {
    private final List<String> tabTitles = Arrays.asList("Pending", "Completed", "Canceled");
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ShippingOrderFragment.newInstance(tabTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
