package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.data.FakeMealPlanRepository;

public class EmptyPlanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_empty, container, false);

        Button btnAuto = view.findViewById(R.id.btnAutoGenerate);
        btnAuto.setOnClickListener(v -> {
            MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
            vm.autoGenerate();
        });
        return view;
    }
} 