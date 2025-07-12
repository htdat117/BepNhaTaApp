package com.example.bepnhataapp.features.checkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.VH>{
    public interface OnMethodSelected{void onSelected(int index);}
    public static class Method{
        public final int iconRes; public final String name;
        public Method(int icon,String name){this.iconRes=icon;this.name=name;}
    }
    private final List<Method> methods;
    private int selected = -1;
    private final OnMethodSelected listener;
    public PaymentAdapter(List<Method> m,OnMethodSelected l){this.methods=m;this.listener=l;}

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method,parent,false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h,int pos){
        Method m=methods.get(pos);
        h.icon.setImageResource(m.iconRes);
        h.name.setText(m.name);
        h.tick.setVisibility(pos == selected ? View.VISIBLE : View.INVISIBLE);
        h.itemView.setOnClickListener(v->{
            int old = selected;
            selected = pos;
            if (old >= 0) notifyItemChanged(old);
            notifyItemChanged(selected);
            if(listener!=null) listener.onSelected(selected);
        });
    }

    @Override public int getItemCount(){return methods==null?0:methods.size();}

    static class VH extends RecyclerView.ViewHolder{
        ImageView icon,tick; TextView name;
        VH(View v){super(v); icon=v.findViewById(R.id.imgIcon); tick=v.findViewById(R.id.imgTick); name=v.findViewById(R.id.tvName);}
    }
} 
