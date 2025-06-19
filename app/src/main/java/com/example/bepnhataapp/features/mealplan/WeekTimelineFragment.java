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
import com.example.bepnhataapp.features.mealplan.adapters.MealAdapter;
import com.example.bepnhataapp.features.mealplan.adapters.MealTimeListAdapter;
import com.example.bepnhataapp.features.mealplan.adapters.WeekAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        WeekAdapter adapter = new WeekAdapter(generateFakeWeek());
        rv.setAdapter(adapter);
        return root;
    }

    private List<WeekAdapter.DayTimeline> generateFakeWeek() {
        List<WeekAdapter.DayTimeline> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            Date date = cal.getTime();
            List<MealTimeListAdapter.MealTimeWithMeals> mealTimes = new ArrayList<>();

            if (i % 2 == 0) { // giả định ngày chẵn có dữ liệu
                List<MealAdapter.MealRow> breakfastMeals = new ArrayList<>();
                breakfastMeals.add(new MealAdapter.MealRow("Trứng ốp la", R.drawable.placeholder_banner_background));
                breakfastMeals.add(new MealAdapter.MealRow("Bánh mì", R.drawable.placeholder_banner_background));
                mealTimes.add(new MealTimeListAdapter.MealTimeWithMeals("Bữa sáng", breakfastMeals));

                List<MealAdapter.MealRow> lunchMeals = new ArrayList<>();
                lunchMeals.add(new MealAdapter.MealRow("Cơm gà", R.drawable.placeholder_banner_background));
                mealTimes.add(new MealTimeListAdapter.MealTimeWithMeals("Bữa trưa", lunchMeals));
            }

            // nếu mealTimes rỗng => ngày trống
            list.add(new WeekAdapter.DayTimeline(date, mealTimes));
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }
} 