package com.example.bepnhataapp.features.mealplan.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

public class StepGeneralGoalFragment extends Fragment {

    private View btnGeneralGoalTab;
    private View btnSpecificGoalTab;
    private FrameLayout goalContentContainer;
    private View generalGoalView;
    private View specificGoalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mealplan_general_goal, container, false);
        Button btnGeneralGoalTab = root.findViewById(R.id.btnGeneralGoalTab);
        Button btnSpecificGoalTab = root.findViewById(R.id.btnSpecificGoalTab);
        FrameLayout goalContentContainer = root.findViewById(R.id.goalContentContainer);

        // Inflate 2 nội dung
        generalGoalView = inflater.inflate(R.layout.fragment_mealplan_general_goal_content, goalContentContainer, false);
        specificGoalView = inflater.inflate(R.layout.fragment_mealplan_specific_goal, goalContentContainer, false);

        // Mặc định: btnGeneralGoalTab được chọn, btnSpecificGoalTab không chọn
        btnGeneralGoalTab.setSelected(true);
        btnSpecificGoalTab.setSelected(false);
        goalContentContainer.removeAllViews();
        goalContentContainer.addView(generalGoalView);

        btnGeneralGoalTab.setOnClickListener(v -> {
            btnGeneralGoalTab.setSelected(true);
            btnSpecificGoalTab.setSelected(false);
            
            // Refresh để cập nhật lại giao diện
            btnGeneralGoalTab.refreshDrawableState();
            btnSpecificGoalTab.refreshDrawableState();
            
            goalContentContainer.removeAllViews();
            goalContentContainer.addView(generalGoalView);
        });
        btnSpecificGoalTab.setOnClickListener(v -> {
            btnGeneralGoalTab.setSelected(false);
            btnSpecificGoalTab.setSelected(true);
            
            // Refresh để cập nhật lại giao diện
            btnGeneralGoalTab.refreshDrawableState();
            btnSpecificGoalTab.refreshDrawableState();
            
            goalContentContainer.removeAllViews();
            goalContentContainer.addView(specificGoalView);
        });
        return root;
    }

    private void showGeneralGoal() {
        // Implementation of showing general goal
    }

    private void showSpecificGoal() {
        // Implementation of showing specific goal
    }
} 