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
import com.example.bepnhataapp.common.utils.SessionManager;

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
        if (SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.model.Customer customer = new com.example.bepnhataapp.common.dao.CustomerDao(ctx).findByPhone(SessionManager.getPhone(ctx));
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
        if (SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.model.Customer customer =
                new com.example.bepnhataapp.common.dao.CustomerDao(ctx)
                    .findByPhone(SessionManager.getPhone(ctx));
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
        MealRecipeDao recDao = new MealRecipeDao(ctx);

        java.util.List<MealDay> days = dayDao.getAllByDate(date.toString());
        for(MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for (com.example.bepnhataapp.common.model.MealTime t : times) {
                recDao.deleteByMealTime(t.getMealTimeID());
                timeDao.delete(t.getMealTimeID());
            }
            dayDao.delete(d.getMealDayID());
        }
    }

    @Override
    public boolean copyFromPreviousDay(LocalDate date) {
        LocalDate prev = date.minusDays(1);

        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);
        MealPlanDao mpDao = new MealPlanDao(ctx);

        // ---------------------------
        // 1. Xác định kế hoạch ĐÍCH (targetPlan)
        //    – Nếu đã đăng nhập ➜ PERSONAL
        //    – Ngược lại ➜ AUTO
        //    – Tạo mới nếu chưa tồn tại
        // ---------------------------
        MealPlan targetPlan = null;
        if (SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.model.Customer cust = new com.example.bepnhataapp.common.dao.CustomerDao(ctx)
                    .findByPhone(SessionManager.getPhone(ctx));
            if (cust != null) {
                targetPlan = mpDao.getPersonalPlan(cust.getCustomerID());
                if (targetPlan == null) {
                    targetPlan = new MealPlan();
                    targetPlan.setCustomerID(cust.getCustomerID());
                    targetPlan.setType("PERSONAL");
                    targetPlan.setTitle("Kế hoạch của tôi");
                    targetPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
                    long id = mpDao.insert(targetPlan);
                    targetPlan.setMealPlanID(id);
                }
            }
        }
        if (targetPlan == null) {
            java.util.List<MealPlan> autos = mpDao.getAutoPlans();
            if (autos.isEmpty()) {
                targetPlan = new MealPlan();
                targetPlan.setType("AUTO");
                targetPlan.setTitle("Kế hoạch tự động");
                targetPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
                long id = mpDao.insert(targetPlan);
                targetPlan.setMealPlanID(id);
            } else {
                targetPlan = autos.get(0);
            }
        }

        long targetPlanId = targetPlan.getMealPlanID();

        // ---------------------------
        // 2. Tìm ngày NGUỒN (prevDay)
        //    – Ưu tiên PERSONAL của ngày hôm qua (nếu tồn tại)
        //    – Sau đó tới AUTO
        // ---------------------------
        MealDay prevDay = null;
        if (SessionManager.isLoggedIn(ctx)) {
            // Tìm trong PERSONAL trước
            MealDay p = dayDao.getByDate(prev.toString());
            if (p != null) {
                // đảm bảo p thuộc plan cá nhân
                MealPlan pp = null;
                // Simple lookup: fetch plan by ID from cached list to avoid new DAO method
                java.util.List<MealPlan> _allPlans = mpDao.getAll();
                for(MealPlan _pl : _allPlans){
                    if(_pl.getMealPlanID() == p.getMealPlanID()){ pp = _pl; break; }
                }
                if (pp != null && "PERSONAL".equalsIgnoreCase(String.valueOf(pp.getType()))) {
                    prevDay = p;
                }
            }
        }
        if (prevDay == null) {
            // Tìm trong AUTO
            prevDay = dayDao.getByDate(prev.toString());
        }

        // Nếu không tìm thấy ngày nguồn ➜ chỉ tạo ngày trống cho targetPlan
        if (prevDay == null) {
            // Xoá bản ghi cũ (nếu có) của targetPlan cho ngày hiện tại
            MealDay existTargetDay = null;
            java.util.List<MealDay> allCur = dayDao.getAllByDate(date.toString());
            for (MealDay d : allCur) {
                if (d.getMealPlanID() == targetPlanId) { existTargetDay = d; break; }
            }
            if (existTargetDay != null) {
                java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(existTargetDay.getMealDayID());
                for (com.example.bepnhataapp.common.model.MealTime t : times) {
                    recDao.deleteByMealTime(t.getMealTimeID());
                    timeDao.delete(t.getMealTimeID());
                }
                dayDao.delete(existTargetDay.getMealDayID());
            }

            // Tạo ngày trống
            MealDay emptyDay = new MealDay(targetPlanId, date.toString(), null);
            dayDao.insert(emptyDay);
            return false;
        }

        // ---------------------------
        // 3. Xoá ngày hiện tại (nếu đã tồn tại) trong targetPlan để tránh trùng lặp
        // ---------------------------
        MealDay existingTarget = null;
        java.util.List<MealDay> curList = dayDao.getAllByDate(date.toString());
        for (MealDay d : curList) {
            if (d.getMealPlanID() == targetPlanId) { existingTarget = d; break; }
        }
        if (existingTarget != null) {
            java.util.List<com.example.bepnhataapp.common.model.MealTime> curTimes = timeDao.getByMealDay(existingTarget.getMealDayID());
            for (com.example.bepnhataapp.common.model.MealTime t : curTimes) {
                recDao.deleteByMealTime(t.getMealTimeID());
                timeDao.delete(t.getMealTimeID());
            }
            dayDao.delete(existingTarget.getMealDayID());
        }

        // ---------------------------
        // 4. Sao chép dữ liệu từ prevDay → targetPlan cho ngày hiện tại
        // ---------------------------
        MealDay newDay = new MealDay(targetPlanId, date.toString(), prevDay.getNote());
        long newDayId = dayDao.insert(newDay);

        java.util.List<com.example.bepnhataapp.common.model.MealTime> prevTimes = timeDao.getByMealDay(prevDay.getMealDayID());
        for (com.example.bepnhataapp.common.model.MealTime pt : prevTimes) {
            MealTime newT = new MealTime(newDayId, pt.getMealType(), pt.getNote());
            long newTId = timeDao.insert(newT);

            java.util.List<com.example.bepnhataapp.common.model.MealRecipe> prevRecipes = recDao.getRecipesForMealTime(pt.getMealTimeID());
            for (com.example.bepnhataapp.common.model.MealRecipe pr : prevRecipes) {
                recDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(newTId, pr.getRecipeID()));
            }
        }

        // Cập nhật totalDays cho plan đích
        targetPlan.setTotalDays(dayDao.getByMealPlan(targetPlanId).size());
        mpDao.update(targetPlan);

        return true;
    }

    @Override
    public void deleteMealTime(java.time.LocalDate date, String mealType) {
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);

        // Lặp qua TẤT CẢ MealDay của ngày (có thể thuộc nhiều MealPlan khác nhau)
        java.util.List<MealDay> days = dayDao.getAllByDate(date.toString());
        for(MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t: times){
                if(mealType.equalsIgnoreCase(t.getMealType())){
                    // Xoá tất cả công thức trong MealTime trước
                    recDao.deleteByMealTime(t.getMealTimeID());
                    // Sau đó xoá MealTime
                    timeDao.delete(t.getMealTimeID());
                }
            }

            // Kiểm tra nếu MealDay này không còn MealTime nào → xoá luôn MealDay để giữ DB sạch
            if(timeDao.getByMealDay(d.getMealDayID()).isEmpty()){
                dayDao.delete(d.getMealDayID());
            }
        }
    }

    @Override
    public void updateNoteForMealTime(java.time.LocalDate date, String mealType, String note){
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);

        MealDay day = dayDao.getByDate(date.toString());
        if(day == null) return;

        java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(day.getMealDayID());
        for(com.example.bepnhataapp.common.model.MealTime t: times){
            if(mealType.equalsIgnoreCase(t.getMealType())){
                t.setNote(note);
                timeDao.update(t);
                break;
            }
        }
    }

    @Override
    public void addIngredientsToCart(java.time.LocalDate date, String mealType, android.content.Context context){
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);
        com.example.bepnhataapp.common.dao.ProductDetailDao pdDao = new com.example.bepnhataapp.common.dao.ProductDetailDao(ctx);
        com.example.bepnhataapp.common.dao.ProductDao productDao = new com.example.bepnhataapp.common.dao.ProductDao(ctx);

        // Lấy tất cả MealDay của ngày (phòng trường hợp meal nằm ở nhiều kế hoạch)
        java.util.List<MealDay> days = dayDao.getAllByDate(date.toString());
        java.util.Set<Long> addedProductIds = new java.util.HashSet<>(); // tránh thêm trùng cùng product

        for(MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t: times){
                if(!mealType.equalsIgnoreCase(t.getMealType())) continue;

                java.util.List<com.example.bepnhataapp.common.model.MealRecipe> recipes = recDao.getRecipesForMealTime(t.getMealTimeID());
                for(com.example.bepnhataapp.common.model.MealRecipe mr: recipes){
                    // Tìm Product gắn với recipe này
                    com.example.bepnhataapp.common.model.ProductDetail pd = pdDao.getByRecipeId(mr.getRecipeID());
                    if(pd == null) continue; // bỏ qua nếu chưa mapping

                    com.example.bepnhataapp.common.model.Product product = productDao.getById(pd.getProductID());
                    if(product == null) continue;

                    // Đảm bảo không thêm trùng sản phẩm
                    if(addedProductIds.contains(product.getProductID())) continue;

                    com.example.bepnhataapp.common.utils.CartHelper.addProduct(context, product);
                    addedProductIds.add(product.getProductID());
                }
            }
        }
    }

    @Override
    public void deleteRecipe(java.time.LocalDate date, String mealType, long recipeId){
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);

        java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dayDao.getAllByDate(date.toString());
        for(com.example.bepnhataapp.common.model.MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t : times){
                if(!mealType.equalsIgnoreCase(t.getMealType())) continue;

                // delete mapping
                java.util.List<com.example.bepnhataapp.common.model.MealRecipe> recipes = recDao.getRecipesForMealTime(t.getMealTimeID());
                for(com.example.bepnhataapp.common.model.MealRecipe mr : recipes){
                    if(mr.getRecipeID() == recipeId){
                        ctx.getApplicationContext(); // no op just to use ctx reference avoid warnings
                        // direct delete by executing SQL
                        helperDeleteMealRecipe(recDao, t.getMealTimeID(), recipeId);
                        break;
                    }
                }

                // Giữ lại MealTime rỗng để slot vẫn hiển thị dưới dạng trống
            }

            // Nếu MealDay rỗng -> xoá
            boolean stillHas = false;
            for(com.example.bepnhataapp.common.model.MealTime t : timeDao.getByMealDay(d.getMealDayID())){
                if(!recDao.getRecipesForMealTime(t.getMealTimeID()).isEmpty()) { stillHas = true; break; }
            }
            if(!stillHas){
                dayDao.delete(d.getMealDayID());
            }
        }
    }

    // Helper to delete single recipe mapping
    private void helperDeleteMealRecipe(MealRecipeDao recDao, long mealTimeId, long recipeId){
        ctx.getApplicationContext();
        android.database.sqlite.SQLiteDatabase db = new com.example.bepnhataapp.common.databases.DBHelper(ctx).getWritableDatabase();
        db.delete(com.example.bepnhataapp.common.databases.DBHelper.TBL_MEAL_RECIPES, "mealTimeID=? AND recipeID=?", new String[]{String.valueOf(mealTimeId), String.valueOf(recipeId)});
    }

    @Override
    public long changeRecipe(java.time.LocalDate date, String mealType, long recipeId){
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);
        com.example.bepnhataapp.common.dao.RecipeDao recipeDao = new com.example.bepnhataapp.common.dao.RecipeDao(ctx);

        java.util.List<com.example.bepnhataapp.common.model.RecipeEntity> all = recipeDao.getAllRecipes();
        if(all.isEmpty()) return -1;

        java.util.Random rnd = new java.util.Random();
        long newId = recipeId;
        int tries = 0;
        while(newId == recipeId && tries < 10){
            int idx = rnd.nextInt(all.size());
            newId = all.get(idx).getRecipeID();
            tries++;
        }
        if(newId == recipeId) return -1; // couldn't find different

        java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dayDao.getAllByDate(date.toString());
        for(com.example.bepnhataapp.common.model.MealDay d : days){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t: times){
                if(!mealType.equalsIgnoreCase(t.getMealType())) continue;

                // tìm mapping cũ
                java.util.List<com.example.bepnhataapp.common.model.MealRecipe> recipes = recDao.getRecipesForMealTime(t.getMealTimeID());
                for(com.example.bepnhataapp.common.model.MealRecipe mr: recipes){
                    if(mr.getRecipeID()==recipeId){
                        // xoá cũ
                        helperDeleteMealRecipe(recDao, t.getMealTimeID(), recipeId);
                        // thêm mới
                        recDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(t.getMealTimeID(), newId));
                        break;
                    }
                }
            }
        }
        return newId;
    }

    @Override
    public void moveRecipeToDate(java.time.LocalDate curDate, String mealType, long recipeId, java.time.LocalDate targetDate){
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);

        // B1: Tìm MealTime nguồn và MealPlanID
        com.example.bepnhataapp.common.model.MealTime sourceTime = null;
        long planId = -1;
        java.util.List<com.example.bepnhataapp.common.model.MealDay> curDays = dayDao.getAllByDate(curDate.toString());
        for(com.example.bepnhataapp.common.model.MealDay d : curDays){
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t: times){
                if(mealType.equalsIgnoreCase(t.getMealType())){
                    for(com.example.bepnhataapp.common.model.MealRecipe mr: recDao.getRecipesForMealTime(t.getMealTimeID())){
                        if(mr.getRecipeID()==recipeId){
                            sourceTime = t;
                            planId = d.getMealPlanID();
                            helperDeleteMealRecipe(recDao, t.getMealTimeID(), recipeId);
                            break;
                        }
                    }
                }
            }
        }
        if(sourceTime == null) return;

        // B2: Tạo hoặc lấy MealDay đích
        com.example.bepnhataapp.common.model.MealDay targetDay = null;
        java.util.List<com.example.bepnhataapp.common.model.MealDay> targetDays = dayDao.getAllByDate(targetDate.toString());
        for(com.example.bepnhataapp.common.model.MealDay d: targetDays){
            if(d.getMealPlanID()==planId){ targetDay = d; break; }
        }
        if(targetDay==null){
            targetDay = new com.example.bepnhataapp.common.model.MealDay(planId, targetDate.toString(), null);
            long id = dayDao.insert(targetDay);
            targetDay.setMealDayID(id);
        }

        // B3: Tạo hoặc lấy MealTime đích
        com.example.bepnhataapp.common.model.MealTime targetTime = null;
        for(com.example.bepnhataapp.common.model.MealTime t: timeDao.getByMealDay(targetDay.getMealDayID())){
            if(mealType.equalsIgnoreCase(t.getMealType())){ targetTime = t; break; }
        }
        if(targetTime==null){
            targetTime = new com.example.bepnhataapp.common.model.MealTime(targetDay.getMealDayID(), mealType, null);
            long tid = timeDao.insert(targetTime);
            targetTime.setMealTimeID(tid);
        }

        // B4: Insert mapping mới
        recDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(targetTime.getMealTimeID(), recipeId));
    }

    @Override
    public void moveRecipeToMeal(java.time.LocalDate date, String fromMealType, long recipeId, String targetMealType){
        if(fromMealType.equalsIgnoreCase(targetMealType)) return;
        MealDayDao dayDao = new MealDayDao(ctx);
        MealTimeDao timeDao = new MealTimeDao(ctx);
        MealRecipeDao recDao = new MealRecipeDao(ctx);

        // Lấy tất cả MealDay ngày
        java.util.List<com.example.bepnhataapp.common.model.MealDay> days = dayDao.getAllByDate(date.toString());
        for(com.example.bepnhataapp.common.model.MealDay d: days){
            com.example.bepnhataapp.common.model.MealTime fromTime = null;
            com.example.bepnhataapp.common.model.MealTime toTime = null;
            java.util.List<com.example.bepnhataapp.common.model.MealTime> times = timeDao.getByMealDay(d.getMealDayID());
            for(com.example.bepnhataapp.common.model.MealTime t: times){
                if(fromMealType.equalsIgnoreCase(t.getMealType())) fromTime = t;
                if(targetMealType.equalsIgnoreCase(t.getMealType())) toTime = t;
            }
            if(fromTime==null) continue;

            if(toTime==null){
                toTime = new com.example.bepnhataapp.common.model.MealTime(d.getMealDayID(), targetMealType, null);
                long tid = timeDao.insert(toTime);
                toTime.setMealTimeID(tid);
            }

            // Xoá khỏi from
            helperDeleteMealRecipe(recDao, fromTime.getMealTimeID(), recipeId);

            // Thêm vào to
            recDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(toTime.getMealTimeID(), recipeId));
        }
    }

    @Override
    public void addIngredientsForRecipe(long recipeId, android.content.Context ctx){
        com.example.bepnhataapp.common.dao.IngredientDao ingDao = new com.example.bepnhataapp.common.dao.IngredientDao(this.ctx);
        java.util.List<com.example.bepnhataapp.common.model.Ingredient> ings = ingDao.getByRecipe(recipeId);
        for(com.example.bepnhataapp.common.model.Ingredient ing : ings){
            com.example.bepnhataapp.common.model.Product p = new com.example.bepnhataapp.common.model.Product();
            p.setProductID(ing.getIngredientID());
            p.setProductName(ing.getIngredientName());
            p.setProductPrice(0);
            com.example.bepnhataapp.common.utils.CartHelper.addProduct(ctx, p);
        }
    }
} 
