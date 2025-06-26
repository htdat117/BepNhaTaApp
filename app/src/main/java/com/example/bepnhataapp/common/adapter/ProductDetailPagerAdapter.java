package com.example.bepnhataapp.common.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bepnhataapp.features.ingredients.IngredientPackageFragment;
import com.example.bepnhataapp.features.ingredients.UsageGuideFragment;
import com.example.bepnhataapp.common.model.ProductDetail;

public class ProductDetailPagerAdapter extends FragmentStateAdapter {

    private final ProductDetail detail;
    private final long productId;

    public ProductDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDetail detail, long productId) {
        super(fragmentActivity);
        this.detail = detail;
        this.productId = productId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return IngredientPackageFragment.newInstance(productId);
        } else {
            return UsageGuideFragment.newInstance(
                    detail != null ? detail.getStorageGuide() : "",
                    detail != null ? detail.getExpiry() : "",
                    detail != null ? detail.getNote() : ""
            );
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
} 