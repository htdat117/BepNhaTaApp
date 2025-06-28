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
import com.example.bepnhataapp.common.repository.LocalMealPlanRepository;
import com.example.bepnhataapp.common.adapter.RecommendedAdapter;
import com.example.bepnhataapp.common.adapter.GridSpacingItemDecoration;
import com.example.bepnhataapp.features.mealplan.WeekTimelineFragment;
import java.time.LocalDate;
import com.example.bepnhataapp.common.base.BaseActivity;
import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;
import com.example.bepnhataapp.common.dao.MealPlanDao;
import com.example.bepnhataapp.common.model.MealPlan;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import android.app.AlertDialog;

public class MealPlanContentActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private LocalDate currentDate;
    private TextView tvCurrentDate;
    private boolean showingWeek = false;
    private MaterialButton btnDayTab, btnWeekTab;
    private MealPlanViewModel mealPlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_content);

        // Khởi tạo ViewModel
        mealPlanViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MealPlanViewModel(new LocalMealPlanRepository(MealPlanContentActivity.this));
            }
        }).get(MealPlanViewModel.class);
        
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        currentDate = LocalDate.now();
        tvCurrentDate = findViewById(R.id.tvCurrentDate);

        // Navigation arrows (move by day or by week depending on mode)
        findViewById(R.id.btnPrevDay).setOnClickListener(v -> {
            currentDate = showingWeek ? currentDate.minusWeeks(1) : currentDate.minusDays(1);
            updateDateLabel();
            if (!showingWeek) loadDayFragment(); else loadWeekFragment();
        });
        findViewById(R.id.btnNextDay).setOnClickListener(v -> {
            currentDate = showingWeek ? currentDate.plusWeeks(1) : currentDate.plusDays(1);
            updateDateLabel();
            if (!showingWeek) loadDayFragment(); else loadWeekFragment();
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

        mealPlanViewModel.getState().observe(this, state -> {
            // Khi dữ liệu thay đổi (ví dụ: sau khi xoá), load lại fragment cho ngày hiện tại
            if (!showingWeek) {
                loadDayFragment();
            } else {
                loadWeekFragment();
            }
        });

        // Đảm bảo người dùng có một kế hoạch cá nhân nếu đã đăng nhập
        ensurePersonalPlanExists();
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
        
        MealPlanDao mealPlanDao = new MealPlanDao(this);
        java.util.List<MealPlan> templates = mealPlanDao.getTemplates();

        java.util.Map<String, java.util.List<MealPlan>> byCat = new java.util.LinkedHashMap<>();
        for (MealPlan mp : templates) {
            String cat = mp.getMealCategory() != null ? mp.getMealCategory() : "Khác";
            byCat.computeIfAbsent(cat, k -> new java.util.ArrayList<>()).add(mp);
        }

        for (java.util.Map.Entry<String, java.util.List<MealPlan>> e : byCat.entrySet()) {
            recoData.add(e.getKey()); // header
            for (MealPlan mp : e.getValue()) {
                int imgRes = R.drawable.placeholder_banner_background;
                String randomImageThumb = mealPlanDao.getRandomRecipeImageForPlan(mp.getMealPlanID());
                String imgStr = randomImageThumb != null ? randomImageThumb : (mp.getImageThumb() != null ? mp.getImageThumb().trim() : "");
                String url = null;
                if (!imgStr.isEmpty()) {
                    if (imgStr.startsWith("http")) {
                        url = imgStr;
                    } else {
                        int resId = getResources().getIdentifier(imgStr, "drawable", getPackageName());
                        if (resId != 0) imgRes = resId;
                    }
                }
                if(url!=null)
                    recoData.add(new RecommendedAdapter.RecoItem(url, mp.getTitle(), mp.getTotalDays()));
                else
                    recoData.add(new RecommendedAdapter.RecoItem(imgRes, mp.getTitle(), mp.getTotalDays()));
            }
        }
        
        RecommendedAdapter recoAdapter = new RecommendedAdapter(recoData);
        
        // Configure GridLayoutManager with proper spacing
        final int spanCount = 2;
        GridLayoutManager glm = new GridLayoutManager(this, spanCount) {
            @Override
            public boolean canScrollVertically() {
                return false; // allow RecyclerView to expand fully inside ScrollView
            }
        };
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
        rvReco.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, true));
        
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
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá toàn bộ thực đơn cho ngày này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        mealPlanViewModel.deletePlanForDate(currentDate);
                        Toast.makeText(this, "Đã xoá thực đơn", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
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
        MealPlanViewModel.State state = mealPlanViewModel.getState().getValue();

        boolean hasDataForDay = false;
        if (state != null && state.week != null) {
            for (com.example.bepnhataapp.common.model.DayPlan d : state.week.days) {
                if (d.date.equals(currentDate)) {
                    hasDataForDay = true;
                    break;
                }
            }
        }

        Fragment fragment;
        if (!hasDataForDay) {
            fragment = new EmptyPlanFragment();
        } else {
            fragment = TimelineFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dayPlanContainer, fragment)
                .commit();
        
        // Setup recommended section after layout pass
        setupRecommendedSectionAfterFragmentLoad();
    }

    private void loadWeekFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dayPlanContainer, WeekTimelineFragment.newInstance())
                .commit();

        // Setup recommended section after layout pass
        setupRecommendedSectionAfterFragmentLoad();
    }

    public LocalDate getCurrentDate() { return currentDate; }

    private void ensurePersonalPlanExists() {
        if (SessionManager.isLoggedIn(this)) {
            new Thread(() -> {
                CustomerDao customerDao = new CustomerDao(this);
                Customer customer = customerDao.findByPhone(SessionManager.getPhone(this));
                if (customer != null) {
                    long customerId = customer.getCustomerID();
                    MealPlanDao mealPlanDao = new MealPlanDao(this);
                    if (mealPlanDao.getPersonalPlan(customerId) == null) {
                        // Kế hoạch cá nhân chưa tồn tại -> tạo mới
                        MealPlan personalPlan = new MealPlan();
                        personalPlan.setCustomerID(customerId);
                        personalPlan.setType("PERSONAL");
                        personalPlan.setTitle("Kế hoạch của tôi");
                        personalPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
                        mealPlanDao.insert(personalPlan);
                    }
                }
            }).start();
        }
    }
} 