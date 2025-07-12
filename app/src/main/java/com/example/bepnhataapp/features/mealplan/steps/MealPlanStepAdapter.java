package com.example.bepnhataapp.features.mealplan.steps;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MealPlanStepAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public MealPlanStepAdapter(@NonNull FragmentActivity fa) {
        super(fa);
        fragments = new ArrayList<>();
        fragments.add(new StepPhysicalProfileFragment());
        fragments.add(new StepGeneralGoalFragment());
        fragments.add(new StepAllergyFragment());
        fragments.add(new StepActivityLevelFragment());
        fragments.add(new StepSummaryFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
} 
