package com.example.bepnhataapp.features.mealplan.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.MealPlanWizardActivity;

public class StepGeneralGoalFragment extends Fragment {

    private Button btnGeneralGoalTab;
    private Button btnSpecificGoalTab;
    private FrameLayout goalContentContainer;
    private View generalGoalView;
    private View specificGoalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mealplan_general_goal, container, false);

        // Setup back button
        ImageButton btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToPreviousStep();
            }
        });

        // Setup next button in header
        TextView btnNext = root.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                // Check if we're in specific goal tab
                if (btnSpecificGoalTab.isSelected()) {
                    // If we're in specific goal tab, switch back to general goal tab
                    btnGeneralGoalTab.setSelected(true);
                    btnSpecificGoalTab.setSelected(false);
                    btnGeneralGoalTab.refreshDrawableState();
                    btnSpecificGoalTab.refreshDrawableState();
                    goalContentContainer.removeAllViews();
                    goalContentContainer.addView(generalGoalView);
                } else {
                    // If we're in general goal tab, proceed to next step
                    ((MealPlanWizardActivity) getActivity()).moveToNextStep();
                }
            }
        });

        // Setup next button at bottom (PrimaryButton)
        Button btnNextBottom = root.findViewById(R.id.btnNextBottom);
        btnNextBottom.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                // Check if we're in specific goal tab
                if (btnSpecificGoalTab.isSelected()) {
                    // If we're in specific goal tab, switch back to general goal tab
                    btnGeneralGoalTab.setSelected(true);
                    btnSpecificGoalTab.setSelected(false);
                    btnGeneralGoalTab.refreshDrawableState();
                    btnSpecificGoalTab.refreshDrawableState();
                    goalContentContainer.removeAllViews();
                    goalContentContainer.addView(generalGoalView);
                } else {
                    // If we're in general goal tab, proceed to next step
                    ((MealPlanWizardActivity) getActivity()).moveToNextStep();
                }
            }
        });

        // Setup goal type tabs
        btnGeneralGoalTab = root.findViewById(R.id.btnGeneralGoalTab);
        btnSpecificGoalTab = root.findViewById(R.id.btnSpecificGoalTab);
        goalContentContainer = root.findViewById(R.id.goalContentContainer);

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