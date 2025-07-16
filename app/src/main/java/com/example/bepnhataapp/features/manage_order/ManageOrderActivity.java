package com.example.bepnhataapp.features.manage_order;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.OrderStatus;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ManageOrderActivity extends com.example.bepnhataapp.common.base.BaseActivity implements com.example.bepnhataapp.common.base.BaseActivity.OnNavigationItemReselectedListener {
    private static final OrderStatus[] TAB_STATUS = new OrderStatus[]{
            OrderStatus.WAIT_CONFIRM, OrderStatus.WAIT_PICKUP, OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED, OrderStatus.RETURNED, OrderStatus.CANCELED
    };
    private static final String[] TAB_TITLES = new String[]{
            "Chờ xác nhận", "Chờ lấy hàng", "Đang vận chuyển", "Đã giao", "Đã trả hàng", "Đã huỷ"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        // Gắn bottom navigation, chọn tab Trang chủ sáng (hoặc tab phù hợp)
        setupBottomNavigationFragment(R.id.nav_home);

        // Seed dữ liệu đơn hàng mẫu nếu cần
        com.example.bepnhataapp.common.utils.SampleOrderSeeder.seedOrdersForCustomer(this, 10);

        TabLayout tabLayout = findViewById(R.id.tabLayoutOrder);
        ViewPager2 viewPager = findViewById(R.id.viewPagerOrder);
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        viewPager.setAdapter(new OrderPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        int tabIndex = getIntent().getIntExtra("tab_index", 0);
        viewPager.setCurrentItem(tabIndex, false);
    }

    @Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        int tabIndex = intent.getIntExtra("tab_index", 0);
        ViewPager2 viewPager = findViewById(R.id.viewPagerOrder);
        viewPager.setCurrentItem(tabIndex, false);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    class OrderPagerAdapter extends FragmentStateAdapter {
        public OrderPagerAdapter(@NonNull FragmentActivity fa) { super(fa); }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return OrderListFragment.newInstance(TAB_STATUS[position]);
        }
        @Override
        public int getItemCount() { return TAB_TITLES.length; }
    }
} 
