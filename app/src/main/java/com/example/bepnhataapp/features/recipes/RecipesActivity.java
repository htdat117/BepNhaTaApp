package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RecipesActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        if (com.example.bepnhataapp.common.utils.NetworkUtils.isNetworkAvailable(this)) {
            // Replace fragment online
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_container, new RecipeListFragment());
            fragmentTransaction.commit();
        } else {
            // Khi mất mạng, chuyển sang OfflineActivity
            startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.offline.OfflineActivity.class));
            finish();
        }
        setupBottomNavigationFragment(R.id.nav_recipes);
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
