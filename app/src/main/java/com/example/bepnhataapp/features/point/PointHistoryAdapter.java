package com.example.bepnhataapp.features.point;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import java.util.List;

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.HistoryViewHolder> {
    private final List<PointHistoryItem> items;

    public PointHistoryAdapter(List<PointHistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        PointHistoryItem item = items.get(position);
        holder.tvVoucherTitle.setText(item.getTitle());
        int point = item.getPoint();
        String pointText = (point > 0 ? "+" : "") + point + " điểm";
        holder.tvPoint.setText(pointText);
        holder.tvPoint.setTextColor(point > 0 ? 0xFFF36B22 : 0xFFE53935); // Cam cho cộng, đỏ cho trừ
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvVoucherTitle, tvPoint;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVoucherTitle = itemView.findViewById(R.id.tvVoucherTitle);
            tvPoint = itemView.findViewById(R.id.tvPoint);
        }
    }

    // Model cho item lịch sử
    public static class PointHistoryItem {
        private final String title;
        private final String desc;
        private final int point;
        public PointHistoryItem(String title, String desc, int point) {
            this.title = title;
            this.desc = desc;
            this.point = point;
        }
        public String getTitle() { return title; }
        public String getDesc() { return desc; }
        public int getPoint() { return point; }
    }
} 
