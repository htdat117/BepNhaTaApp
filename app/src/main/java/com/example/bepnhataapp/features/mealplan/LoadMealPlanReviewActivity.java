package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import com.example.bepnhataapp.features.mealplan.data.FakeSavedPlanRepository;

public class LoadMealPlanReviewActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_meal_plan_review);

        setupBottomNavigationFragment(R.id.nav_meal_plan);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String planName = getIntent().getStringExtra("PLAN_NAME");
        String dateStr = getIntent().getStringExtra("SELECTED_DATE");
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ignored) {}

        FakeSavedPlanRepository repo = new FakeSavedPlanRepository();
        FakeSavedPlanRepository.PlanInfo info = repo.getPlan(planName);

        if (info != null) {
            ((TextView) findViewById(R.id.tvDetailTitle)).setText(info.title);
            ((TextView) findViewById(R.id.tvDetailDescription)).setText(info.description);
            ((TextView) findViewById(R.id.tvDetailDayCount)).setText(info.dayCount + " ngày");
            ((TextView) findViewById(R.id.tvDetailCal)).setText(info.avgCal + " Kcal");
            ((TextView) findViewById(R.id.tvDetailCarbs)).setText(info.avgCarbs + "g");
            ((TextView) findViewById(R.id.tvDetailFat)).setText(info.avgFat + "g");
            ((TextView) findViewById(R.id.tvDetailProtein)).setText(info.avgProtein + "g");
        }

        ((TextView) findViewById(R.id.tvDetailLoadDate)).setText(date != null ? buildVietnameseDateString(date) : "");

        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            Toast.makeText(this, "Đã tải kế hoạch: " + planName, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private String buildVietnameseDateString(LocalDate date) {
        String[] weekdays = {"Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        return weekdays[date.getDayOfWeek().getValue() % 7] + ", Ngày " + date.getDayOfMonth() + " tháng " + date.getMonthValue();
    }
} 