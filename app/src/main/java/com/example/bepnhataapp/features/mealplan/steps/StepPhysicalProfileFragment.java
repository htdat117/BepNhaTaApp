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
import com.example.bepnhataapp.features.mealplan.steps.CustomerHealthViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import androidx.lifecycle.ViewModelProvider;
import android.widget.EditText;
import android.widget.Toast;

public class StepPhysicalProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_physical_profile, container, false);

        CustomerHealthViewModel vm = new ViewModelProvider(requireActivity()).get(CustomerHealthViewModel.class);

        // Views
        EditText etHeight = view.findViewById(R.id.etHeight);
        EditText etWeight = view.findViewById(R.id.etWeight);
        EditText etAge    = view.findViewById(R.id.etAge);
        ChipGroup cgGender = view.findViewById(R.id.chipGroupGender);
        ChipGroup cgBody   = view.findViewById(R.id.chipGroupBody);

        // restore if exists
        if(vm.draft.getHeight()>0) etHeight.setText(String.valueOf((int)vm.draft.getHeight()));
        if(vm.draft.getWeight()>0) etWeight.setText(String.valueOf(vm.draft.getWeight()));
        if(vm.draft.getAge()>0)    etAge.setText(String.valueOf(vm.draft.getAge()));

        // Setup back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MealPlanWizardActivity) {
                MealPlanWizardActivity act = (MealPlanWizardActivity) getActivity();
                if(act!=null){
                    if(act.getSupportFragmentManager()==null){
                        act.finish();
                        return;
                    }
                }
                if(act!=null && act.getCurrentStepIndex()!=0){
                    act.moveToPreviousStep();
                }else{
                    requireActivity().finish();
                }
            }else{
                requireActivity().onBackPressed();
            }
        });

        // Setup next button in header
        TextView btnNext = view.findViewById(R.id.btnNext);
        View.OnClickListener nextListener = v->{
            // validation
            if(cgGender.getCheckedChipId()== View.NO_ID){Toast.makeText(getContext(),"Chọn giới tính",Toast.LENGTH_SHORT).show();return;}
            if(cgBody.getCheckedChipId()== View.NO_ID){Toast.makeText(getContext(),"Chọn tình trạng cơ thể",Toast.LENGTH_SHORT).show();return;}
            String hStr=etHeight.getText().toString().trim();
            String wStr=etWeight.getText().toString().trim();
            String aStr=etAge.getText().toString().trim();
            if(hStr.isEmpty()||wStr.isEmpty()||aStr.isEmpty()){Toast.makeText(getContext(),"Điền đủ chiều cao, cân nặng, tuổi",Toast.LENGTH_SHORT).show();return;}

            try{
                vm.draft.setHeight(Double.parseDouble(hStr));
                vm.draft.setWeight(Double.parseDouble(wStr));
                vm.draft.setAge(Integer.parseInt(aStr));
            }catch(NumberFormatException e){Toast.makeText(getContext(),"Giá trị không hợp lệ",Toast.LENGTH_SHORT).show();return;}

            Chip selectedGender=(Chip)cgGender.findViewById(cgGender.getCheckedChipId());
            vm.draft.setGender(selectedGender.getText().toString());
            Chip selectedBody=(Chip)cgBody.findViewById(cgBody.getCheckedChipId());
            vm.draft.setBodyType(selectedBody.getText().toString());

            if (getActivity() instanceof MealPlanWizardActivity) {
                ((MealPlanWizardActivity) getActivity()).moveToNextStep();
            }
        };

        btnNext.setOnClickListener(nextListener);

        // Setup next button at bottom
        Button btnNext1 = view.findViewById(R.id.btnNext1);
        btnNext1.setOnClickListener(nextListener);

        return view;
    }
} 
