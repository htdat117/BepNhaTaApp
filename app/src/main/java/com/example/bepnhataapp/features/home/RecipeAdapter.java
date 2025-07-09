package com.example.bepnhataapp.features.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.Recipe;

import java.text.Normalizer;
import java.util.List;

/**
 * Adapter for displaying a list of recipes in a RecyclerView.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        String img = recipe.getImageUrl();
        Object source;
        if (img == null || img.isEmpty()) {
            source = R.drawable.placeholder_banner_background;
        } else if (img.startsWith("http")) {
            source = img;
        } else {
            int resId = holder.itemView.getContext().getResources()
                    .getIdentifier(img, "drawable", holder.itemView.getContext().getPackageName());
            source = resId != 0 ? resId : R.drawable.placeholder_banner_background;
        }

        Glide.with(holder.itemView.getContext())
                .load(source)
                .placeholder(R.drawable.placeholder_banner_background)
                .error(R.drawable.placeholder_banner_background)
                .into(holder.recipeImage);

        holder.recipeTitle.setText(recipe.getName());
        holder.recipeCategory.setText(recipe.getCategory());

        boolean isFav = false;
        if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(holder.itemView.getContext())) {
            String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(holder.itemView.getContext());
            com.example.bepnhataapp.common.dao.CustomerDao dao = new com.example.bepnhataapp.common.dao.CustomerDao(holder.itemView.getContext());
            com.example.bepnhataapp.common.model.Customer c = dao.findByPhone(phone);
            if (c != null) {
                com.example.bepnhataapp.common.dao.FavouriteRecipeDao frDao = new com.example.bepnhataapp.common.dao.FavouriteRecipeDao(holder.itemView.getContext());
                isFav = frDao.get(recipe.getId(), c.getCustomerID()) != null;
            }
        }
        recipe.setFavorite(isFav);
        holder.favoriteIcon.setImageResource(isFav ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);

        holder.favoriteIcon.setOnClickListener(v -> {
            android.content.Context ctx = holder.itemView.getContext();
            if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ctx)) {
                android.widget.Toast.makeText(ctx, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            String phone2 = com.example.bepnhataapp.common.utils.SessionManager.getPhone(ctx);
            com.example.bepnhataapp.common.dao.CustomerDao dao2 = new com.example.bepnhataapp.common.dao.CustomerDao(ctx);
            com.example.bepnhataapp.common.model.Customer c2 = dao2.findByPhone(phone2);
            if (c2 == null) return;

            com.example.bepnhataapp.common.dao.FavouriteRecipeDao favDao = new com.example.bepnhataapp.common.dao.FavouriteRecipeDao(ctx);
            boolean currentlyFav = favDao.get(recipe.getId(), c2.getCustomerID()) != null;
            if (currentlyFav) {
                favDao.delete(recipe.getId(), c2.getCustomerID());
            } else {
                favDao.insert(new com.example.bepnhataapp.common.model.FavouriteRecipe() {{
                    setRecipeID(recipe.getId());
                    setCustomerID(c2.getCustomerID());
                    setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                }});
            }
            recipe.setFavorite(!currentlyFav);
            notifyItemChanged(position);
        });

        holder.itemView.setOnClickListener(v -> {
            android.content.Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, com.example.bepnhataapp.features.recipes.RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            context.startActivity(intent);
        });

        // Benefit icon mapping (if view exists in layout)
        if(holder.itemView.findViewById(R.id.imvUse)!=null && holder.itemView.findViewById(R.id.txtBenefit)!=null){
            ImageView icon = (ImageView)holder.itemView.findViewById(R.id.imvUse);
            TextView tvBenefit = (TextView)holder.itemView.findViewById(R.id.txtBenefit);
            String benefit = tvBenefit.getText().toString();
            if(benefit!=null && !benefit.isEmpty()){
                String slug = slugify(benefit);
                int resId = holder.itemView.getResources().getIdentifier("ic_"+slug,"drawable",holder.itemView.getContext().getPackageName());
                if(resId==0){
                    if(slug.contains("ngu")) slug="sleepy";
                    else if(slug.contains("skin")||slug.contains("da")) slug="skin";
                    else if(slug.contains("xuong")) slug="bone";
                    else if(slug.contains("bo_mau")|| (slug.contains("mau")&&!slug.contains("bo_mau"))) slug="blood";
                    else if(slug.contains("giai_doc")||slug.contains("detox")||slug.contains("doc")) slug="detox";
                    else if(slug.contains("giam_can")||slug.contains("weight")) slug="weight";
                    else if(slug.contains("tim")||slug.contains("heart")) slug="tim";
                    resId = holder.itemView.getResources().getIdentifier("ic_"+slug,"drawable",holder.itemView.getContext().getPackageName());
                    if(resId==0) resId= R.drawable.ic_bone;
                }
                icon.setImageResource(resId);
                if(slug.equals("skin")){
                    android.view.ViewGroup.LayoutParams lp = icon.getLayoutParams();
                    lp.width = lp.height = (int) (20*holder.itemView.getResources().getDisplayMetrics().density);
                    icon.setLayoutParams(lp);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipeCategory;
        ImageView favoriteIcon;
        ImageView imvUse; // benefit icon

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeCategory = itemView.findViewById(R.id.recipe_category);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            imvUse = itemView.findViewById(R.id.imvUse);
        }
    }

    private String slugify(String input){
        if(input==null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        temp = temp.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+","_");
        if(temp.startsWith("_")) temp=temp.substring(1);
        if(temp.endsWith("_")) temp=temp.substring(0,temp.length()-1);
        return temp;
    }
}