package com.example.bepnhataapp.features.products;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.databinding.BottomSheetFilterProductBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Set;

/**
 * A BottomSheetDialogFragment that provides product filtering options.
 * Includes ingredients, region, nutrition group, calorie and time filters.
 */
public class FilterProductBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetFilterProductBinding binding;

    private final String[] ingredientTags = {"Thịt heo", "Thịt gà", "Thịt bò", "Hải sản", "Trứng", "Đậu phụ", "Rau củ", "Nấm"};
    private final String[] regionTags = {"Miền Bắc", "Miền Trung", "Miền Nam", "Miền Tây", "Món Âu"};
    private final String[] nutritionTags = {
            "Ít chất béo",
            "Không cholesterol",
            "Giàu omega-3",
            "Giàu vitamin",
            "Giàu đạm",
            "Nhiều canxi",
            "Giàu chất xơ"
    };

    public static class FilterCriteria {
        public Set<String> ingredients;
        public Set<String> regions;
        public Set<String> nutritions;
        public int maxKcal;
        public int maxTime;
    }

    public interface OnFilterAppliedListener {
        void onFilterApplied(FilterCriteria criteria);
    }

    private OnFilterAppliedListener listener;

    // Hold previously applied filter so we can preselect when reopening
    private FilterCriteria initialCriteria;

    public void setOnFilterAppliedListener(OnFilterAppliedListener l) { this.listener = l; }

    // Allow parent activity to pass in previously selected criteria
    public void setInitialCriteria(FilterCriteria c){
        this.initialCriteria = c;
    }

    public FilterProductBottomSheet() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetFilterProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupTagFlex(binding.flexIngredients, ingredientTags, false);
        setupTagFlex(binding.flexRegions, regionTags, false);
        setupTagFlex(binding.flexNutritions, nutritionTags, false);

        // Restore previous selections if available
        if(initialCriteria!=null){
            preselectTags(binding.flexIngredients, initialCriteria.ingredients);
            preselectTags(binding.flexRegions, initialCriteria.regions);
            preselectTags(binding.flexNutritions, initialCriteria.nutritions);

            // sliders restore
            if(initialCriteria.maxKcal>0){
                binding.sliderKcal.setValue(initialCriteria.maxKcal);
                binding.tvKcalValue.setText(String.valueOf(initialCriteria.maxKcal));
            }
            if(initialCriteria.maxTime>0){
                binding.sliderTime.setValue(initialCriteria.maxTime);
                binding.tvTimeValue.setText(String.valueOf(initialCriteria.maxTime));
            }
        }

        // sliders setup
        binding.sliderKcal.setLabelFormatter(value -> String.valueOf((int) value));
        binding.sliderKcal.addOnChangeListener((slider, value, fromUser) -> binding.tvKcalValue.setText(String.valueOf((int) value)));
        float maxKcal = binding.sliderKcal.getValueTo();
        binding.sliderKcal.setValue(maxKcal);
        binding.tvKcalValue.setText(String.valueOf((int) maxKcal));

        binding.sliderTime.setLabelFormatter(value -> String.valueOf((int) value));
        binding.sliderTime.addOnChangeListener((slider, value, fromUser) -> binding.tvTimeValue.setText(String.valueOf((int) value)));
        float maxTime = binding.sliderTime.getValueTo();
        binding.sliderTime.setValue(maxTime);
        binding.tvTimeValue.setText(String.valueOf((int) maxTime));

        // actions
        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnApply.setOnClickListener(v -> {
            if(listener != null){
                FilterCriteria c = new FilterCriteria();
                c.ingredients = getSelectedTags(binding.flexIngredients);
                c.regions = getSelectedTags(binding.flexRegions);
                c.nutritions = getSelectedTags(binding.flexNutritions);
                c.maxKcal = (int) binding.sliderKcal.getValue();
                c.maxTime = (int) binding.sliderTime.getValue();
                listener.onFilterApplied(c);
            }
            dismiss();
        });

        binding.btnClear.setOnClickListener(v -> {
            // reset UI selections
            resetSelections(binding.flexIngredients);
            resetSelections(binding.flexRegions);
            resetSelections(binding.flexNutritions);

            float maxK = binding.sliderKcal.getValueTo();
            binding.sliderKcal.setValue(maxK);
            binding.tvKcalValue.setText(String.valueOf((int)maxK));

            float maxT = binding.sliderTime.getValueTo();
            binding.sliderTime.setValue(maxT);
            binding.tvTimeValue.setText(String.valueOf((int)maxT));

            // Do not auto-apply or dismiss; user can adjust then press Apply
        });
    }

    private void setupTagFlex(FlexboxLayout flex, String[] tags, boolean isSingleSelection) {
        for (String tag : tags) {
            MaterialButton chip = new MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            chip.setText(tag);
            chip.setTextSize(14);
            chip.setStrokeColorResource(R.color.primary1);
            chip.setStrokeWidth(2);
            chip.setRippleColorResource(R.color.primary3);
            chip.setPaddingRelative(20, 8, 20, 8);
            chip.setCornerRadius(24);
            styleChip(chip, false);

            chip.setOnClickListener(v -> {
                if (isSingleSelection) {
                    // single selection behavior
                    for (int i = 0; i < flex.getChildCount(); i++) {
                        MaterialButton mb = (MaterialButton) flex.getChildAt(i);
                        boolean sel = (mb == chip);
                        styleChip(mb, sel);
                        mb.setTag(sel);
                    }
                } else {
                    boolean current = chip.getTag() != null && (boolean) chip.getTag();
                    styleChip(chip, !current);
                    chip.setTag(!current);
                }
            });

            chip.setTag(false);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            int m = (int) (getResources().getDisplayMetrics().density * 4); // 4dp margin each side
            lp.setMargins(m, m, m, m);
            chip.setLayoutParams(lp);
            flex.addView(chip);
        }
    }

    private void styleChip(MaterialButton chip, boolean selected) {
        if (selected) {
            chip.setBackgroundTintList(requireContext().getColorStateList(R.color.primary1));
            chip.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            chip.setBackgroundTintList(requireContext().getColorStateList(android.R.color.white));
            chip.setTextColor(getResources().getColor(R.color.primary1));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) d;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackground(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        return dialog;
    }

    private java.util.Set<String> getSelectedTags(FlexboxLayout flex){
        java.util.Set<String> set = new java.util.HashSet<>();
        for(int i=0;i<flex.getChildCount();i++){
            MaterialButton mb = (MaterialButton) flex.getChildAt(i);
            if(mb.getTag()!=null && (boolean)mb.getTag()) set.add(mb.getText().toString());
        }
        return set;
    }

    private String getSingleSelectedTag(FlexboxLayout flex){
        for(int i=0;i<flex.getChildCount();i++){
            MaterialButton mb=(MaterialButton) flex.getChildAt(i);
            if(mb.getTag()!=null && (boolean)mb.getTag()) return mb.getText().toString();
        }
        return null;
    }

    private void preselectTags(FlexboxLayout flex, java.util.Set<String> selected){
        if(selected==null || selected.isEmpty()) return;
        for(int i=0;i<flex.getChildCount();i++){
            com.google.android.material.button.MaterialButton mb = (com.google.android.material.button.MaterialButton) flex.getChildAt(i);
            String text = mb.getText().toString();
            boolean sel = containsIgnoreCase(selected,text);
            styleChip(mb, sel);
            mb.setTag(sel);
        }
    }

    private boolean containsIgnoreCase(java.util.Set<String> set, String value){
        for(String s:set){ if(s.equalsIgnoreCase(value)) return true; }
        return false;
    }

    private void resetSelections(FlexboxLayout flex){
        for(int i=0;i<flex.getChildCount();i++){
            MaterialButton mb=(MaterialButton) flex.getChildAt(i);
            styleChip(mb,false);
            mb.setTag(false);
        }
    }
} 
