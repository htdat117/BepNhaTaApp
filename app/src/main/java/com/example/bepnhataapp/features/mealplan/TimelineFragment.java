package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.common.adapter.MealTimeAdapter;
import java.util.Arrays;
import java.util.List;
import androidx.lifecycle.ViewModelProvider;
import java.time.LocalDate;
import com.example.bepnhataapp.common.model.DayPlan;
import com.example.bepnhataapp.common.model.Meal;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.model.RecipeDetail;

public class TimelineFragment extends Fragment {

    private MealTimeAdapter.OnMealSectionActionListener sectionListener;
    private MealTimeAdapter currentAdapter;
    private RecyclerView rvTimeline;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_timeline, container, false);

        rvTimeline = view.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));

        MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        // create adapter placeholder (empty list first)
        sectionListener = new MealTimeAdapter.OnMealSectionActionListener() {
            @Override
            public void onAddNew(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot) {
                startActivity(new android.content.Intent(getContext(), com.example.bepnhataapp.features.recipes.RecipesActivity.class));
            }

            @Override
            public void onDeleteSection(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot) {
                MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                vm.deleteMealTime(dt, slotToString(slot));
            }

            @Override
            public void onAddNote(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot) {
                android.widget.EditText input = new android.widget.EditText(getContext());
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Thêm ghi chú")
                        .setView(input)
                        .setPositiveButton("Lưu", (d, which) -> {
                            String note = input.getText().toString().trim();
                            MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                            java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                            vm.updateNoteForMealTime(dt, slotToString(slot), note);
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }

            @Override
            public void onBuyAll(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot) {
                MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                vm.addIngredientsToCart(dt, slotToString(slot), getContext());
                android.widget.Toast.makeText(getContext(), "Đã thêm vào giỏ hàng", android.widget.Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSetReminder(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                new android.app.TimePickerDialog(getContext(), (tp, hour, minute) -> {
                    cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
                    cal.set(java.util.Calendar.MINUTE, minute);
                    cal.set(java.util.Calendar.SECOND, 0);

                    long triggerAt = cal.getTimeInMillis();
                    android.app.AlarmManager am = (android.app.AlarmManager) getContext().getSystemService(android.content.Context.ALARM_SERVICE);
                    android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.common.utils.ReminderReceiver.class);
                    intent.putExtra("meal_slot", slotToString(slot));
                    int reqCode = slot.ordinal();
                    android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(getContext(), reqCode, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);
                    if(am!=null){
                        am.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAt, pi);
                        android.widget.Toast.makeText(getContext(), "Đã đặt nhắc nhở", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }, cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), true).show();
            }

            private String slotToString(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum s){
                switch (s){
                    case BREAKFAST: return "Sáng";
                    case LUNCH: return "Trưa";
                    case DINNER: return "Tối";
                    default: return "Snack";
                }
            }
        };

        currentAdapter = new MealTimeAdapter(new java.util.ArrayList<>(), sectionListener);
        rvTimeline.setAdapter(currentAdapter);

        // Observe state changes to refresh UI when data updates (e.g., after delete).
        vm.getState().observe(getViewLifecycleOwner(), st -> {
            updateUiWithState(st, view);
        });

        // Initial
        updateUiWithState(vm.getState().getValue(), view);

        return view;
    }

    private void updateUiWithState(MealPlanViewModel.State curState, View root){
        if(root==null || currentAdapter==null) return;

        java.util.List<MealTimeAdapter.MealTimeSection> sections = new java.util.ArrayList<>();
        int totalSections = 0;
        DayPlan day = null;
        if (curState != null && curState.week != null && getActivity() instanceof MealPlanContentActivity) {
            java.time.LocalDate date = ((MealPlanContentActivity) getActivity()).getCurrentDate();
            for (DayPlan d : curState.week.days) {
                if (d.date.equals(date)) { day = d; break; }
            }

            if (day != null) {
                DayPlan.MealTimeEnum[] slots = DayPlan.MealTimeEnum.values();
                for (DayPlan.MealTimeEnum slot : slots) {
                    java.util.List<MealTimeAdapter.RecipeItem> recipes = new java.util.ArrayList<>();
                    int cals = 0;
                    for (android.util.Pair<DayPlan.MealTimeEnum, Meal> p : day.meals) {
                        if (p.first == slot) {
                            Meal m = p.second;
                            if(m.imageUrl!=null){
                                recipes.add(new MealTimeAdapter.RecipeItem(m.imageUrl, m.title, m.calories));
                            } else {
                                recipes.add(new MealTimeAdapter.RecipeItem(m.imageResId, m.title, m.calories));
                            }
                            cals += m.calories;
                        }
                    }
                    // Bỏ qua section nếu chỉ toàn placeholder "Chưa có món"
                    boolean onlyPlaceholder = true;
                    for(MealTimeAdapter.RecipeItem it : recipes){
                        if(it.name != null && !"Chưa có món".equalsIgnoreCase(it.name.trim())){
                            onlyPlaceholder = false; break;
                        }
                    }
                    if (!recipes.isEmpty() && !onlyPlaceholder) {
                        String title;
                        switch (slot) {
                            case BREAKFAST: title = "Bữa sáng"; break;
                            case LUNCH: title = "Bữa trưa"; break;
                            case DINNER: title = "Bữa tối"; break;
                            default: title = "Ăn nhẹ"; break;
                        }
                        sections.add(new MealTimeAdapter.MealTimeSection(title, cals, recipes, slot));
                    }
                }
            }
            totalSections = sections.size();
        }

        // Cập nhật RecyclerView bằng adapter mới để đảm bảo hiển thị chính xác
        if(rvTimeline != null){
            currentAdapter = new MealTimeAdapter(sections, sectionListener);
            rvTimeline.setAdapter(currentAdapter);
            currentAdapter.notifyDataSetChanged();
        }

        // Update stats UI
        ((android.widget.TextView) root.findViewById(R.id.tvMealCount)).setText("Theo dõi 0/" + (totalSections==0? com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum.values().length : totalSections) + " bữa ăn");

        double totCals=0, totCarb=0, totPro=0, totFat=0;
        if(day != null) {
            for(android.util.Pair<com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum, Meal> p: day.meals){
                totCals += p.second.calories;
                totCarb += p.second.carbs;
                totPro += p.second.protein;
                totFat += p.second.fat;
            }
        }
        android.widget.TextView tvCal=root.findViewById(R.id.tvCalories);
        android.widget.TextView tvMacros=root.findViewById(R.id.tvMacros);
        if(tvCal!=null) tvCal.setText((int)totCals+" Calories");
        if(tvMacros!=null) tvMacros.setText((int)totCarb+"g Carbs, "+(int)totFat+"g Fat, "+(int)totPro+"g Protein");
    }
} 
