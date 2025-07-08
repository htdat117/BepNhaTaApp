package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayVH> {

    public static class DayTimeline {
        public final Date date;
        public final List<MealTimeListAdapter.MealTimeWithMeals> mealTimes;
        public DayTimeline(Date d, List<MealTimeListAdapter.MealTimeWithMeals> m){ date = d; mealTimes = m; }
    }

    private final List<DayTimeline> days;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());

    public WeekAdapter(List<DayTimeline> ds){ this.days = ds; }

    @NonNull
    @Override
    public DayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_day, parent, false);
        return new DayVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DayVH holder, int position) {
        DayTimeline day = days.get(position);
        holder.tvHeader.setText(dateFmt.format(day.date));
        boolean hasData = day.mealTimes != null && !day.mealTimes.isEmpty();
        holder.rvMealTimes.setVisibility(hasData ? View.VISIBLE : View.GONE);
        holder.placeholder.setVisibility(hasData ? View.GONE : View.VISIBLE);
        if (hasData) {
            MealTimeListAdapter adapter = new MealTimeListAdapter(day.mealTimes);
            holder.rvMealTimes.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() { return days.size(); }

    static class DayVH extends RecyclerView.ViewHolder {
        TextView tvHeader; RecyclerView rvMealTimes; View placeholder;
        DayVH(@NonNull View itemView){
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvDayLabel);
            rvMealTimes = itemView.findViewById(R.id.rvMealTimes);
            rvMealTimes.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            placeholder = itemView.findViewById(R.id.placeholderContainer);
        }
    }
} 