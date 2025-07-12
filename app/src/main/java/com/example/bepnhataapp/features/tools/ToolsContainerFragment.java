package com.example.bepnhataapp.features.tools;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.bmibmr.BmiBmrContentFragment;
import com.example.bepnhataapp.features.calocal.CaloCalculatorFragment;

public class ToolsContainerFragment extends Fragment {
    private static final String TAG = "ToolsContainerFragment";

    private Button btnBmiBmrTab, btnCaloTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating ToolsContainerFragment view");
        View view = inflater.inflate(R.layout.fragment_bmi_bmr_calo, container, false);

        // Ánh xạ các View từ layout chính
        btnBmiBmrTab = view.findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = view.findViewById(R.id.btnCaloTab);

        if (btnBmiBmrTab == null || btnCaloTab == null) {
            Log.e(TAG, "onCreateView: Failed to find tab buttons");
            return view;
        }

        // Load fragment BMI/BMR content mặc định
        loadFragment(new BmiBmrContentFragment());
        btnBmiBmrTab.setSelected(true); // Đặt trạng thái selected ban đầu cho BMI/BMR
        btnCaloTab.setSelected(false); // Đặt trạng thái unselected ban đầu cho Calo

        // Gán sự kiện click cho các nút tab
        btnBmiBmrTab.setOnClickListener(v -> {
            Log.d(TAG, "BMI/BMR tab clicked");
            btnBmiBmrTab.setSelected(true); // Cập nhật trạng thái selected
            btnCaloTab.setSelected(false); // Cập nhật trạng thái unselected
            loadFragment(new BmiBmrContentFragment());
        });

        btnCaloTab.setOnClickListener(v -> {
            Log.d(TAG, "Calo tab clicked");
            btnCaloTab.setSelected(true); // Cập nhật trạng thái selected
            btnBmiBmrTab.setSelected(false); // Cập nhật trạng thái unselected
            loadFragment(new CaloCalculatorFragment());
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
} 
