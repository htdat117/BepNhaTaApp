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
import com.example.bepnhataapp.common.dao.MealDayDao;
import com.example.bepnhataapp.common.dao.MealTimeDao;
import com.example.bepnhataapp.common.dao.MealRecipeDao;

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
                return (T) new MealPlanViewModel(new LocalMealPlanRepository(getApplicationContext()));
            }
        }).get(MealPlanViewModel.class);
        
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> MealPlanContentActivity.this.finish());

        String dateExtraStr = getIntent().getStringExtra("SELECTED_DATE");
        if(dateExtraStr!=null){
            try{
                currentDate = java.time.LocalDate.parse(dateExtraStr);
            }catch(Exception e){
                currentDate = LocalDate.now();
            }
        }else{
            currentDate = LocalDate.now();
        }
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
                    recoData.add(new RecommendedAdapter.RecoItem(url, mp.getTitle(), mp.getTotalDays(), mp.getMealPlanID()));
                else
                    recoData.add(new RecommendedAdapter.RecoItem(imgRes, mp.getTitle(), mp.getTotalDays(), mp.getMealPlanID()));
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
        glm.setAutoMeasureEnabled(true);
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

        // Ẩn / hiện tuỳ chọn "Xoá ghi chú" tuỳ theo ngày hiện tại có ghi chú hay không
        try {
            com.example.bepnhataapp.common.dao.MealDayDao dayDaoCheck = new com.example.bepnhataapp.common.dao.MealDayDao(this);
            java.util.List<com.example.bepnhataapp.common.model.MealDay> daysCheck = dayDaoCheck.getAllByDate(currentDate.toString());
            boolean hasNote = false;
            for(com.example.bepnhataapp.common.model.MealDay dDay: daysCheck){
                if(dDay.getNote()!=null && !dDay.getNote().trim().isEmpty()) { hasNote = true; break; }
            }
            popup.getMenu().findItem(R.id.menu_delete_note).setVisible(hasNote);
        } catch (Exception ignored) {}

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_refresh_day) {
                if (showingWeek) {
                    // Refresh the entire week that contains currentDate
                    mealPlanViewModel.autoGenerateWeekFor(currentDate);
                    Toast.makeText(this, "Đã làm mới thực đơn tuần", Toast.LENGTH_SHORT).show();
                } else {
                    // Refresh only the selected day
                    mealPlanViewModel.autoGenerateFor(currentDate);
                    Toast.makeText(this, "Đã làm mới thực đơn ngày", Toast.LENGTH_SHORT).show();
                }
                // Refresh UI to display updated meals
                refreshDayView();
            } else if (id == R.id.menu_edit_layout) {
                Toast.makeText(this, "Chỉnh sửa bố cục", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_save_template) {
                boolean saveWeek = showingWeek;
                showSaveTemplateDialog(saveWeek);
            } else if (id == R.id.menu_load_template) {
                startActivity(new android.content.Intent(this, LoadMealPlanActivity.class));
            } else if (id == R.id.menu_share_template) {
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_add_note) {
                // Hiển thị dialog nhập ghi chú cho ngày
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Nhập ghi chú cho ngày");
                input.setMinLines(2);
                input.setMaxLines(4);
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Thêm ghi chú")
                    .setView(input)
                    .setPositiveButton("Lưu", (d, w) -> {
                        String note = input.getText().toString().trim();
                        new Thread(() -> {
                            com.example.bepnhataapp.common.dao.MealDayDao dayDao = new com.example.bepnhataapp.common.dao.MealDayDao(getApplicationContext());
                            java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dayDao.getAllByDate(currentDate.toString());
                            if(days.isEmpty()){
                                // nếu chưa có MealDay cho ngày này (có thể do kế hoạch trống), tạo placeholder vào AUTO plan đầu tiên
                                com.example.bepnhataapp.common.dao.MealPlanDao mpDao = new com.example.bepnhataapp.common.dao.MealPlanDao(getApplicationContext());
                                java.util.List<com.example.bepnhataapp.common.model.MealPlan> autos = mpDao.getAutoPlans();
                                long planId;
                                if(autos.isEmpty()){
                                    com.example.bepnhataapp.common.model.MealPlan np = new com.example.bepnhataapp.common.model.MealPlan();
                                    np.setType("AUTO");
                                    np.setTitle("Kế hoạch tự động");
                                    np.setCreatedAt(java.time.LocalDateTime.now().toString());
                                    planId = mpDao.insert(np);
                                } else {
                                    planId = autos.get(0).getMealPlanID();
                                }
                                com.example.bepnhataapp.common.model.MealDay nd = new com.example.bepnhataapp.common.model.MealDay(planId, currentDate.toString(), note);
                                dayDao.insert(nd);
                            } else {
                                for(com.example.bepnhataapp.common.model.MealDay dDay: days){
                                    dDay.setNote(note);
                                    dayDao.update(dDay);
                                }
                            }
                            runOnUiThread(() -> {
                                mealPlanViewModel.refresh();
                                refreshDayView();
                                android.widget.Toast.makeText(this, "Đã lưu ghi chú", android.widget.Toast.LENGTH_SHORT).show();
                            });
                        }).start();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
            } else if (id == R.id.menu_delete_note) {
                new AlertDialog.Builder(this)
                     .setTitle("Xoá ghi chú")
                     .setMessage("Bạn có chắc chắn muốn xoá ghi chú cho ngày này không?")
                     .setPositiveButton("Xoá", (d, w) -> {
                        new Thread(() -> {
                            com.example.bepnhataapp.common.dao.MealDayDao dDao = new com.example.bepnhataapp.common.dao.MealDayDao(getApplicationContext());
                            java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dDao.getAllByDate(currentDate.toString());
                            for(com.example.bepnhataapp.common.model.MealDay dDay: days){
                                dDay.setNote(null);
                                dDao.update(dDay);
                            }
                            runOnUiThread(() -> {
                                mealPlanViewModel.refresh();
                                refreshDayView();
                                android.widget.Toast.makeText(this, "Đã xoá ghi chú", android.widget.Toast.LENGTH_SHORT).show();
                            });
                        }).start();
                     })
                     .setNegativeButton("Huỷ", null)
                     .show();
            } else if (id == R.id.menu_delete_day) {
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá toàn bộ thực đơn cho ngày này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        mealPlanViewModel.deletePlanForDate(currentDate);
                        Toast.makeText(this, "Đã xoá thực đơn", Toast.LENGTH_SHORT).show();
                        loadDayFragment();
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

    /**
     * Refresh current view after data changes (e.g., copy or delete day).
     */
    public void refreshDayView(){
        if(!showingWeek){
            loadDayFragment();
        }else{
            loadWeekFragment();
        }
    }

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

    /**
     * Hiển thị dialog nhập tên mẫu thực đơn, sau đó lưu xuống DB.
     * @param saveWeek true = lưu cả tuần, false = chỉ lưu ngày hiện tại
     */
    private void showSaveTemplateDialog(boolean saveWeek){
        int paddingPx = (int)(16 * getResources().getDisplayMetrics().density);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        android.widget.EditText titleInput = new android.widget.EditText(this);
        titleInput.setHint("Tiêu đề");
        layout.addView(titleInput);

        android.widget.EditText descInput = new android.widget.EditText(this);
        descInput.setHint("Mô tả (tuỳ chọn)");
        descInput.setMinLines(2);
        descInput.setMaxLines(4);
        layout.addView(descInput);

        new android.app.AlertDialog.Builder(this)
            .setTitle("Lưu mẫu thực đơn")
            .setView(layout)
            .setPositiveButton("Lưu", (dlg, which)->{
                String name = titleInput.getText().toString().trim();
                String desc = descInput.getText().toString().trim();
                if(name.isEmpty()){
                    android.widget.Toast.makeText(this, "Vui lòng nhập tiêu đề", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(() -> {
                    saveTemplateToDatabase(name, desc, saveWeek);
                }).start();
            })
            .setNegativeButton("Huỷ", null)
            .show();
    }

    /**
     * Sao chép thực đơn hiện tại (1 ngày hoặc cả tuần) thành MealPlan mới với type="DOWNLOADED".
     */
    private void saveTemplateToDatabase(String title, String description, boolean isWeek){
        MealPlanDao mpDao = new MealPlanDao(this);
        MealDayDao dayDao = new MealDayDao(this);
        MealTimeDao timeDao = new MealTimeDao(this);
        MealRecipeDao recDao = new MealRecipeDao(this);

        // 1. Tạo MealPlan mới
        com.example.bepnhataapp.common.model.MealPlan newPlan = new com.example.bepnhataapp.common.model.MealPlan();
        newPlan.setMealCategory("Template");
        newPlan.setTitle(title);
        newPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
        newPlan.setType("DOWNLOADED");
        newPlan.setNote(description);

        Long cid = null;
        if(SessionManager.isLoggedIn(this)){
            com.example.bepnhataapp.common.dao.CustomerDao custDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
            com.example.bepnhataapp.common.model.Customer c = custDao.findByPhone(SessionManager.getPhone(this));
            if(c!=null) cid = c.getCustomerID();
        }
        newPlan.setCustomerID(cid);

        long newPlanId = mpDao.insert(newPlan);
        newPlan.setMealPlanID(newPlanId);

        java.util.List<java.time.LocalDate> datesToCopy = new java.util.ArrayList<>();
        if(isWeek){
            java.time.LocalDate monday = currentDate.with(java.time.DayOfWeek.MONDAY);
            for(int i=0;i<7;i++){
                datesToCopy.add(monday.plusDays(i));
            }
        }else{
            datesToCopy.add(currentDate);
        }

        int daysCopied = 0;

        for(java.time.LocalDate date : datesToCopy){
            java.util.List<com.example.bepnhataapp.common.model.MealDay> srcDays = dayDao.getAllByDate(date.toString());
            if(srcDays.isEmpty()) continue; // skip empty dates

            // Chọn MealDay trùng với kế hoạch đang hiển thị (PERSONAL ưu tiên nếu đã đăng nhập)
            com.example.bepnhataapp.common.model.MealDay src = null;
            Long personalPlanId = null;
            if(SessionManager.isLoggedIn(this)){
                com.example.bepnhataapp.common.dao.CustomerDao cd = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer cu = cd.findByPhone(SessionManager.getPhone(this));
                if(cu!=null){
                    com.example.bepnhataapp.common.dao.MealPlanDao mpd = new com.example.bepnhataapp.common.dao.MealPlanDao(this);
                    com.example.bepnhataapp.common.model.MealPlan pers = mpd.getPersonalPlan(cu.getCustomerID());
                    if(pers!=null) personalPlanId = pers.getMealPlanID();
                }
            }

            Long autoPlanId = null;
            if(personalPlanId==null){
                java.util.List<com.example.bepnhataapp.common.model.MealPlan> autos = new com.example.bepnhataapp.common.dao.MealPlanDao(this).getAutoPlans();
                if(!autos.isEmpty()) autoPlanId = autos.get(0).getMealPlanID();
            }

            for(com.example.bepnhataapp.common.model.MealDay d : srcDays){
                if(personalPlanId!=null && d.getMealPlanID()==personalPlanId){ src=d; break; }
                if(personalPlanId==null && autoPlanId!=null && d.getMealPlanID()==autoPlanId){ src=d; break; }
            }

            if(src==null) src = srcDays.get(0);

            // For each src day (e.g., PERSONAL, AUTO) copy its content into newPlan (merge)
            daysCopied++;
            com.example.bepnhataapp.common.model.MealDay newDay = new com.example.bepnhataapp.common.model.MealDay();
            newDay.setMealPlanID(newPlanId);
            newDay.setDate(date.toString());
            newDay.setNote(null);
            long newDayId = dayDao.insert(newDay);

            java.util.List<com.example.bepnhataapp.common.model.MealTime> srcTimes = timeDao.getByMealDay(src.getMealDayID());

            for(com.example.bepnhataapp.common.model.MealTime st : srcTimes){
                com.example.bepnhataapp.common.model.MealTime nt = new com.example.bepnhataapp.common.model.MealTime();
                nt.setMealDayID(newDayId);
                nt.setMealType(st.getMealType());
                nt.setNote(st.getNote());
                long ntId = timeDao.insert(nt);

                java.util.List<com.example.bepnhataapp.common.model.MealRecipe> srcRecipes = recDao.getRecipesForMealTime(st.getMealTimeID());
                for(com.example.bepnhataapp.common.model.MealRecipe sr : srcRecipes){
                    recDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(ntId, sr.getRecipeID()));
                }
            }
        }

        // Cập nhật totalDays
        newPlan.setTotalDays(daysCopied);
        mpDao.update(newPlan);

        runOnUiThread(() -> android.widget.Toast.makeText(this, "Đã lưu mẫu thực đơn", android.widget.Toast.LENGTH_SHORT).show());
    }
} 
