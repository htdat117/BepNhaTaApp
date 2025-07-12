package com.example.bepnhataapp.features.voucher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import java.util.List;

public class VoucherDisplayAdapter extends RecyclerView.Adapter<VoucherDisplayAdapter.VoucherDisplayViewHolder> {
    private final List<VoucherItem> items;
    private final Context context;
    private final int userPoint;

    public VoucherDisplayAdapter(List<VoucherItem> list, Context context, int userPoint) {
        this.items = list;
        this.context = context;
        this.userPoint = userPoint;
    }

    @NonNull
    @Override
    public VoucherDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_display, parent, false);
        return new VoucherDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherDisplayViewHolder holder, int position) {
        VoucherItem item = items.get(position);
        holder.tvCode.setText(item.code);
        holder.tvDesc.setText(item.desc);
        holder.tvExpire.setText("HSD: " + item.expire);
        holder.tvCondition.setText("Đổi: " + item.point + " điểm");
        // Thêm nút Đổi điểm (TextView làm nút)
        holder.tvRedeem.setVisibility(View.VISIBLE);
        holder.tvRedeem.setText("Đổi điểm");
        if (userPoint < item.point) {
            holder.tvRedeem.setEnabled(false);
            holder.tvRedeem.setAlpha(0.5f);
            holder.tvRedeem.setText("Không đủ điểm");
        } else {
            holder.tvRedeem.setEnabled(true);
            holder.tvRedeem.setAlpha(1f);
            holder.tvRedeem.setText("Đổi điểm");
        }
        holder.tvRedeem.setOnClickListener(v -> {
            if (userPoint < item.point) return;
            // TODO: Xử lý đổi điểm ở đây
            // Sau khi trừ điểm thành công:
            com.example.bepnhataapp.common.utils.NotificationHelper.pushEarnPoint(context, -item.point, "Đổi voucher", "Bạn đã sử dụng " + item.point + " điểm để đổi voucher.");
        });
        // Thêm xử lý sự kiện nhấp vào mục
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VoucherDetailActivity.class);
            intent.putExtra("code", item.code);
            intent.putExtra("desc", item.desc);
            intent.putExtra("expire", item.expire);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VoucherDisplayViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvDesc, tvExpire, tvCondition, tvRedeem;

        VoucherDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvExpire = itemView.findViewById(R.id.tvExpire);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvRedeem = itemView.findViewById(R.id.tvRedeem);
        }
    }
} 
