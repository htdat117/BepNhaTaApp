package com.example.bepnhataapp.features.tools;

import android.animation.ObjectAnimator;
import android.graphics.Color;
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
    private View tabIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating ToolsContainerFragment view");
        View view = inflater.inflate(R.layout.fragment_bmi_bmr_calo, container, false);

        // Ánh xạ các View từ layout chính
        tabIndicator = view.findViewById(R.id.tabIndicator);
        btnBmiBmrTab = view.findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = view.findViewById(R.id.btnCaloTab);

        if (tabIndicator == null || btnBmiBmrTab == null || btnCaloTab == null) {
            Log.e(TAG, "onCreateView: Failed to find tab views");
            return view;
        }

        // Load fragment BMI/BMR content mặc định
        loadFragment(new BmiBmrContentFragment());

        // Đặt chiều rộng tabIndicator bằng với btnBmiBmrTab khi layout xong
        btnBmiBmrTab.post(() -> {
            try {
                int width = btnBmiBmrTab.getWidth();
                ViewGroup.LayoutParams params = tabIndicator.getLayoutParams();
                params.width = width;
                tabIndicator.setTranslationX(0);
                Log.d(TAG, "Tab indicator width set to: " + width);
            } catch (Exception e) {
                Log.e(TAG, "Error setting tab indicator width", e);
            }
        });

        // Gán sự kiện click cho các nút tab
        btnBmiBmrTab.setOnClickListener(v -> {
            Log.d(TAG, "BMI/BMR tab clicked");
            animateTab(0);
            btnBmiBmrTab.setTextColor(Color.WHITE);
            btnCaloTab.setTextColor(Color.BLACK);
            loadFragment(new BmiBmrContentFragment());
        });

        btnCaloTab.setOnClickListener(v -> {
            Log.d(TAG, "Calo tab clicked");
            int width = btnBmiBmrTab.getWidth();
            animateTab(width);
            btnBmiBmrTab.setTextColor(Color.BLACK);
            btnCaloTab.setTextColor(Color.WHITE);
            loadFragment(new CaloCalculatorFragment()); // Load CaloCalculatorFragment
        });

        return view;
    }

    private void animateTab(int toX) {
        try {
            ObjectAnimator animator = ObjectAnimator.ofFloat(tabIndicator, "translationX", toX);
            animator.setDuration(200);
            animator.start();
            Log.d(TAG, "Tab animation started to position: " + toX);
        } catch (Exception e) {
            Log.e(TAG, "Error animating tab", e);
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            try {
                getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
                Log.d(TAG, "Fragment loaded: " + fragment.getClass().getSimpleName());
            } catch (Exception e) {
                Log.e(TAG, "Failed to load fragment", e);
            }
        } else {
            Log.e(TAG, "FragmentManager is null. Cannot load fragment.");
        }
    }
} 