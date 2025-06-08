package com.example.bepnhataapp.features.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import com.example.bepnhataapp.R;

public class BmiBmrFragment extends Fragment {
    private Button btnBmiBmrTab, btnCaloTab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi_bmr_calo, container, false);
        btnBmiBmrTab = view.findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = view.findViewById(R.id.btnCaloTab);

        // Mặc định hiển thị fragment BMI/BMR
        showBmiBmr();

        btnBmiBmrTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBmiBmr();
            }
        });
        btnCaloTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalo();
            }
        });
        return view;
    }

    private void showBmiBmr() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new BmiBmrContentFragment());
        ft.commit();
        btnBmiBmrTab.setBackgroundResource(R.drawable.bg_tab_selected);
        btnBmiBmrTab.setTextColor(getResources().getColor(android.R.color.white));
        btnCaloTab.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnCaloTab.setTextColor(getResources().getColor(R.color.dark1));
    }

    private void showCalo() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new CaloCalculatorFragment());
        ft.commit();
        btnCaloTab.setBackgroundResource(R.drawable.bg_tab_selected);
        btnCaloTab.setTextColor(getResources().getColor(android.R.color.white));
        btnBmiBmrTab.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnBmiBmrTab.setTextColor(getResources().getColor(R.color.dark1));
    }
} 