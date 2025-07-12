package com.example.bepnhataapp.features.mealplan.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.MealPlanWizardActivity;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;

public class StepAllergyFragment extends Fragment {

    // Allergy chips references
    private MaterialButton[] allergyChips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_allergy, container, false);

        // Setup back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToPreviousStep();
            }
        });

        // Setup next button in header
        TextView btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToNextStep();
            }
        });

        // Setup next button at bottom
        Button btnNextBottom = view.findViewById(R.id.btnNextBottom);
        btnNextBottom.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToNextStep();
            }
        });

        //============ Allergy chips logic ============
        allergyChips = new MaterialButton[] {
                view.findViewById(R.id.chip1),
                view.findViewById(R.id.chip2),
                view.findViewById(R.id.chip3),
                view.findViewById(R.id.chip4),
                view.findViewById(R.id.chip5),
                view.findViewById(R.id.chip6),
                view.findViewById(R.id.chip7)
        };

        View.OnClickListener chipClickListener = v -> {
            if (v instanceof MaterialButton) {
                MaterialButton chip = (MaterialButton) v;
                chip.setSelected(!chip.isSelected()); // toggle state
                updateChipAppearance();
            }
        };

        for (MaterialButton chip : allergyChips) {
            if (chip != null) chip.setOnClickListener(chipClickListener);
        }

        updateChipAppearance();

        return view;
    }

    private void updateChipAppearance() {
        int primaryColor = ContextCompat.getColor(requireContext(), R.color.primary1);
        int whiteColor = ContextCompat.getColor(requireContext(), R.color.white);

        for (MaterialButton chip : allergyChips) {
            if (chip == null) continue;
            if (chip.isSelected()) {
                chip.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
                chip.setTextColor(whiteColor);
            } else {
                chip.setBackgroundTintList(ColorStateList.valueOf(whiteColor));
                chip.setTextColor(primaryColor);
            }
        }
    }
} 
