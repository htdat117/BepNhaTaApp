package com.example.bepnhataapp.common.repository;

import java.util.*;

public class SavedPlanRepository {

    public static class PlanInfo {
        public final String title;
        public final String description;
        public final int dayCount;
        public final int avgCal;
        public final int avgCarbs;
        public final int avgFat; // grams
        public final int avgProtein;

        public PlanInfo(String title, String description, int dayCount, int avgCal, int avgCarbs, int avgFat, int avgProtein) {
            this.title = title;
            this.description = description;
            this.dayCount = dayCount;
            this.avgCal = avgCal;
            this.avgCarbs = avgCarbs;
            this.avgFat = avgFat;
            this.avgProtein = avgProtein;
        }
    }

    private static final Map<String, PlanInfo> PLAN_MAP = new HashMap<>();

    static {
        add(new PlanInfo(
                "Thực đơn ăn kiêng",
                "Giảm calo giữ dinh dưỡng, phù hợp giảm cân.",
                7,
                1800,
                150,
                50,
                120));

        add(new PlanInfo(
                "Thực đơn tập gym",
                "Tăng cơ giảm mỡ, giàu protein.",
                7,
                2500,
                200,
                70,
                180));

        add(new PlanInfo(
                "Thực đơn chay",
                "Thực đơn thuần chay thanh đạm.",
                5,
                2000,
                220,
                60,
                100));

        add(new PlanInfo(
                "Thực đơn tăng cơ",
                "Calo cao hỗ trợ tăng cân và cơ bắp.",
                7,
                3000,
                250,
                90,
                200));
    }

    private static void add(PlanInfo info) {
        PLAN_MAP.put(info.title, info);
    }

    public List<String> getPlanNames() {
        return new ArrayList<>(PLAN_MAP.keySet());
    }

    public PlanInfo getPlan(String title) {
        return PLAN_MAP.get(title);
    }
} 