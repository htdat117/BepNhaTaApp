package com.example.bepnhataapp.features.mealplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MealPlanActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_meal_plan);

        // Setup the "Điền thông tin" button click listener
        MaterialButton btnFillInfo = findViewById(R.id.btnFillInfo);
        btnFillInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlanActivity.this, MealPlanWizardActivity.class);
                startActivity(intent);
            }
        });
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