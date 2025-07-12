package com.example.bepnhataapp.features.mealplan.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.MealPlanWizardActivity;
import com.google.android.material.button.MaterialButton;
import androidx.core.content.ContextCompat;
import android.content.res.ColorStateList;
import java.util.ArrayList;
import java.util.List;

public class StepActivityLevelFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_activity_level, container, false);

        // Setup back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToPreviousStep();
            }
        });

        // Setup next button in header
        TextView btnNextHeader = view.findViewById(R.id.btnNext);
        btnNextHeader.setOnClickListener(v -> {
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

        // Apply bold title formatting for each option
        applyFormattedText(view, R.id.btnSedentary, "Ít vận động", "Ít hoặc không tập thể dục với công việc ngồi bàn giấy");
        applyFormattedText(view, R.id.btnLight, "Hoạt động nhẹ", "Ít hoặc không tập thể dục với công việc ngồi bàn giấy");
        applyFormattedText(view, R.id.btnModerate, "Hoạt động vừa phải", "Cuộc sống hàng ngày vừa phải với tập thể dục 3 - 5 ngày mỗi tuần");
        applyFormattedText(view, R.id.btnActive, "Hoạt động mạnh", "Lối sống đòi hỏi thể chất với tập thể dục hoặc thể thao 6 - 7 ngày mỗi tuần");
        applyFormattedText(view, R.id.btnVeryActive, "Hoạt động cực kỳ cao", "Tập thể dục hoặc thể thao nặng hàng ngày và công việc thể chất");

        // Collect option buttons
        List<MaterialButton> optionButtons = new ArrayList<>();
        optionButtons.add(view.findViewById(R.id.btnSedentary));
        optionButtons.add(view.findViewById(R.id.btnLight));
        optionButtons.add(view.findViewById(R.id.btnModerate));
        optionButtons.add(view.findViewById(R.id.btnActive));
        optionButtons.add(view.findViewById(R.id.btnVeryActive));

        // Pre-select the first option
        setButtonSelected(optionButtons.get(0), true);

        // Initialize listeners and default stroke for others
        for (MaterialButton btn : optionButtons) {
            if (btn != optionButtons.get(0)) {
                setButtonSelected(btn, false);
            }
            btn.setOnClickListener(v -> {
                for (MaterialButton b : optionButtons) {
                    setButtonSelected(b, b == v);
                }
            });
        }

        return view;
    }

    private void applyFormattedText(View root, int buttonId, String title, String description) {
        Button btn = root.findViewById(buttonId);
        SpannableStringBuilder ssb = new SpannableStringBuilder(title + "\n" + description);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btn.setAllCaps(false);
        btn.setTransformationMethod(null);
        btn.setText(ssb);
    }

    private void setButtonSelected(MaterialButton button, boolean selected) {
        int colorRes = selected ? R.color.primary1 : R.color.dark2;
        int color = ContextCompat.getColor(requireContext(), colorRes);
        button.setStrokeColor(ColorStateList.valueOf(color));
        // Keep stroke width 5dp
        button.setStrokeWidth(5);
    }
} 
