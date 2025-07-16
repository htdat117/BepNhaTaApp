package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import com.example.bepnhataapp.common.repository.SavedPlanRepository;
import com.example.bepnhataapp.common.dao.MealPlanDao;
import com.example.bepnhataapp.common.dao.MealDayDao;
import com.example.bepnhataapp.common.dao.MealTimeDao;
import com.example.bepnhataapp.common.dao.MealRecipeDao;
import com.example.bepnhataapp.common.model.MealPlan;
import com.example.bepnhataapp.common.model.MealDay;
import com.example.bepnhataapp.common.model.MealTime;
import com.example.bepnhataapp.common.model.MealRecipe;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.features.mealplan.MealPlanContentActivity;

public class LoadMealPlanReviewActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_meal_plan_review);

        setupBottomNavigationFragment(R.id.nav_meal_plan);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String planName = getIntent().getStringExtra("PLAN_NAME");
        long planIdExtra = getIntent().getLongExtra("PLAN_ID", -1);
        String dateStr = getIntent().getStringExtra("SELECTED_DATE");
        LocalDate dateTemp = null;
        try {
            dateTemp = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ignored) {}

        final LocalDate selectedDateFinal = dateTemp;

        SavedPlanRepository repo = new SavedPlanRepository(this);
        SavedPlanRepository.PlanInfo info = planIdExtra!=-1? repo.getPlan(planIdExtra): repo.getPlan(planName);

        if (info != null) {
            ((TextView) findViewById(R.id.tvDetailTitle)).setText(info.title);
            ((TextView) findViewById(R.id.tvDetailDescription)).setText(info.description);
            ((TextView) findViewById(R.id.tvDetailDayCount)).setText(info.dayCount + " ngày");
            ((TextView) findViewById(R.id.tvDetailCal)).setText(info.avgCal + " Kcal");
            ((TextView) findViewById(R.id.tvDetailCarbs)).setText(info.avgCarbs + "g");
            ((TextView) findViewById(R.id.tvDetailFat)).setText(info.avgFat + "g");
            ((TextView) findViewById(R.id.tvDetailProtein)).setText(info.avgProtein + "g");
            setupPreviewList(info.mealPlanId);
        }

        ((TextView) findViewById(R.id.tvDetailLoadDate)).setText(selectedDateFinal != null ? buildVietnameseDateString(selectedDateFinal) : "");

        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            new Thread(() -> {
                applyTemplatePlan(info.mealPlanId, selectedDateFinal);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã tải kế hoạch: " + info.title, Toast.LENGTH_SHORT).show();
                    android.content.Intent intent = new android.content.Intent(this, MealPlanContentActivity.class);
                    intent.putExtra("SELECTED_DATE", selectedDateFinal!=null? selectedDateFinal.toString():null);
                    startActivity(intent);
                    finish();
                });
            }).start();
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

    /**
     * Sao chép nội dung kế hoạch mẫu (templateId) vào plan đích của user bắt đầu tại startDate.
     * Ghi đè các ngày đã tồn tại trong khoảng.
     */
    private void applyTemplatePlan(long templatePlanId, java.time.LocalDate startDate){
        if(templatePlanId<=0) return;

        MealPlanDao mpDao = new MealPlanDao(this);
        MealDayDao dayDao = new MealDayDao(this);
        MealTimeDao timeDao = new MealTimeDao(this);
        MealRecipeDao recDao = new MealRecipeDao(this);

        // Determine target plan (personal if logged in else auto). Create if missing.
        MealPlan targetPlan = null;
        if(SessionManager.isLoggedIn(this)){
            com.example.bepnhataapp.common.dao.CustomerDao cdao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
            com.example.bepnhataapp.common.model.Customer c = cdao.findByPhone(SessionManager.getPhone(this));
            if(c!=null){
                targetPlan = mpDao.getPersonalPlan(c.getCustomerID());
                if(targetPlan==null){
                    targetPlan = new MealPlan();
                    targetPlan.setCustomerID(c.getCustomerID());
                    targetPlan.setType("PERSONAL");
                    targetPlan.setTitle("Kế hoạch của tôi");
                    targetPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
                    long id = mpDao.insert(targetPlan);
                    targetPlan.setMealPlanID(id);
                }
            }
        }
        if(targetPlan==null){
            java.util.List<MealPlan> autos = mpDao.getAutoPlans();
            if(autos.isEmpty()){
                targetPlan = new MealPlan();
                targetPlan.setType("AUTO");
                targetPlan.setTitle("Kế hoạch tự động");
                targetPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
                long id = mpDao.insert(targetPlan);
                targetPlan.setMealPlanID(id);
            }else targetPlan = autos.get(0);
        }

        // Get template days sorted by date asc
        java.util.List<MealDay> templateDays = dayDao.getByMealPlan(templatePlanId);
        java.util.Collections.sort(templateDays, (a,b)-> a.getDate().compareTo(b.getDate()));

        int offset=0;
        for(MealDay tplDay : templateDays){
            java.time.LocalDate targetDate = startDate.plusDays(offset++);

            // Delete any existing day for targetPlan on that date
            java.util.List<MealDay> existDays = dayDao.getAllByDate(targetDate.toString());
            for(MealDay d : existDays){
                if(d.getMealPlanID()==targetPlan.getMealPlanID()){
                    java.util.List<MealTime> oldTimes = timeDao.getByMealDay(d.getMealDayID());
                    for(MealTime t: oldTimes){
                        recDao.deleteByMealTime(t.getMealTimeID());
                        timeDao.delete(t.getMealTimeID());
                    }
                    dayDao.delete(d.getMealDayID());
                }
            }

            // Copy day
            MealDay newDay = new MealDay();
            newDay.setMealPlanID(targetPlan.getMealPlanID());
            newDay.setDate(targetDate.toString());
            newDay.setNote(tplDay.getNote());
            long newDayId = dayDao.insert(newDay);

            for(MealTime tplTime : timeDao.getByMealDay(tplDay.getMealDayID())){
                MealTime nt = new MealTime();
                nt.setMealDayID(newDayId);
                nt.setMealType(tplTime.getMealType());
                nt.setNote(tplTime.getNote());
                long ntId = timeDao.insert(nt);

                for(MealRecipe mr : recDao.getRecipesForMealTime(tplTime.getMealTimeID())){
                    recDao.insert(new MealRecipe(ntId, mr.getRecipeID()));
                }
            }
        }

        targetPlan.setTotalDays(dayDao.getByMealPlan(targetPlan.getMealPlanID()).size());
        mpDao.update(targetPlan);
    }

    private void setupPreviewList(long templatePlanId){
        androidx.recyclerview.widget.RecyclerView rv = findViewById(R.id.rvPreview);
        if(rv==null) return;
        rv.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        java.util.List<com.example.bepnhataapp.common.adapter.DayPreviewAdapter.DayData> dayDataList = new java.util.ArrayList<>();

        com.example.bepnhataapp.common.dao.MealDayDao dayDao = new com.example.bepnhataapp.common.dao.MealDayDao(this);
        com.example.bepnhataapp.common.dao.MealTimeDao timeDao = new com.example.bepnhataapp.common.dao.MealTimeDao(this);
        com.example.bepnhataapp.common.dao.MealRecipeDao recDao = new com.example.bepnhataapp.common.dao.MealRecipeDao(this);
        com.example.bepnhataapp.common.dao.RecipeDao recipeDao = new com.example.bepnhataapp.common.dao.RecipeDao(this);

        java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dayDao.getByMealPlan(templatePlanId);
        java.util.Collections.sort(days, (a,b)-> a.getDate().compareTo(b.getDate()));

        for(com.example.bepnhataapp.common.model.MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            java.util.List<com.example.bepnhataapp.common.adapter.MealTimeListAdapter.MealTimeWithMeals> mealTimesList = new java.util.ArrayList<>();
            for(com.example.bepnhataapp.common.model.MealTime t : times){
                java.util.List<com.example.bepnhataapp.common.adapter.MealAdapter.MealRow> rows = new java.util.ArrayList<>();
                for(com.example.bepnhataapp.common.model.MealRecipe mr : recDao.getRecipesForMealTime(t.getMealTimeID())){
                    com.example.bepnhataapp.common.model.RecipeEntity re = recipeDao.getById(mr.getRecipeID());
                    String name = re!=null? re.getRecipeName():"Món";
                    String url = re!=null? re.getImageThumb():null;
                    if(url!=null && url.startsWith("http"))
                        rows.add(new com.example.bepnhataapp.common.adapter.MealAdapter.MealRow(name, url));
                    else {
                        int res = getResources().getIdentifier(url!=null?url.trim():"",
                                "drawable", getPackageName());
                        if(res==0) res = com.example.bepnhataapp.R.drawable.placeholder_banner_background;
                        rows.add(new com.example.bepnhataapp.common.adapter.MealAdapter.MealRow(name, res));
                    }
                }
                if(!rows.isEmpty()){
                    String label = t.getMealType();
                    mealTimesList.add(new com.example.bepnhataapp.common.adapter.MealTimeListAdapter.MealTimeWithMeals(label, rows));
                }
            }
            if(!mealTimesList.isEmpty()){
                String vietnameseDate = buildVietnameseDateString(java.time.LocalDate.parse(d.getDate()));
                dayDataList.add(new com.example.bepnhataapp.common.adapter.DayPreviewAdapter.DayData(vietnameseDate, mealTimesList));
            }
        }

        com.example.bepnhataapp.common.adapter.DayPreviewAdapter adapter = new com.example.bepnhataapp.common.adapter.DayPreviewAdapter(dayDataList);
        rv.setAdapter(adapter);
    }
} 
