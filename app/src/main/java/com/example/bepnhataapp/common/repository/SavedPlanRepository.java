package com.example.bepnhataapp.common.repository;

import android.content.Context;

import com.example.bepnhataapp.common.dao.MealPlanDao;
import com.example.bepnhataapp.common.model.MealPlan;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.*;

/**
 * Repository trả về danh sách kế hoạch nấu ăn (mealPlan) có <code>type = "DOWNLOADED"</code>.
 * Nếu người dùng đã đăng nhập, bao gồm cả kế hoạch của họ (customerID = current) và kế hoạch chung (customerID NULL).
 * Nếu chưa đăng nhập chỉ lấy kế hoạch chung.
 */
public class SavedPlanRepository {

    public static class PlanInfo {
        public final String title;
        public final String description;
        public final int dayCount;
        public final int avgCal;
        public final int avgCarbs;
        public final int avgFat; // grams
        public final int avgProtein;

        public final long mealPlanId;

        public PlanInfo(long id, String title, String description, int dayCount,
                         int avgCal, int avgCarbs, int avgFat, int avgProtein) {
            this.mealPlanId = id;
            this.title = title;
            this.description = description;
            this.dayCount = dayCount;
            this.avgCal = avgCal;
            this.avgCarbs = avgCarbs;
            this.avgFat = avgFat;
            this.avgProtein = avgProtein;
        }
    }

    private final Map<String, PlanInfo> planMap = new LinkedHashMap<>();
    private final Map<Long, PlanInfo> idMap = new java.util.HashMap<>();

    public SavedPlanRepository(Context ctx) {
        loadFromDatabase(ctx.getApplicationContext());
    }

    private void loadFromDatabase(Context ctx) {
        MealPlanDao dao = new MealPlanDao(ctx);
        List<MealPlan> all = dao.getAll();

        Long currentCustomerId = null;
        if (SessionManager.isLoggedIn(ctx)) {
            com.example.bepnhataapp.common.dao.CustomerDao custDao = new com.example.bepnhataapp.common.dao.CustomerDao(ctx);
            com.example.bepnhataapp.common.model.Customer c = custDao.findByPhone(SessionManager.getPhone(ctx));
            if (c != null) currentCustomerId = c.getCustomerID();
        }

        for (MealPlan mp : all) {
            if (mp.getType() == null || !"DOWNLOADED".equalsIgnoreCase(mp.getType())) continue;

            Long cid = mp.getCustomerID();
            boolean keep;
            if (currentCustomerId == null) {
                keep = cid == null;
            } else {
                keep = (cid == null) || currentCustomerId.equals(cid);
            }

            if (!keep) continue;

            String desc = mp.getNote() != null ? mp.getNote() : (mp.getMealCategory() != null ? mp.getMealCategory() : "");

            PlanInfo info = new PlanInfo(
                    mp.getMealPlanID(),
                    mp.getTitle(),
                    desc,
                    mp.getTotalDays(),
                    (int) mp.getAvgCalories(),
                    (int) mp.getAvgCarbs(),
                    (int) mp.getAvgFat(),
                    (int) mp.getAvgProtein()
            );
            planMap.put(info.title, info);
            idMap.put(info.mealPlanId, info);
        }
    }

    public List<String> getPlanNames() {
        return new ArrayList<>(planMap.keySet());
    }

    public PlanInfo getPlan(String title) {
        return planMap.get(title);
    }

    public PlanInfo getPlan(long id){
        return idMap.get(id);
    }
} 
