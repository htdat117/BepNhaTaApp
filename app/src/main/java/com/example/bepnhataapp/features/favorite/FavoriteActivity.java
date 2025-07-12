package com.example.bepnhataapp.features.favorite;

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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FavoriteActivity extends AppCompatActivity {

    private static final String[] TAB_TITLES = {"Sản phẩm", "Công thức", "Blog"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        TabLayout tabLayout = findViewById(R.id.tabLayoutFavorite);
        ViewPager2 viewPager = findViewById(R.id.viewPagerFavorite);
        viewPager.setAdapter(new FavoritePagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();
    }

    class FavoritePagerAdapter extends FragmentStateAdapter {
        public FavoritePagerAdapter(@NonNull FragmentActivity fa) { super(fa); }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new FavoriteProductFragment();
                case 1: return new FavoriteRecipeFragment();
                default: return new FavoriteBlogFragment();
            }
        }
        @Override
        public int getItemCount() { return TAB_TITLES.length; }
    }
} 
