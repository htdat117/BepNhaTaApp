package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

public class BlankDayPlanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_day_plan, container, false);

        String[] meals = {"Buổi sáng", "Buổi trưa", "Buổi tối", "Bữa phụ"};
        LinearLayout containerMeals = view.findViewById(R.id.containerMeals);
        for (String m : meals) {
            View row = inflater.inflate(R.layout.item_blank_meal, containerMeals, false);
            ((TextView) row.findViewById(R.id.tvMealTitle)).setText(m);
            containerMeals.addView(row);
        }
        ((TextView) view.findViewById(R.id.tvTracked)).setText("Theo dõi 0/" + meals.length + " bữa ăn");
        return view;
    }
} 
