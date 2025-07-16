package com.example.bepnhataapp.common.repository;

import com.example.bepnhataapp.common.model.WeekPlan;
import java.time.LocalDate;

public interface MealPlanRepository {
    boolean hasPlan();
    WeekPlan getWeekPlan();
    WeekPlan generateWeekPlan(LocalDate start);
    WeekPlan generateEmptyWeekPlan(LocalDate start);
    void deletePlanForDate(LocalDate date);
    /**
     * Sao chép thực đơn của ngày <code>date.minusDays(1)</code> sang ngày hiện tại.
     *
     * @param date Ngày đích cần sao chép thực đơn.
     * @return <code>true</code> nếu có thực đơn của ngày trước đó và thao tác sao chép đã được thực hiện,
     *         <code>false</code> nếu không tìm thấy dữ liệu để sao chép (trong trường hợp này có thể chỉ tạo ngày trống hoặc không làm gì).
     */
    boolean copyFromPreviousDay(LocalDate date);
    void clear();

    /**
     * Xoá toàn bộ món ăn trong một buổi (meal time) của ngày cho trước.
     * @param date Ngày cần xoá.
     * @param mealType Chuỗi mealType ("Sáng", "Trưa", "Tối", "Snack").
     */
    void deleteMealTime(LocalDate date, String mealType);

    /**
     * Cập nhật ghi chú cho một buổi.
     */
    void updateNoteForMealTime(LocalDate date, String mealType, String note);

    /**
     * Thêm toàn bộ nguyên liệu của các món trong buổi đó vào giỏ hàng.
     */
    void addIngredientsToCart(LocalDate date, String mealType, android.content.Context ctx);

    /**
     * Xoá một công thức ra khỏi một buổi của ngày.
     * @param date Ngày cần xoá.
     * @param mealType "Sáng", "Trưa", "Tối", "Snack".
     * @param recipeId ID của công thức cần xoá.
     */
    void deleteRecipe(LocalDate date, String mealType, long recipeId);

    /**
     * Thay thế 1 công thức trong buổi bằng công thức khác (random).
     * @return ID công thức mới hoặc -1 nếu không thay.
     */
    long changeRecipe(LocalDate date, String mealType, long recipeId);

    /** Di chuyển một công thức sang NGÀY khác, giữ nguyên mealType */
    void moveRecipeToDate(LocalDate curDate, String mealType, long recipeId, LocalDate targetDate);

    /** Di chuyển công thức sang buổi khác trong cùng ngày */
    void moveRecipeToMeal(LocalDate date, String fromMealType, long recipeId, String targetMealType);

    /** Thêm nguyên liệu của 1 công thức vào giỏ hàng */
    void addIngredientsForRecipe(long recipeId, android.content.Context ctx);
} 
