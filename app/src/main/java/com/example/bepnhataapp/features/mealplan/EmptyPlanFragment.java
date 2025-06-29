package com.example.bepnhataapp.features.mealplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bepnhataapp.R;

public class EmptyPlanFragment extends Fragment {

    private Button btnAuto;
    private Button btnCopy;
    private Button btnLoadBlank;
    private Button btnLoadSaved;
    private MealPlanViewModel viewModel;

    /**
     * Convenience method to get the target date that the current activity is displaying.
     * If the parent activity is not {@link MealPlanContentActivity}, the current day is used.
     */
    private java.time.LocalDate getTargetDate() {
        if (requireActivity() instanceof com.example.bepnhataapp.features.mealplan.MealPlanContentActivity) {
            return ((com.example.bepnhataapp.features.mealplan.MealPlanContentActivity) requireActivity()).getCurrentDate();
        }
        return java.time.LocalDate.now();
    }

    private void setButtonsEnabled(boolean enabled) {
        btnAuto.setEnabled(enabled);
        btnCopy.setEnabled(enabled);
        btnLoadBlank.setEnabled(enabled);
        btnLoadSaved.setEnabled(enabled);
    }

    /**
     * Execute a potentially heavy action on a background thread, displaying a toast on success and
     * refreshing the current view if the parent activity supports it.
     */
    private void runBackgroundTask(Runnable action, String successMsg) {
        setButtonsEnabled(false);
        new Thread(() -> {
            try {
                action.run();
                // Notify UI thread when done
                requireActivity().runOnUiThread(() -> {
                    setButtonsEnabled(true);
                    if (requireActivity() instanceof com.example.bepnhataapp.features.mealplan.MealPlanContentActivity) {
                        ((com.example.bepnhataapp.features.mealplan.MealPlanContentActivity) requireActivity()).refreshDayView();
                    }
                    android.widget.Toast.makeText(requireContext(), successMsg, android.widget.Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                // Basic error handling – re-enable buttons and inform the user
                requireActivity().runOnUiThread(() -> {
                    setButtonsEnabled(true);
                    android.widget.Toast.makeText(requireContext(), "Đã xảy ra lỗi, vui lòng thử lại", android.widget.Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_empty, container, false);

        // Initialise UI components
        btnAuto       = view.findViewById(R.id.btnAutoGenerate);
        btnCopy       = view.findViewById(R.id.btnCopyPrev);
        btnLoadBlank  = view.findViewById(R.id.btnLoadBlank);
        btnLoadSaved  = view.findViewById(R.id.btnLoadSaved);

        viewModel = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        // 1. Tạo tự động
        btnAuto.setOnClickListener(v -> runBackgroundTask(() -> {
            viewModel.autoGenerateFor(getTargetDate());
        }, "Đã tạo thực đơn tự động"));

        // 2. Sao chép ngày trước đó
        btnCopy.setOnClickListener(v -> runBackgroundTask(() -> {
            viewModel.copyFromPreviousDay(getTargetDate());
        }, "Đã sao chép thực đơn ngày trước"));

        // 3. Tạo thực đơn trống (chỉ hiển thị UI, không thao tác DB nặng)
        btnLoadBlank.setOnClickListener(v -> {
            setButtonsEnabled(false);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dayPlanContainer, new BlankDayPlanFragment())
                    .commit();
            setButtonsEnabled(true);
        });

        // 4. Tải thực đơn đã lưu
        btnLoadSaved.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(requireContext(), LoadMealPlanActivity.class);
            startActivity(intent);
        });

        return view;
    }
} 