package com.example.bepnhataapp.features.mealplan.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

public class StepSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_summary, container, false);

        // Setup show menu button
        Button btnShowMenu = view.findViewById(R.id.btnShowMenu);
        btnShowMenu.setOnClickListener(v -> {
            // TODO: Handle showing suggested menu
            // For now, just finish the activity
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }
} 