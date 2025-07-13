package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.steps.MealPlanStepAdapter;

public class MealPlanWizardActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_wizard);

        viewPager = findViewById(R.id.mealplan_viewpager);
        viewPager.setAdapter(new MealPlanStepAdapter(this));
        viewPager.setUserInputEnabled(false); // Disable swipe between fragments
    }

    // Method to be called from fragments to move to next step
    public void moveToNextStep() {
        if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    // Method to be called from fragments to move to previous step
    public void moveToPreviousStep() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public int getCurrentStepIndex(){
        return viewPager.getCurrentItem();
    }
} 
