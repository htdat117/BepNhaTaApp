package com.example.bepnhataapp.features.recipes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RecipeDetailPagerAdapter extends FragmentStateAdapter {
    private final long recipeId;
    private int servingFactor = 1;
    public RecipeDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, long recipeId, int servingFactor) {
        super(fragmentActivity);
        this.recipeId = recipeId;
        this.servingFactor = servingFactor;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return RecipeIngredientFragment.newInstance(recipeId, servingFactor);
        } else {
            return RecipeStepGuideFragment.newInstance(recipeId);
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
    public void setServingFactor(int factor) {
        this.servingFactor = factor;
    }
} 
