package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;

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
        } else {
            // Attach button listeners for blank day actions
            Button btnAuto   = holder.placeholder.findViewById(R.id.btnAutoGenerate);
            Button btnCopy   = holder.placeholder.findViewById(R.id.btnCopyPrev);
            Button btnBlank  = holder.placeholder.findViewById(R.id.btnCreateBlank);

            // Convert util.Date to java.time.LocalDate
            java.time.LocalDate localDate = day.date.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            FragmentActivity act = (FragmentActivity) holder.itemView.getContext();
            com.example.bepnhataapp.features.mealplan.MealPlanViewModel vm =
                    new androidx.lifecycle.ViewModelProvider((androidx.lifecycle.ViewModelStoreOwner) act)
                            .get(com.example.bepnhataapp.features.mealplan.MealPlanViewModel.class);

            // 1. Auto generate meals for this day
            btnAuto.setOnClickListener(v -> {
                v.setEnabled(false);
                new Thread(() -> {
                    vm.autoGenerateFor(localDate);
                    act.runOnUiThread(() -> {
                        v.setEnabled(true);
                        Toast.makeText(act, "Đã tạo thực đơn tự động", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            });

            // 2. Copy previous day meals
            btnCopy.setOnClickListener(v -> {
                v.setEnabled(false);
                new Thread(() -> {
                    boolean copied = vm.copyFromPreviousDay(localDate);
                    act.runOnUiThread(() -> {
                        v.setEnabled(true);
                        String msg = copied ? "Đã sao chép thực đơn ngày trước" : "Không có thực đơn của ngày trước để sao chép";
                        Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
                    });
                }).start();
            });

            // 3. Create blank day – open day view for detailed editing
            btnBlank.setOnClickListener(v -> {
                Intent intent = new Intent(act, com.example.bepnhataapp.features.mealplan.MealPlanContentActivity.class);
                intent.putExtra("SELECTED_DATE", localDate.toString());
                act.startActivity(intent);
            });
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
