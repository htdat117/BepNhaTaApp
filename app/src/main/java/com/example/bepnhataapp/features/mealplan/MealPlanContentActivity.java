package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.data.FakeMealPlanRepository;
import com.example.bepnhataapp.features.mealplan.adapters.RecommendedAdapter;
import com.example.bepnhataapp.features.mealplan.adapters.GridSpacingItemDecoration;
import com.example.bepnhataapp.features.mealplan.WeekTimelineFragment;
import java.time.LocalDate;
import com.example.bepnhataapp.common.base.BaseActivity;
import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;

public class MealPlanContentActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private LocalDate currentDate;
    private TextView tvCurrentDate;
    private boolean showingWeek = false;
    private MaterialButton btnDayTab, btnWeekTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_content);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        currentDate = LocalDate.now();
        tvCurrentDate = findViewById(R.id.tvCurrentDate);

        // Navigation arrows (move by day or by week depending on mode)
        findViewById(R.id.btnPrevDay).setOnClickListener(v -> {
            currentDate = showingWeek ? currentDate.minusWeeks(1) : currentDate.minusDays(1);
            updateDateLabel();
        });
        findViewById(R.id.btnNextDay).setOnClickListener(v -> {
            currentDate = showingWeek ? currentDate.plusWeeks(1) : currentDate.plusDays(1);
            updateDateLabel();
        });

        // Action menu
        findViewById(R.id.btnAction).setOnClickListener(v -> showActionMenu(v));

        // New tabs
        btnDayTab = findViewById(R.id.btnDayTab);
        btnWeekTab = findViewById(R.id.btnWeekTab);

        btnDayTab.setSelected(true);

        btnDayTab.setOnClickListener(v -> {
            if (showingWeek) {
                showingWeek = false;
                loadDayFragment();
                btnDayTab.setSelected(true);
                btnWeekTab.setSelected(false);
                updateDateLabel();
            }
        });

        btnWeekTab.setOnClickListener(v -> {
            if (!showingWeek) {
                showingWeek = true;
                loadWeekFragment();
                btnWeekTab.setSelected(true);
                btnDayTab.setSelected(false);
                updateDateLabel();
            }
        });

        updateDateLabel();

        loadDayFragment();

        // Setup bottom navigation
        setupBottomNavigationFragment(R.id.nav_meal_plan);
    }
    
    private void setupRecommendedSectionAfterFragmentLoad() {
        // Wait for layout to be ready to ensure proper sizing
        final View contentContainer = findViewById(R.id.dayPlanContainer);
        contentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove listener to avoid multiple calls
                contentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                
                // Now setup the recommended section
                setupRecommendedSection();
                
                // Force layout refresh
                View scrollView = findViewById(R.id.scrollView);
                if (scrollView != null) {
                    scrollView.requestLayout();
                }
            }
        });
    }
    
    private void setupRecommendedSection() {
        RecyclerView rvReco = findViewById(R.id.rvRecommended);
        java.util.List<Object> recoData = new java.util.ArrayList<>();
        
        // First category
        recoData.add("Nhu cầu dinh dưỡng");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Thực đơn cân bằng dinh dưỡng",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bổ máu",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Giúp làm việc trí não hiệu quả",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Xây dựng cơ xương",7));

        // Second category
        recoData.add("Bữa tối nhanh chóng cho người bận rộn");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 20 phút (thao keo ngon)", 7 ));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 30 phút (không khóe hoang)", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 40 phút (chế biến đơn giản)", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 15 phút (siêu nhanh)", 7));

        // Third category
        recoData.add("Thực đơn ăn chay cho ngày rằm");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Hương Chay 3 Món - 7 Ngày Thanh Lọc", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Hương Chay 5 Món - 7 Ngày Thanh Lọc"));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Chay Thanh Đạm - 5 Món Đơn Giản"));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Chay Tinh Khiết - 10 Món Đặc Biệt"));

        // Fourth category
        recoData.add("Thực đơn cho người tập thể thao");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Tăng cơ giảm mỡ - 7 ngày", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Dinh dưỡng cho runner", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bổ sung protein - 10 món", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Thực đơn tập gym", 7));

        RecommendedAdapter recoAdapter = new RecommendedAdapter(recoData);
        
        // Configure GridLayoutManager with proper spacing
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (recoAdapter.getItemViewType(position) == 0) ? 2 : 1;
            }
        });
        
        // Clear existing decorations to avoid duplicates
        for (int i = 0; i < rvReco.getItemDecorationCount(); i++) {
            rvReco.removeItemDecorationAt(i);
        }
        
        // Add spacing decoration
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        rvReco.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        rvReco.setLayoutManager(glm);
        rvReco.setAdapter(recoAdapter);
        rvReco.setNestedScrollingEnabled(false);
        
        // Force layout recalculation
        rvReco.post(() -> {
            rvReco.requestLayout();
        });
    }

    private void updateDateLabel() {
        if (tvCurrentDate == null) return;
        String text;
        if (showingWeek) {
            java.time.DayOfWeek firstDayOfWeek = java.time.DayOfWeek.MONDAY;
            java.time.LocalDate start = currentDate.with(java.time.temporal.TemporalAdjusters.previousOrSame(firstDayOfWeek));
            java.time.LocalDate end = start.plusDays(6);

            java.time.format.DateTimeFormatter monthDayFmt = java.time.format.DateTimeFormatter.ofPattern("MMM d");
            // Example: Jun 23 – 29 or Jun 28 – Jul 4
            if (start.getMonth() == end.getMonth()) {
                text = start.format(monthDayFmt) + " – " + end.getDayOfMonth();
            } else {
                text = start.format(monthDayFmt) + " – " + end.format(monthDayFmt);
            }
        } else {
            java.time.LocalDate today = java.time.LocalDate.now();
            if (currentDate.isEqual(today)) {
                text = "Hôm nay, " + today.getDayOfMonth() + " tháng " + today.getMonthValue();
            } else {
                text = currentDate.getDayOfMonth() + " tháng " + currentDate.getMonthValue();
            }
        }
        tvCurrentDate.setText(text);
    }

    private void showActionMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.meal_plan_action_menu, popup.getMenu());

        // Force icons to show
        try {
            Field mField = popup.getClass().getDeclaredField("mPopup");
            mField.setAccessible(true);
            Object menuHelper = mField.get(popup);
            Method setForceShowIcon = menuHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_refresh_day) {
                Toast.makeText(this, "Làm mới ngày", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_edit_layout) {
                Toast.makeText(this, "Chỉnh sửa bố cục", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_save_template) {
                Toast.makeText(this, "Lưu mẫu thực đơn", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_load_template) {
                startActivity(new android.content.Intent(this, LoadMealPlanActivity.class));
            } else if (id == R.id.menu_share_template) {
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_add_note) {
                Toast.makeText(this, "Thêm ghi chú", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_delete_day) {
                Toast.makeText(this, "Xoá thực đơn", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        popup.show();
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void loadDayFragment() {
        MealPlanViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
                //noinspection unchecked
                return (T) new MealPlanViewModel(new com.example.bepnhataapp.features.mealplan.data.FakeMealPlanRepository());
            }
        }).get(MealPlanViewModel.class);
        viewModel.getState().observe(this, state -> {
            Fragment fragment = (state.week == null) ? new EmptyPlanFragment() : TimelineFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dayPlanContainer, fragment)
                    .commit();
            
            // Setup recommended section after fragment is loaded
            setupRecommendedSectionAfterFragmentLoad();
        });
    }

    private void loadWeekFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dayPlanContainer, WeekTimelineFragment.newInstance())
                .commit();
    }
} 