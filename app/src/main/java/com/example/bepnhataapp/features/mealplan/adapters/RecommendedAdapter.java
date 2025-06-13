package com.example.bepnhataapp.features.mealplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecoVH> {
    public static class RecoItem{
        public final int imageRes;public final String title;
        public RecoItem(int img,String title){this.imageRes=img;this.title=title;}
    }
    private final List<RecoItem> data;
    public RecommendedAdapter(List<RecoItem> d){data=d;}
    @NonNull @Override public RecoVH onCreateViewHolder(@NonNull ViewGroup p,int v){View v1= LayoutInflater.from(p.getContext()).inflate(R.layout.item_recommended,p,false);return new RecoVH(v1);}    
    @Override public void onBindViewHolder(@NonNull RecoVH h,int pos){RecoItem i=data.get(pos);h.img.setImageResource(i.imageRes);h.tv.setText(i.title);}    
    @Override public int getItemCount(){return data.size();}
    static class RecoVH extends RecyclerView.ViewHolder{ImageView img;TextView tv;RecoVH(View v){super(v);img=v.findViewById(R.id.imgReco);tv=v.findViewById(R.id.tvTitle);} }
} 