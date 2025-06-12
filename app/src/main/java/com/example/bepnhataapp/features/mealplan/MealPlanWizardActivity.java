package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.steps.MealPlanStepAdapter;

public class MealPlanWizardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_wizard);

        ViewPager2 viewPager = findViewById(R.id.mealplan_viewpager);
        viewPager.setAdapter(new MealPlanStepAdapter(this));
    }
} 