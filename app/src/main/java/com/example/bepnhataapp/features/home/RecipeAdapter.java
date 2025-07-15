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
import com.example.bepnhataapp.common.model.Recipe;

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
                android.widget.Toast.makeText(ctx, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                favDao.insert(new com.example.bepnhataapp.common.model.FavouriteRecipe() {{
                    setRecipeID(recipe.getId());
                    setCustomerID(c2.getCustomerID());
                    setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                }});
                android.widget.Toast.makeText(ctx, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
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

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeCategory = itemView.findViewById(R.id.recipe_category);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}
