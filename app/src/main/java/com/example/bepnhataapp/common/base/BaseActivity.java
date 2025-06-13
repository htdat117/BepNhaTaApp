package com.example.bepnhataapp.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.home.HomeActivity;
import com.example.bepnhataapp.features.ingredients.ProductActivity;
import com.example.bepnhataapp.features.mealplan.MealPlanActivity;
import com.example.bepnhataapp.features.recipes.RecipesActivity;
import com.example.bepnhataapp.features.tools.ToolsActivity;
import com.example.bepnhataapp.features.manage_account.ManageAccountActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    // Interface to communicate from BottomNavigationFragment to Activity
    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(int itemId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Thiết lập sự kiện click cho header (nếu header tồn tại trong layout)
        setupHeaderClicks();
    }

    private void setupHeaderClicks() {
        // Tìm các icon trên header (nếu layout hiện tại có include header)
        View root = findViewById(android.R.id.content);
        if (root == null) return;
        ImageView ivCart = root.findViewById(R.id.iv_cart);
        ImageView ivMessage = root.findViewById(R.id.iv_message);
        ImageView ivNotification = root.findViewById(R.id.iv_notification);
        ImageView ivLogo = root.findViewById(R.id.iv_logo);
        TextView tvLogin = root.findViewById(R.id.tv_login);

        View.OnClickListener goToManageAccount = v -> {
            if (!(BaseActivity.this instanceof ManageAccountActivity)) {
                Intent intent = new Intent(BaseActivity.this, ManageAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        if (ivCart != null) ivCart.setOnClickListener(goToManageAccount);
        if (ivMessage != null) ivMessage.setOnClickListener(goToManageAccount);
        if (ivNotification != null) ivNotification.setOnClickListener(goToManageAccount);
        if (ivLogo != null) ivLogo.setOnClickListener(goToManageAccount);
        if (tvLogin != null) tvLogin.setOnClickListener(goToManageAccount);
    }

    // Method to be implemented by child activities to get the container ID for the bottom navigation fragment
    protected abstract int getBottomNavigationContainerId();

    // Method to be implemented by child activities to handle navigation
    protected void handleNavigation(int itemId) {
        Intent intent = null;
        Class<?> targetActivity = null;

        if (itemId == R.id.nav_home) {
            targetActivity = HomeActivity.class;
            overridePendingTransition(0,0);
        } else if (itemId == R.id.nav_ingredients) {
            targetActivity = ProductActivity.class;
            overridePendingTransition(0,0);
        } else if (itemId == R.id.nav_recipes) {
            targetActivity = RecipesActivity.class;
            overridePendingTransition(0,0);
        } else if (itemId == R.id.nav_meal_plan) {
            targetActivity = MealPlanActivity.class;
            overridePendingTransition(0,0);
        } else if (itemId == R.id.nav_tools) {
            targetActivity = ToolsActivity.class;
            overridePendingTransition(0,0);
        }

        if (targetActivity != null) {
            Log.d(TAG, "Current activity: " + this.getClass().getSimpleName());
            Log.d(TAG, "Target activity: " + targetActivity.getSimpleName());

            if (!this.getClass().equals(targetActivity)) {
                try {
                    intent = new Intent(this, targetActivity);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.d(TAG, "Successfully started activity: " + targetActivity.getSimpleName());
                } catch (Exception e) {
                    Log.e(TAG, "Error starting activity: " + e.getMessage());
                }
            } else {
                Log.d(TAG, "Already in target activity");
            }
        }
    }

    // Method to attach the BottomNavigationFragment
    protected void setupBottomNavigationFragment(int selectedItemId) {
        BottomNavigationFragment bottomNavFragment = new BottomNavigationFragment();
        Bundle args = new Bundle();
        args.putInt("selectedItemId", selectedItemId);
        bottomNavFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(getBottomNavigationContainerId(), bottomNavFragment)
                .commit();
    }
} 