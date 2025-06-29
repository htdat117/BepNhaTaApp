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

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_timeline, container, false);

        RecyclerView rv = view.findViewById(R.id.rvTimeline);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        MealPlanViewModel vm = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);
        MealPlanViewModel.State curState = vm.getState().getValue();

        List<MealTimeAdapter.MealTimeSection> sections = new java.util.ArrayList<>();
        int totalSections = 0;
        DayPlan day = null;
        if (curState != null && curState.week != null && getActivity() instanceof MealPlanContentActivity) {
            LocalDate date = ((MealPlanContentActivity) getActivity()).getCurrentDate();
            for (DayPlan d : curState.week.days) {
                if (d.date.equals(date)) { day = d; break; }
            }

            if (day != null) {
                DayPlan.MealTimeEnum[] slots = DayPlan.MealTimeEnum.values();
                for (DayPlan.MealTimeEnum slot : slots) {
                    List<MealTimeAdapter.RecipeItem> recipes = new java.util.ArrayList<>();
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
                    if (!recipes.isEmpty()) {
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

        // Nếu không có dữ liệu, hiển thị rỗng
        MealTimeAdapter adapter = new MealTimeAdapter(sections, new MealTimeAdapter.OnMealSectionActionListener() {
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

            private String slotToString(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum s){
                switch (s){
                    case BREAKFAST: return "Sáng";
                    case LUNCH: return "Trưa";
                    case DINNER: return "Tối";
                    default: return "Snack";
                }
            }
        });
        rv.setAdapter(adapter);

        ((TextView) view.findViewById(R.id.tvMealCount)).setText("Theo dõi 0/" + (totalSections==0? DayPlan.MealTimeEnum.values().length : totalSections) + " bữa ăn");

        // Tính tổng calo & macros
        double totCals=0, totCarb=0, totPro=0, totFat=0;
        if(day != null) {
            for(android.util.Pair<DayPlan.MealTimeEnum, Meal> p: day.meals){
                totCals += p.second.calories;
                totCarb += p.second.carbs;
                totPro += p.second.protein;
                totFat += p.second.fat;
            }
        }
        TextView tvCal=view.findViewById(R.id.tvCalories);
        TextView tvMacros=view.findViewById(R.id.tvMacros);
        tvCal.setText((int)totCals+" Calories");
        tvMacros.setText((int)totCarb+"g Carbs, "+(int)totFat+"g Fat, "+(int)totPro+"g Protein");

        return view;
    }
} 