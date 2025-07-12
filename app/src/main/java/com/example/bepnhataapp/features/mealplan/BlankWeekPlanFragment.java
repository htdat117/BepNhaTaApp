package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

public class BlankWeekPlanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_week_plan, container, false);
        LinearLayout list = view.findViewById(R.id.containerDays);
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        for (String d : days) {
            View item = inflater.inflate(R.layout.item_blank_week_day, list, false);
            ((TextView) item.findViewById(R.id.tvDayLabel)).setText(d);
            list.addView(item);
        }
        return view;
    }
} 
