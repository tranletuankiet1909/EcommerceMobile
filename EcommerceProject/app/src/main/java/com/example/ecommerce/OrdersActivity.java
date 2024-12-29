package com.example.ecommerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecommerce.adapter.ViewPagerOrderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrdersActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ViewPagerOrderAdapter mViewPagerOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mTabLayout = findViewById(R.id.tab_layout_order);
        mViewPager = findViewById(R.id.view_pager_order);
        mViewPagerOrderAdapter = new ViewPagerOrderAdapter(this);
        mViewPager.setAdapter(mViewPagerOrderAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Chờ thanh toán");
                    break;
                case 1:
                    tab.setText("Chờ lấy hàng");
                    break;
                case 2:
                    tab.setText("Chờ giao hàng");
                    break;
                case 3:
                    tab.setText("Đánh giá");
                    break;
            }
        }).attach();

    }
}