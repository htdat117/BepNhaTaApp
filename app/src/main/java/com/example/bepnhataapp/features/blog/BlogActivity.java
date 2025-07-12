package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;

public class BlogActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        // Load BlogFeedFragment into content_container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_container, new BlogFeedFragment());
        fragmentTransaction.commit();

        // Sự kiện nút back
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Setup the bottom navigation fragment
        // Removed setupBottomNavigationFragment to prevent automatic navigation
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
