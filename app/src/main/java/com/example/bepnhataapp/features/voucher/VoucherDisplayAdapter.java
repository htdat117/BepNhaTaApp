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

    public VoucherDisplayAdapter(List<VoucherItem> list, Context context) {
        this.items = list;
        this.context = context;
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
        // Thêm xử lý sự kiện nhấp vào mục
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VoucherDetailActivity.class);
            // Truyền dữ liệu voucher sang trang chi tiết (nếu có)
            intent.putExtra("code", item.code);
            intent.putExtra("desc", item.desc);
            intent.putExtra("expire", item.expire);
            // Các trường khác như 'point', 'time', 'benefit', 'product', 'payment', 'shipping', 'device', 'condition' có thể cần được thêm vào VoucherItem và truyền đi nếu VoucherDetailActivity yêu cầu.
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VoucherDisplayViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvDesc, tvExpire, tvCondition;

        VoucherDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvExpire = itemView.findViewById(R.id.tvExpire);
            tvCondition = itemView.findViewById(R.id.tvCondition);
        }
    }
} 