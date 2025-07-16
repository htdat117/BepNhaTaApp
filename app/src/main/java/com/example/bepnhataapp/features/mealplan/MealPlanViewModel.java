package com.example.bepnhataapp.features.mealplan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bepnhataapp.common.repository.MealPlanRepository;
import com.example.bepnhataapp.common.model.WeekPlan;

import java.time.LocalDate;

public class MealPlanViewModel extends ViewModel {

    public static class State {
        public final WeekPlan week;
        public State(WeekPlan week) { this.week = week; }
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();
    private final MealPlanRepository repository;

    public MealPlanViewModel(MealPlanRepository repository) {
        this.repository = repository;
        refresh();
    }

    public LiveData<State> getState() { return state; }

    public void refresh() {
        State newState = new State(repository.hasPlan() ? repository.getWeekPlan() : null);
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            // On main thread – safe to call setValue
            state.setValue(newState);
        } else {
            // Background thread – must use postValue to avoid IllegalStateException
            state.postValue(newState);
        }
    }

    public void autoGenerate() {
        autoGenerateFor(LocalDate.now());
    }

    public void autoGenerateFor(java.time.LocalDate date){
        repository.generateWeekPlan(date);
        refresh();
    }

    /**
     * Regenerate meals only for the days that already have a thực đơn (MealDay) inside
     * the week that contains {@code referenceDate}.  Empty days will be kept nguyên trạng
     * (không sinh tự động).
     */
    public void autoGenerateWeekFor(java.time.LocalDate referenceDate){
        // Fetch current week plan to know which dates already exist
        com.example.bepnhataapp.common.model.WeekPlan wp = repository.getWeekPlan();
        if(wp == null || wp.days == null || wp.days.isEmpty()){
            return; // nothing to refresh
        }

        java.time.LocalDate monday = referenceDate.with(java.time.DayOfWeek.MONDAY);
        java.time.LocalDate sunday = monday.plusDays(6);

        java.util.Set<java.time.LocalDate> datesToRefresh = new java.util.HashSet<>();
        for(com.example.bepnhataapp.common.model.DayPlan d : wp.days){
            if(!d.date.isBefore(monday) && !d.date.isAfter(sunday)){
                datesToRefresh.add(d.date);
            }
        }

        for(java.time.LocalDate d : datesToRefresh){
            repository.generateWeekPlan(d);
        }
        refresh();
    }

    public void generateEmpty() {
        repository.generateEmptyWeekPlan(LocalDate.now());
        refresh();
    }

    public void deletePlanForDate(LocalDate date) {
        repository.deletePlanForDate(date);
        refresh();
    }

    /**
     * Sao chép thực đơn ngày trước đó sang ngày <code>date</code>.
     * @return {@code true} nếu có dữ liệu được sao chép, {@code false} nếu không sao chép được.
     */
    public boolean copyFromPreviousDay(LocalDate date){
        boolean copied = repository.copyFromPreviousDay(date);
        refresh();
        return copied;
    }

    public void deleteMealTime(LocalDate date, String mealType){
        repository.deleteMealTime(date, mealType);
        refresh();
    }

    public void updateNoteForMealTime(LocalDate date, String mealType, String note){
        repository.updateNoteForMealTime(date, mealType, note);
        refresh();
    }

    public void addIngredientsToCart(LocalDate date, String mealType, android.content.Context ctx){
        repository.addIngredientsToCart(date, mealType, ctx);
    }

    public void deleteRecipe(LocalDate date, String mealType, long recipeId){
        repository.deleteRecipe(date, mealType, recipeId);
        refresh();
    }

    public void changeRecipe(LocalDate date, String mealType, long recipeId){
        repository.changeRecipe(date, mealType, recipeId);
        refresh();
    }

    public void moveRecipeToDate(LocalDate curDate, String mealType, long recipeId, LocalDate targetDate){
        repository.moveRecipeToDate(curDate, mealType, recipeId, targetDate);
        refresh();
    }

    public void moveRecipeToMeal(LocalDate date, String fromMealType, long recipeId, String targetMealType){
        repository.moveRecipeToMeal(date, fromMealType, recipeId, targetMealType);
        refresh();
    }

    public void addIngredientsForRecipe(long recipeId, android.content.Context ctx){
        repository.addIngredientsForRecipe(recipeId, ctx);
    }
} 
