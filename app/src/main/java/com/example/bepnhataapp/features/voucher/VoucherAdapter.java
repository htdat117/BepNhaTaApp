package com.example.bepnhataapp.features.voucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>{
    private final List<VoucherItem> items;private int selected=-1;
    public VoucherAdapter(List<VoucherItem> list){this.items=list;}
    @NonNull @Override public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup p,int v){View view= LayoutInflater.from(p.getContext()).inflate(R.layout.item_voucher_select,p,false);return new VoucherViewHolder(view);}    
    @Override public void onBindViewHolder(@NonNull VoucherViewHolder h,int pos){VoucherItem it=items.get(pos);h.tvCode.setText(it.code);h.tvDesc.setText(it.desc);h.tvExpire.setText("HSD: "+it.expire);h.radio.setChecked(pos==selected);h.itemView.setOnClickListener(v->{selected=pos;notifyDataSetChanged();});}
    @Override public int getItemCount(){return items==null?0:items.size();}
    static class VoucherViewHolder extends RecyclerView.ViewHolder{TextView tvCode,tvDesc,tvExpire;RadioButton radio;VoucherViewHolder(@NonNull View v){super(v);tvCode=v.findViewById(R.id.tvCode);tvDesc=v.findViewById(R.id.tvDesc);tvExpire=v.findViewById(R.id.tvExpire);radio=v.findViewById(R.id.radioSelect);} }
} 