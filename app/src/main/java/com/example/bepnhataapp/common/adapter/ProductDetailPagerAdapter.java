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
    private int servingFactor;
    private IngredientPackageFragment ingredientFragment;

    public ProductDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDetail detail, long productId, int servingFactor) {
        super(fragmentActivity);
        this.detail = detail;
        this.productId = productId;
        this.servingFactor = servingFactor;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            ingredientFragment = IngredientPackageFragment.newInstance(productId, servingFactor);
            return ingredientFragment;
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

    public IngredientPackageFragment getIngredientFragment(){
        return ingredientFragment;
    }

    public void updateServingFactor(int factor){
        this.servingFactor = factor;
        if(ingredientFragment!=null){
            ingredientFragment.setServingFactor(factor);
        }
    }
} 