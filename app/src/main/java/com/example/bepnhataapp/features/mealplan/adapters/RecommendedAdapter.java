package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ******************** Data model ********************
    public static class RecoItem {
        public final int imageRes; // 0 if using URL
        public final String imageUrl; // may be null
        public final String title;
        public final int count;
        public RecoItem(int img, String t){this(img,t,0,null);} // default
        public RecoItem(int img, String t, int c){this(img,t,c,null);} // img resource
        public RecoItem(String url,String t,int c){this(0,t,c,url);} // url variant
        public RecoItem(int img,String t,int c,String url){imageRes=img;title=t;count=c;imageUrl=url;}
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    private final List<Object> data; // Elements are either String (header) or RecoItem

    public RecommendedAdapter(List<Object> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return (data.get(position) instanceof RecoItem) ? TYPE_ITEM : TYPE_HEADER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View v = inflater.inflate(R.layout.item_recommended_header, parent, false);
            return new HeaderVH(v);
        } else {
            View v = inflater.inflate(R.layout.item_recommended, parent, false);
            return new ItemVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object obj = data.get(position);
        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).tv.setText((String) obj);
        } else if (holder instanceof ItemVH) {
            RecoItem item = (RecoItem) obj;
            ItemVH vh = (ItemVH) holder;
            if(item.imageUrl!=null && !item.imageUrl.isEmpty()){
                Glide.with(vh.img.getContext())
                        .load(item.imageUrl)
                        .placeholder(item.imageRes!=0?item.imageRes:R.drawable.placeholder_banner_background)
                        .into(vh.img);
            }else{
                vh.img.setImageResource(item.imageRes);
            }
            vh.tv.setText(item.title);
            if(vh.badge!=null){
                if(item.count>0){
                    vh.badge.setText(item.count+" thực đơn");
                    vh.badge.setVisibility(View.VISIBLE);
                }else vh.badge.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ******************** ViewHolders ********************
    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView tv;
        HeaderVH(View v) { super(v); tv = v.findViewById(R.id.tvHeaderTitle); }
    }

    static class ItemVH extends RecyclerView.ViewHolder {
        ImageView img; TextView tv; TextView badge;
        ItemVH(View v) {
            super(v);
            img = v.findViewById(R.id.imgReco);
            tv = v.findViewById(R.id.tvTitle);
            badge = v.findViewById(R.id.tvBadge);
        }
    }
} 