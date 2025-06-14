package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.features.mealplan.adapters.MealTimeAdapter;
import java.util.Arrays;
import java.util.List;

public class TimelineFragment extends Fragment {

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_timeline, container, false);

        RecyclerView rv = view.findViewById(R.id.rvTimeline);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fake data
        MealTimeAdapter.RecipeItem recipe = new MealTimeAdapter.RecipeItem(R.drawable.placeholder_banner_background, "Sườn nướng mật ong", 1560);
        List<MealTimeAdapter.RecipeItem> recipes = Arrays.asList(recipe, recipe);

        List<MealTimeAdapter.MealTimeSection> sections = Arrays.asList(
                new MealTimeAdapter.MealTimeSection("Buổi sáng", 826, recipes),
                new MealTimeAdapter.MealTimeSection("Buổi trưa", 826, recipes),
                new MealTimeAdapter.MealTimeSection("Buổi tối", 826, recipes)
        );

        MealTimeAdapter adapter = new MealTimeAdapter(sections);
        rv.setAdapter(adapter);

        // Update meal count label
        ((TextView) view.findViewById(R.id.tvMealCount)).setText("Theo dõi 0/" + sections.size() + " bữa ăn");

        return view;
    }
} 