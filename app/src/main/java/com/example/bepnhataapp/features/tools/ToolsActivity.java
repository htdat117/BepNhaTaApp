package com.example.bepnhataapp.features.tools;

import android.os.Bundle;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ToolsActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        // Hiển thị fragment BMI/BMR vào content_container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_container, new BmiBmrFragment());
        fragmentTransaction.commit();

        // Giữ nguyên thanh bottom navigation như cũ
        setupBottomNavigationFragment(R.id.nav_tools);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }
}