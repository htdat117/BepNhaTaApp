package com.example.bepnhataapp.common.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bepnhataapp.features.ingredients.IngredientPackageFragment;
import com.example.bepnhataapp.features.ingredients.UsageGuideFragment;

public class ProductDetailPagerAdapter extends FragmentStateAdapter {

    public ProductDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new IngredientPackageFragment();
        } else {
            return new UsageGuideFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
} 