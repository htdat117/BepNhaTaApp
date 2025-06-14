package com.example.bepnhataapp.features.mealplan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import com.example.bepnhataapp.features.mealplan.data.FakeSavedPlanRepository;
import java.util.List;

public class LoadMealPlanActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private Spinner spinnerSavedPlan;
    private TextView tvSelectedDate;

    private LocalDate selectedDate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_meal_plan);

        // Back button behaviour
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        spinnerSavedPlan = findViewById(R.id.spinnerSavedPlan);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        setupSavedPlanSpinner();
        updateSelectedDateLabel();

        // Date picker handler
        findViewById(R.id.btnChooseDate).setOnClickListener(v -> openDatePicker());

        // Next button action
        findViewById(R.id.btnNext).setOnClickListener(v -> {
            String planName = (String) spinnerSavedPlan.getSelectedItem();
            if (planName == null) {
                Toast.makeText(this, "Vui lòng chọn thực đơn", Toast.LENGTH_SHORT).show();
                return;
            }

            android.content.Intent intent = new android.content.Intent(this, LoadMealPlanReviewActivity.class);
            intent.putExtra("PLAN_NAME", planName);
            intent.putExtra("SELECTED_DATE", selectedDate.toString());
            startActivity(intent);
        });

        setupBottomNavigationFragment(R.id.nav_meal_plan);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void setupSavedPlanSpinner() {
        FakeSavedPlanRepository repo = new FakeSavedPlanRepository();
        List<String> plans = repo.getPlanNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, plans);
        spinnerSavedPlan.setAdapter(adapter);
    }

    private void openDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    updateSelectedDateLabel();
                },
                selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
        dialog.show();
    }

    private void updateSelectedDateLabel() {
        tvSelectedDate.setText(buildVietnameseDateString(selectedDate));
    }

    private String buildVietnameseDateString(@NonNull LocalDate date) {
        String[] weekdays = {"Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        DayOfWeek dow = date.getDayOfWeek();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        return weekdays[dow.getValue() % 7] + ", Ngày " + day + " tháng " + month;
    }
} 