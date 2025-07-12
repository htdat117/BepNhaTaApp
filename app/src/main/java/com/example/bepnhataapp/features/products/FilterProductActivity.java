package com.example.bepnhataapp.features.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.databinding.ActivityFilterProductBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

public class FilterProductActivity extends AppCompatActivity {

    ActivityFilterProductBinding binding;

    private final String[] ingredientTags = {"Thịt heo","Thịt gà","Thịt bò","Hải sản","Trứng","Đậu phụ","Rau củ","Nấm"};
    private final String[] regionTags = {"Miền Bắc","Miền Trung","Miền Nam","Miền Tây","Món Âu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupTagFlex(binding.flexIngredients, ingredientTags);
        setupTagFlex(binding.flexRegions, regionTags);

        // sliders
        binding.sliderKcal.setLabelFormatter(value -> String.valueOf((int) value));
        binding.sliderKcal.addOnChangeListener((slider, value, fromUser) -> binding.tvKcalValue.setText(String.valueOf((int) value)));
        binding.sliderKcal.setValue(200);

        binding.sliderTime.setLabelFormatter(value -> String.valueOf((int) value));
        binding.sliderTime.addOnChangeListener((slider, value, fromUser) -> binding.tvTimeValue.setText(String.valueOf((int) value)));
        binding.sliderTime.setValue(10);

        binding.btnClose.setOnClickListener(v -> finish());
        binding.btnApply.setOnClickListener(v -> finish());
    }

    private void setupTagFlex(FlexboxLayout flex, String[] tags){
        boolean isRegion = (flex == binding.flexRegions);
        for(String tag: tags){
            MaterialButton chip = new MaterialButton(this,null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            chip.setText(tag);
            chip.setTextSize(14);
            chip.setStrokeColorResource(R.color.primary1);
            chip.setStrokeWidth(2);
            chip.setRippleColorResource(R.color.primary3);
            chip.setPaddingRelative(20,8,20,8);
            chip.setCornerRadius(24);
            styleChip(chip,false);

            chip.setOnClickListener(v -> {
                if(isRegion){ // single selection
                    for(int i=0;i<flex.getChildCount();i++){
                        MaterialButton mb=(MaterialButton) flex.getChildAt(i);
                        boolean sel = (mb==chip);
                        styleChip(mb,sel);
                        mb.setTag(sel);
                    }
                }else{ // multi toggle
                    boolean current = chip.getTag()!=null && (boolean)chip.getTag();
                    styleChip(chip,!current);
                    chip.setTag(!current);
                }
            });

            chip.setTag(false);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            int m = (int) getResources().getDisplayMetrics().density * 4; // 4dp each side => 8dp gap between chips
            lp.setMargins(m,m,m,m);
            chip.setLayoutParams(lp);
            flex.addView(chip);
        }
    }

    private void styleChip(MaterialButton chip, boolean selected){
        if(selected){
            chip.setBackgroundTintList(getColorStateList(R.color.primary1));
            chip.setTextColor(getResources().getColor(android.R.color.white));
        }else{
            chip.setBackgroundTintList(getColorStateList(android.R.color.white));
            chip.setTextColor(getResources().getColor(R.color.primary1));
        }
    }
}
