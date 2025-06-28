package com.example.bepnhataapp.features.mealplan;

import android.content.Intent;
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

public class EmptyPlanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_empty, container, false);

        Button btnAuto = view.findViewById(R.id.btnAutoGenerate);
        btnAuto.setOnClickListener(v -> {
            MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
            java.time.LocalDate date;
            if(requireActivity() instanceof com.example.bepnhataapp.features.mealplan.MealPlanContentActivity){
                date = ((com.example.bepnhataapp.features.mealplan.MealPlanContentActivity)requireActivity()).getCurrentDate();
            }else{ date = java.time.LocalDate.now(); }
            vm.autoGenerateFor(date);
        });

        MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        view.findViewById(R.id.btnLoadBlank).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dayPlanContainer, new BlankDayPlanFragment())
                    .commit();
        });

        view.findViewById(R.id.btnLoadSaved).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoadMealPlanActivity.class);
            startActivity(intent);
        });

        return view;
    }
} 