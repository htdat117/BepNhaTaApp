package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

/** Adapter hiển thị 1 ngày và danh sách các buổi + món bên trong (dùng trong preview template). */
public class DayPreviewAdapter extends RecyclerView.Adapter<DayPreviewAdapter.DayVH> {

    public static class DayData {
        public final String label; // ví dụ: "Thứ 2, 15/7"
        public final List<MealTimeListAdapter.MealTimeWithMeals> mealsOfDay;
        public DayData(String l, List<MealTimeListAdapter.MealTimeWithMeals> m){ label=l; mealsOfDay=m; }
    }

    private final List<DayData> items;
    public DayPreviewAdapter(List<DayData> list){ this.items = list; }

    @NonNull
    @Override public DayVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_preview, parent,false);
        return new DayVH(v);
    }

    @Override public void onBindViewHolder(@NonNull DayVH holder, int position) {
        DayData d = items.get(position);
        holder.tvDate.setText(d.label);
        MealTimeListAdapter adapter = new MealTimeListAdapter(d.mealsOfDay);
        holder.rv.setAdapter(adapter);
    }

    @Override public int getItemCount(){ return items.size(); }

    static class DayVH extends RecyclerView.ViewHolder{
        TextView tvDate; RecyclerView rv;
        DayVH(@NonNull View item){
            super(item);
            tvDate = item.findViewById(R.id.tvDayLabel);
            rv = item.findViewById(R.id.rvDayMealTimes);
            rv.setLayoutManager(new LinearLayoutManager(item.getContext()));
        }
    }
} 