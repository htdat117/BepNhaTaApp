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
    public interface OnVoucherSelectListener{void onSelected(int index);} private OnVoucherSelectListener listener;
    public void setOnVoucherSelectListener(OnVoucherSelectListener l){this.listener=l;}
    public VoucherAdapter(List<VoucherItem> list){this.items=list;}
    public int getSelectedIndex(){return selected;}
    @NonNull @Override public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup p,int v){View view= LayoutInflater.from(p.getContext()).inflate(R.layout.item_voucher_select,p,false);return new VoucherViewHolder(view);}    
    @Override public void onBindViewHolder(@NonNull VoucherViewHolder h,int pos){
        VoucherItem it=items.get(pos);
        h.tvCode.setText(it.code);
        h.tvDesc.setText(it.desc);
        h.tvExpire.setText("HSD: "+it.expire);
        h.radio.setChecked(pos==selected);
        h.radio.setEnabled(it.enabled);
        h.itemView.setAlpha(it.enabled?1f:0.4f);
        h.itemView.setOnClickListener(v->{
            if(!it.enabled) return;
            selected=pos;notifyDataSetChanged();if(listener!=null)listener.onSelected(pos);
        });
    }
    @Override public int getItemCount(){return items==null?0:items.size();}
    static class VoucherViewHolder extends RecyclerView.ViewHolder{TextView tvCode,tvDesc,tvExpire;RadioButton radio;VoucherViewHolder(@NonNull View v){super(v);tvCode=v.findViewById(R.id.tvCode);tvDesc=v.findViewById(R.id.tvDesc);tvExpire=v.findViewById(R.id.tvExpire);radio=v.findViewById(R.id.radioSelect);} }
} 
