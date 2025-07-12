package com.example.bepnhataapp.features.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;

import java.text.Normalizer;
import java.util.List;

public class RecommendedRecipeAdapter extends RecyclerView.Adapter<RecommendedRecipeAdapter.RecommendViewHolder> {

    private final List<RecipeItem> list;
    private final RecipeAdapter.OnRecipeActionListener listener;

    public RecommendedRecipeAdapter(List<RecipeItem> list, RecipeAdapter.OnRecipeActionListener l){
        this.list = list;
        this.listener = l;
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_recipe, parent, false);
        return new RecommendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder h, int pos) {
        RecipeItem item = list.get(pos);
        // dynamic benefit icon selection
        ImageView imvUse = h.itemView.findViewById(R.id.imvUse);
        // image
        if (item.getImageData()!=null && item.getImageData().length>0) {
            Glide.with(h.imv.getContext()).load(item.getImageData()).placeholder(R.drawable.placeholder_banner_background).into(h.imv);
        } else if(item.getImageUrl()!=null && !item.getImageUrl().isEmpty()){
            String url = item.getImageUrl();
            if(url.startsWith("http"))
                Glide.with(h.imv.getContext()).load(url).placeholder(R.drawable.placeholder_banner_background).into(h.imv);
            else {
                int resId = h.imv.getContext().getResources().getIdentifier(url, "drawable", h.imv.getContext().getPackageName());
                h.imv.setImageResource(resId!=0?resId: R.drawable.placeholder_banner_background);
            }
        } else {
            h.imv.setImageResource(item.getImageResId());
        }
        h.tvName.setText(item.getName());
        h.tvTime.setText(item.getTime());
        h.tvLevel.setText(item.getLevel());
        if(h.tvBenefit!=null) h.tvBenefit.setText(item.getBenefit());

        // set icon for benefit
        if(imvUse != null && item.getBenefit()!=null){
            String slug = slugify(item.getBenefit());
            int res = h.itemView.getResources().getIdentifier("ic_"+slug, "drawable", h.itemView.getContext().getPackageName());
            if(res==0){
                if(slug.contains("ngu")) slug="sleepy";
                else if(slug.contains("skin")||slug.contains("da")) slug="skin";
                else if(slug.contains("xuong")) slug="bone";
                else if(slug.contains("bo_mau")|| (slug.contains("mau")&&!slug.contains("bo_mau"))) slug="blood";
                else if(slug.contains("giai_doc")||slug.contains("detox")||slug.contains("doc")) slug="detox";
                else if(slug.contains("giam_can")||slug.contains("weight")) slug="weight";
                else if(slug.contains("tim")||slug.contains("heart")) slug="tim";
                res = h.itemView.getResources().getIdentifier("ic_"+slug, "drawable", h.itemView.getContext().getPackageName());
                if(res==0) res = R.drawable.ic_bone; // fallback
            }
            imvUse.setImageResource(res);
        }
        h.tvLove.setText(String.valueOf(item.getLikeCount()));
        h.tvCmt.setText(String.valueOf(item.getCommentCount()));

        h.itemView.setOnClickListener(v -> {
            if(listener!=null) listener.onView(item);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class RecommendViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;
        TextView tvName, tvTime, tvBenefit, tvLevel, tvLove, tvCmt;
        RecommendViewHolder(@NonNull View v){
            super(v);
            imv = v.findViewById(R.id.imvRecipe);
            tvName = v.findViewById(R.id.txtName);
            tvTime = v.findViewById(R.id.txtTime);
            tvBenefit = v.findViewById(R.id.txtBenefit);
            tvLevel = v.findViewById(R.id.txtLevel);
            tvLove = v.findViewById(R.id.txtLove);
            tvCmt = v.findViewById(R.id.txtComment);
        }
    }

    private String slugify(String input){
        if(input==null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "d");
        temp = temp.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+","_");
        if(temp.startsWith("_")) temp = temp.substring(1);
        if(temp.endsWith("_")) temp = temp.substring(0,temp.length()-1);
        return temp;
    }
} 
