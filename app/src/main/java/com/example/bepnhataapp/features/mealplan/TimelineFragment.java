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
            public void onDeleteRecipe(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Xoá món khỏi buổi")
                        .setMessage("Bạn có chắc chắn muốn xoá món này ra khỏi buổi không?")
                        .setPositiveButton("Xoá", (d, w) -> {
                            MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                            java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                            vm.deleteRecipe(dt, slotToString(slot), recipeId);
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }

            @Override
            public void onChangeRecipe(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId) {
                MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                vm.changeRecipe(dt, slotToString(slot), recipeId);
            }

            @Override
            public void onChangeDay(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId) {
                java.time.LocalDate cur = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(cur.getYear(), cur.getMonthValue()-1, cur.getDayOfMonth());
                new android.app.DatePickerDialog(getContext(), (dp, y, m, d) -> {
                    java.time.LocalDate newDate = java.time.LocalDate.of(y, m+1, d);
                    MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                    vm.moveRecipeToDate(cur, slotToString(slot), recipeId, newDate);
                }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }

            @Override
            public void onMoveMeal(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId) {
                String[] options = {"Sáng", "Trưa", "Tối", "Snack"};
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Chọn bữa chuyển đến")
                        .setItems(options, (d, which) -> {
                            String target = options[which];
                            MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                            java.time.LocalDate dt = ((MealPlanContentActivity) requireActivity()).getCurrentDate();
                            vm.moveRecipeToMeal(dt, slotToString(slot), recipeId, target);
                        })
                        .show();
            }

            @Override
            public void onBuyIngredients(long recipeId) {
                MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
                vm.addIngredientsForRecipe(recipeId, getContext());
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
                            if(m.id==0) continue; // skip placeholder mục trống
                            if(m.imageUrl!=null){
                                recipes.add(new MealTimeAdapter.RecipeItem(m.id, m.imageUrl, m.title, m.calories));
                            } else {
                                recipes.add(new MealTimeAdapter.RecipeItem(m.id, m.imageResId, m.title, m.calories));
                            }
                            cals += m.calories;
                        }
                    }
                    // Luôn thêm section kể cả khi danh sách món trống
                    {
                        String lbl;
                        switch (slot) {
                            case BREAKFAST: lbl = "Bữa sáng"; break;
                            case LUNCH: lbl = "Bữa trưa"; break;
                            case DINNER: lbl = "Bữa tối"; break;
                            default: lbl = "Ăn nhẹ"; break;
                        }
                        sections.add(new MealTimeAdapter.MealTimeSection(lbl, cals, recipes, slot));
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
        android.widget.TextView tvNote=root.findViewById(R.id.tvDayNote);
        if(tvCal!=null) tvCal.setText((int)totCals+" Calories");
        if(tvMacros!=null) tvMacros.setText((int)totCarb+"g Carbs, "+(int)totFat+"g Fat, "+(int)totPro+"g Protein");

        if(tvNote!=null){
            String noteStr = null;
            if(getContext()!=null){
                com.example.bepnhataapp.common.dao.MealDayDao dDao = new com.example.bepnhataapp.common.dao.MealDayDao(getContext());
                java.time.LocalDate curDate = null;
                if(getActivity() instanceof com.example.bepnhataapp.features.mealplan.MealPlanContentActivity){
                    curDate = ((com.example.bepnhataapp.features.mealplan.MealPlanContentActivity)getActivity()).getCurrentDate();
                }
                com.example.bepnhataapp.common.model.MealDay dEnt = curDate!=null ? dDao.getByDate(curDate.toString()) : null;
                if(dEnt!=null) noteStr = dEnt.getNote();
            }
            if(noteStr!=null && !noteStr.trim().isEmpty()){
                tvNote.setText("Ghi chú: "+noteStr);
                tvNote.setVisibility(View.VISIBLE);
            } else {
                tvNote.setVisibility(View.GONE);
            }
        }
    }
} 
