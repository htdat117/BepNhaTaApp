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
import com.example.bepnhataapp.common.repository.SavedPlanRepository;
import com.example.bepnhataapp.common.repository.SavedPlanRepository.PlanInfo;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class LoadMealPlanActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private Spinner spinnerSavedPlan;
    private TextView tvSelectedDate;

    private LocalDate selectedDate = LocalDate.now();

    private java.util.List<SavedPlanRepository.PlanInfo> planInfos;

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
            int pos = spinnerSavedPlan.getSelectedItemPosition();
            String planName = (String) spinnerSavedPlan.getSelectedItem();
            if (planName == null) {
                Toast.makeText(this, "Vui lòng chọn thực đơn", Toast.LENGTH_SHORT).show();
                return;
            }

            long planId = planInfos.get(pos).mealPlanId;

            android.content.Intent intent = new android.content.Intent(this, LoadMealPlanReviewActivity.class);
            intent.putExtra("PLAN_NAME", planName);
            intent.putExtra("PLAN_ID", planId);
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
        SavedPlanRepository repo = new SavedPlanRepository(this);
        planInfos = new java.util.ArrayList<>();
        for(String name : repo.getPlanNames()){
            SavedPlanRepository.PlanInfo pi = repo.getPlan(name);
            if(pi!=null) planInfos.add(pi);
        }

        java.util.List<String> titles = new java.util.ArrayList<>();
        for(SavedPlanRepository.PlanInfo pi: planInfos) titles.add(pi.title);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, titles);
        spinnerSavedPlan.setAdapter(adapter);
    }

    private void openDatePicker() {
        // Use MaterialDatePicker for a more modern UX with optional text input mode
        long preselectMillis = selectedDate
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setSelection(preselectMillis)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR) // allow user to toggle to text input icon
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            java.time.Instant instant = java.time.Instant.ofEpochMilli(selection);
            selectedDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            updateSelectedDateLabel();
        });

        picker.show(getSupportFragmentManager(), "DATE_PICKER");
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
