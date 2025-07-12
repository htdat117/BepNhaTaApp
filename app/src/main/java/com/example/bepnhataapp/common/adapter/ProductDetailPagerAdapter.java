package com.example.bepnhataapp.common.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bepnhataapp.features.ingredients.IngredientPackageFragment;
import com.example.bepnhataapp.features.ingredients.UsageGuideFragment;
import com.example.bepnhataapp.common.model.ProductDetail;
import com.example.bepnhataapp.common.model.Product;

public class ProductDetailPagerAdapter extends FragmentStateAdapter {

    private final ProductDetail detail;
    private final Product product;
    private final long productId;
    private int servingFactor;
    private IngredientPackageFragment ingredientFragment;

    public ProductDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDetail detail, Product product, long productId, int servingFactor) {
        super(fragmentActivity);
        this.detail = detail;
        this.product = product;
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
            String imageUrl = null;
            if (product != null && product.getProductThumb() != null && !product.getProductThumb().isEmpty()) {
                String thumb = product.getProductThumb();
                // Kiểm tra link cloudinary hoặc http/https
                if (thumb.startsWith("http://") || thumb.startsWith("https://")) {
                    imageUrl = thumb;
                }
            }
            return UsageGuideFragment.newInstance(
                    detail != null ? detail.getStorageGuide() : "",
                    detail != null ? detail.getExpiry() : "",
                    detail != null ? detail.getNote() : "",
                    imageUrl,
                    null,
                    0
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
