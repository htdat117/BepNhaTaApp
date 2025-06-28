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
        state.setValue(new State(repository.hasPlan() ? repository.getWeekPlan() : null));
    }

    public void autoGenerate() {
        autoGenerateFor(LocalDate.now());
    }

    public void autoGenerateFor(java.time.LocalDate date){
        repository.generateWeekPlan(date);
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
} 