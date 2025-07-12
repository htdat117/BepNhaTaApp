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
} 
