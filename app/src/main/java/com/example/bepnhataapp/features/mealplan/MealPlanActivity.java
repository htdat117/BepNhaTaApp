package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MealPlanActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_meal_plan);
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