package com.example.bepnhataapp.features.recipes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RecipeDetailPagerAdapter extends FragmentStateAdapter {
    private final long recipeId;
    public RecipeDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, long recipeId) {
        super(fragmentActivity);
        this.recipeId = recipeId;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return RecipeIngredientFragment.newInstance(recipeId);
        } else {
            return RecipeStepGuideFragment.newInstance(recipeId);
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
} 