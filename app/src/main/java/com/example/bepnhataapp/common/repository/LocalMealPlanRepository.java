package com.example.bepnhataapp.common.repository;

import android.content.Context;
import android.util.Pair;

import com.example.bepnhataapp.common.dao.MealPlanDao;
import com.example.bepnhataapp.common.dao.MealDayDao;
import com.example.bepnhataapp.common.dao.MealTimeDao;
import com.example.bepnhataapp.common.dao.MealRecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.model.DayPlan;
import com.example.bepnhataapp.common.model.Meal;
import com.example.bepnhataapp.common.model.MealDay;
import com.example.bepnhataapp.common.model.MealPlan;
import com.example.bepnhataapp.common.model.MealRecipe;
import com.example.bepnhataapp.common.model.MealTime;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.model.WeekPlan;

import java.time.LocalDate;

/**
 * Basic on-device implementation backed by SQLite via MealPlanDao.
 * Currently only provides hasPlan() check; the rest returns null / no-op to keep
 * compile running until full implementation is provided later.
 */
public class LocalMealPlanRepository implements MealPlanRepository {

    private final MealPlanDao dao;
    private final Context ctx;

    public LocalMealPlanRepository(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        this.dao = new MealPlanDao(this.ctx);
    }

    @Override
    public boolean hasPlan() {
        return !dao.getAll().isEmpty();
    }

    @Override
    public WeekPlan getWeekPlan() {
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao mealRecipeDao = new MealRecipeDao(ctx);
        RecipeDao recipeDao = new RecipeDao(ctx);
        RecipeDetailDao recipeDetailDao = new RecipeDetailDao(ctx);

        java.util.List<MealDay> allDays = new java.util.ArrayList<>();

        // 1. Lấy dữ liệu từ kế hoạch AUTO
        java.util.List<MealPlan> autoPlans = dao.getAutoPlans();
        if (!autoPlans.isEmpty()) {
            allDays.addAll(dayDao.getByMealPlan(autoPlans.get(0).getMealPlanID()));
        }

        // 2. Lấy dữ liệu từ kế hoạch PERSONAL của người dùng (nếu có)
        if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.model.Customer customer = new com.example.bepnhataapp.common.dao.CustomerDao(ctx).findByPhone(com.example.bepnhataapp.common.utils.SessionManager.getPhone(ctx));
            if (customer != null) {
                MealPlan personalPlan = dao.getPersonalPlan(customer.getCustomerID());
                if (personalPlan != null) {
                    allDays.addAll(dayDao.getByMealPlan(personalPlan.getMealPlanID()));
                }
            }
        }

        if (allDays.isEmpty()) return null;

        // Dùng Map để gộp dữ liệu, PERSONAL sẽ ghi đè AUTO nếu trùng ngày
        java.util.Map<java.time.LocalDate, MealDay> finalDayMap = new java.util.LinkedHashMap<>();
        for (MealDay day : allDays) {
            try {
                finalDayMap.put(java.time.LocalDate.parse(day.getDate()), day);
            } catch (Exception ignored) {}
        }
        
        java.util.List<DayPlan> dayPlans = new java.util.ArrayList<>();
        for (MealDay dEnt : finalDayMap.values()) {
            java.util.List<Pair<DayPlan.MealTimeEnum, Meal>> meals = new java.util.ArrayList<>();
            java.util.List<com.example.bepnhataapp.common.model.MealTime> timeEntities = timeDao.getByMealDay(dEnt.getMealDayID());

            for (com.example.bepnhataapp.common.model.MealTime tEnt : timeEntities) {
                DayPlan.MealTimeEnum slot;
                String type = tEnt.getMealType() != null ? tEnt.getMealType().toLowerCase() : "";
                switch (type) {
                    case "sáng": slot = DayPlan.MealTimeEnum.BREAKFAST; break;
                    case "trưa": slot = DayPlan.MealTimeEnum.LUNCH; break;
                    case "tối": slot = DayPlan.MealTimeEnum.DINNER; break;
                    default: slot = DayPlan.MealTimeEnum.SNACK; break;
                }

                java.util.List<MealRecipe> mealRecipes = mealRecipeDao.getRecipesForMealTime(tEnt.getMealTimeID());

                for (MealRecipe mr : mealRecipes) {
                    long rid = mr.getRecipeID();
                    RecipeEntity re = recipeDao.getById(rid);
                    RecipeDetail det = recipeDetailDao.get(rid);

                    double cal=0, carb=0, pro=0, fat=0;
                    if(det!=null){
                        cal = det.getCalo();
                        carb = det.getCarbs();
                        pro = det.getProtein();
                        fat = det.getFat();
                    }
                    int imgRes = com.example.bepnhataapp.R.drawable.placeholder_banner_background;
                    String thumb = re != null ? re.getImageThumb() : null;
                    if (thumb != null && !thumb.startsWith("http")) {
                        int resId = ctx.getResources().getIdentifier(thumb.trim(), "drawable", ctx.getPackageName());
                        if (resId != 0) imgRes = resId;
                    }
                    
                    Meal meal;
                    if (thumb != null && thumb.startsWith("http")) {
                        meal = new Meal((int)rid, re!=null? re.getRecipeName():"Món ăn", (int)cal, imgRes, thumb, carb, pro, fat);
                    } else {
                        meal = new Meal((int)rid, re!=null? re.getRecipeName():"Món ăn", (int)cal, imgRes, null, carb, pro, fat);
                    }
                    meals.add(new Pair<>(slot, meal));
                }

                if (mealRecipes.isEmpty() && tEnt.getNote() != null && !tEnt.getNote().isEmpty()) {
                    Meal meal = new Meal(0, tEnt.getNote(), 0, com.example.bepnhataapp.R.drawable.placeholder_banner_background);
                    meals.add(new Pair<>(slot, meal));
                }
            }

            // After processing all mealTimes, make sure we have Breakfast, Lunch, Dinner
            // Ensure all three main meal slots exist, adding placeholders if missing
            for (DayPlan.MealTimeEnum reqSlot : new DayPlan.MealTimeEnum[]{DayPlan.MealTimeEnum.BREAKFAST, DayPlan.MealTimeEnum.LUNCH, DayPlan.MealTimeEnum.DINNER}) {
                boolean found = false;
                for (Pair<DayPlan.MealTimeEnum, Meal> p : meals) {
                    if (p.first == reqSlot) { found = true; break; }
                }
                if (!found) {
                    Meal placeholder = new Meal(0, "Chưa có món", 0, com.example.bepnhataapp.R.drawable.placeholder_banner_background);
                    meals.add(new Pair<>(reqSlot, placeholder));
                }
            }
            // Sort meals by slot order
            java.util.Collections.sort(meals, (a, b) -> Integer.compare(a.first.ordinal(), b.first.ordinal()));

            java.time.LocalDate date;
            try {
                date = java.time.LocalDate.parse(dEnt.getDate());
            } catch (Exception e) {
                date = java.time.LocalDate.now();
            }

            dayPlans.add(new DayPlan(date, meals));
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
        return new com.example.bepnhataapp.common.model.WeekPlan(monday, dayPlans);
    }

    @Override
    public WeekPlan generateWeekPlan(LocalDate start) {
        MealPlan mp = null;
        // If user is logged in, use or create PERSONAL plan
        if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.model.Customer customer =
                new com.example.bepnhataapp.common.dao.CustomerDao(ctx)
                    .findByPhone(com.example.bepnhataapp.common.utils.SessionManager.getPhone(ctx));
            if (customer != null) {
                mp = dao.getPersonalPlan(customer.getCustomerID());
                if (mp == null) {
                    mp = new MealPlan();
                    mp.setCustomerID(customer.getCustomerID());
                    mp.setMealCategory("Cá nhân");
                    mp.setTitle("Kế hoạch cá nhân");
                    mp.setCreatedAt(java.time.LocalDateTime.now().toString());
                    mp.setTotalDays(0);
                    mp.setType("PERSONAL");
                    long planId = dao.insert(mp);
                    mp.setMealPlanID(planId);
                }
            }
        }
        // If mp still null, use or create AUTO plan
        if (mp == null) {
            java.util.List<MealPlan> autoPlans = dao.getAutoPlans();
            if (autoPlans.isEmpty()) {
                mp = new MealPlan();
                mp.setCustomerID(null);
                mp.setMealCategory("Tự động");
                mp.setTitle("Kế hoạch tự động");
                mp.setCreatedAt(java.time.LocalDateTime.now().toString());
                mp.setTotalDays(0); // will update later
                mp.setType("AUTO");
                long planId = dao.insert(mp);
                mp.setMealPlanID(planId);
            } else {
                mp = autoPlans.get(0);
            }
        }

        // Now generate meals for mp
        RecipeDao rDao = new RecipeDao(ctx);
        java.util.List<RecipeEntity> recipes = rDao.getAllRecipes();
        if (recipes.isEmpty()) return null;
        java.util.Collections.shuffle(recipes);
        java.util.List<RecipeEntity> pick = new java.util.ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            pick.add(recipes.get(i % recipes.size()));
        }

        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao mealRecipeDao = new MealRecipeDao(ctx);

        // Check if a day entry exists for this plan and date
        MealDay day = null;
        java.util.List<MealDay> existingDays = dayDao.getByMealPlan(mp.getMealPlanID());
        for (MealDay d : existingDays) {
            if (d.getDate().equals(start.toString())) {
                day = d;
                break;
            }
        }
        if (day == null) {
            day = new MealDay();
            day.setMealPlanID(mp.getMealPlanID());
            day.setDate(start.toString());
            long dayId = dayDao.insert(day);
            day.setMealDayID(dayId);
        } else {
            // Delete old meals for this day
            java.util.List<MealTime> oldTimes = timeDao.getByMealDay(day.getMealDayID());
            for (MealTime t : oldTimes) {
                mealRecipeDao.deleteByMealTime(t.getMealTimeID());
                timeDao.delete(t.getMealTimeID());
            }
        }

        long dayId = day.getMealDayID();
        String[] slots = {"Sáng","Trưa","Tối"};
        for (int i = 0; i < slots.length; i++) {
            MealTime mt = new MealTime();
            mt.setMealDayID(dayId);
            mt.setMealType(slots[i]);
            mt.setNote("");
            long mealTimeId = timeDao.insert(mt);
            MealRecipe mr = new MealRecipe(mealTimeId, pick.get(i).getRecipeID());
            mealRecipeDao.insert(mr);
        }

        // Update plan totals
        mp.setTotalDays(dayDao.getByMealPlan(mp.getMealPlanID()).size());
        dao.update(mp);

        return getWeekPlan();
    }

    @Override
    public WeekPlan generateEmptyWeekPlan(LocalDate start) {
        // TODO: Create empty plan structure
        return null;
    }

    @Override
    public void clear() {
        // TODO: Implement clearing logic
    }

    @Override
    public void deletePlanForDate(LocalDate date) {
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao mealRecipeDao = new MealRecipeDao(ctx);

        String dateStr = date.toString();
        // Delete all days matching this date across all plans (auto and personal)
        java.util.List<MealDay> days = dayDao.getAllByDate(dateStr);
        for (MealDay day : days) {
            long dayId = day.getMealDayID();
            java.util.List<MealTime> times = timeDao.getByMealDay(dayId);
            for (MealTime t : times) {
                // Delete all recipe associations for this meal time
                mealRecipeDao.deleteByMealTime(t.getMealTimeID());
                // Delete the meal time itself
                timeDao.delete(t.getMealTimeID());
            }
            // Finally, delete the day record itself
            dayDao.delete(dayId);
        }
    }
} 