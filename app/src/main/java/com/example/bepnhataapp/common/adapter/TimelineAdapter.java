package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineVH> {

    public static class TimelineItem{
        public final String mealTime;
        public final String mealName;
        public final int kcal;
        public final int imageRes;
        public TimelineItem(String mealTime,String mealName,int kcal,int imageRes){this.mealTime=mealTime;this.mealName=mealName;this.kcal=kcal;this.imageRes=imageRes;}
    }

    private final List<TimelineItem> items;
    public TimelineAdapter(List<TimelineItem> items){this.items=items;}

    @NonNull
    @Override
    public TimelineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline,parent,false);
        return new TimelineVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineVH holder, int position) {
        TimelineItem it = items.get(position);
        holder.tvTime.setText(it.mealTime + " - " + it.kcal + " Kcal");
        holder.tvName.setText(it.mealName);
        holder.img.setImageResource(it.imageRes);
    }

    @Override
    public int getItemCount() {return items.size();}

    static class TimelineVH extends RecyclerView.ViewHolder{
        TextView tvTime,tvName;ImageView img;
        TimelineVH(@NonNull View itemView){super(itemView);tvTime=itemView.findViewById(R.id.tvMealTime);tvName=itemView.findViewById(R.id.tvMealName);img=itemView.findViewById(R.id.imgMeal);}    }
} 
