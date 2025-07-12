package com.example.bepnhataapp.features.checkout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.features.home.HomeActivity;

/**
 * Hiển thị màn hình thông báo đặt hàng thành công.
 * Header và footer được xử lý bởi {@link BaseActivity} để bảo đảm đồng bộ khắp ứng dụng.
 */
public class PaymentSuccessActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // Gắn BottomNavigationFragment và highlight tab "Trang chủ" mặc định
        setupBottomNavigationFragment(R.id.nav_home);

        // Theo dõi đơn hàng
        findViewById(R.id.btnTrackOrder).setOnClickListener(v -> {
            Intent it = new Intent(PaymentSuccessActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
            it.putExtra("tab_index", 0); // tab Chờ xác nhận
            startActivity(it);
        });

        // Về trang chủ
        findViewById(R.id.btnGoHome).setOnClickListener(v -> {
            Intent i = new Intent(PaymentSuccessActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        // Sử dụng logic điều hướng chung trong BaseActivity
        handleNavigation(itemId);
    }
} 
