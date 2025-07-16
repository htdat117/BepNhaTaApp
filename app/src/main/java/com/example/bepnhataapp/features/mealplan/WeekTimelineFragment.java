package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.MealAdapter;
import com.example.bepnhataapp.common.adapter.MealTimeListAdapter;
import com.example.bepnhataapp.common.adapter.WeekAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeekTimelineFragment extends Fragment {

    public static WeekTimelineFragment newInstance() {
        return new WeekTimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_week_timeline, container, false);
        RecyclerView rv = root.findViewById(R.id.rvWeek);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        WeekAdapter adapter = new WeekAdapter(buildWeekTimelineData());
        rv.setAdapter(adapter);
        return root;
    }

    private List<WeekAdapter.DayTimeline> buildWeekTimelineData(){
        java.util.List<WeekAdapter.DayTimeline> week = new java.util.ArrayList<>();

        MealPlanViewModel vm = new androidx.lifecycle.ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
        MealPlanViewModel.State state = vm.getState().getValue();
        if(state==null || state.week==null) return week;

        // Build map date->DayPlan for quick lookup
        java.util.Map<java.time.LocalDate, com.example.bepnhataapp.common.model.DayPlan> map = new java.util.HashMap<>();
        for(com.example.bepnhataapp.common.model.DayPlan d : state.week.days){
            map.put(d.date, d);
        }

        // Determine the Monday that corresponds to the date currently being viewed in the parent
        // activity instead of relying on the startDate saved inside the WeekPlan (which may always
        // be the week that contained the data when the ViewModel was first created).
        java.time.LocalDate monday;
        try {
            MealPlanContentActivity act = (MealPlanContentActivity) requireActivity();
            monday = act.getCurrentDate().with(java.time.DayOfWeek.MONDAY);
        } catch (Exception e) {
            // Fallback – current week of today if activity not yet attached or cast failed
            monday = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        }

        for(int offset=0; offset<7; offset++){
            java.time.LocalDate cur = monday.plusDays(offset);
            com.example.bepnhataapp.common.model.DayPlan d = map.get(cur);
            java.util.List<com.example.bepnhataapp.common.adapter.MealTimeListAdapter.MealTimeWithMeals> mealTimes = new java.util.ArrayList<>();

            if(d != null){
                // build mealTime slots order breakfast lunch dinner snack
                for(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot : com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum.values()){
                    java.util.List<com.example.bepnhataapp.common.adapter.MealAdapter.MealRow> rows = new java.util.ArrayList<>();
                    int slotCal = 0;
                    for(android.util.Pair<com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum, com.example.bepnhataapp.common.model.Meal> p : d.meals){
                        if(p.first == slot){
                            com.example.bepnhataapp.common.model.Meal m = p.second;
                            slotCal += m.calories;
                            if(m.imageUrl!=null && !m.imageUrl.isEmpty()){
                                rows.add(new com.example.bepnhataapp.common.adapter.MealAdapter.MealRow(m.title, m.imageUrl));
                            } else {
                                int res = m.imageResId!=0 ? m.imageResId : com.example.bepnhataapp.R.drawable.placeholder_banner_background;
                                rows.add(new com.example.bepnhataapp.common.adapter.MealAdapter.MealRow(m.title, res));
                            }
                        }
                    }
                    if(!rows.isEmpty()){
                        String lbl;
                        switch(slot){
                            case BREAKFAST: lbl = "Bữa sáng"; break;
                            case LUNCH: lbl = "Bữa trưa"; break;
                            case DINNER: lbl = "Bữa tối"; break;
                            default: lbl = "Ăn nhẹ"; break;
                        }
                        if(slotCal>0){ lbl += " - "+slotCal+" Kcal"; }
                        mealTimes.add(new com.example.bepnhataapp.common.adapter.MealTimeListAdapter.MealTimeWithMeals(lbl, rows));
                    }
                }
            }

            java.util.Date dat;
            try{
                dat = java.sql.Date.valueOf(cur.toString());
            }catch(Exception e){
                dat = new java.util.Date();
            }
            week.add(new com.example.bepnhataapp.common.adapter.WeekAdapter.DayTimeline(dat, mealTimes));
        }

        return week;
    }
} 
