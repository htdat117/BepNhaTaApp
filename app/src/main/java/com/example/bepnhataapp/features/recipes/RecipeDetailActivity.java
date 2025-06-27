package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.RecipeEntity;

public class RecipeDetailActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        ImageView img = findViewById(R.id.imvRecipe);
        TextView tvName = findViewById(R.id.txtName);
        TextView tvCategory = findViewById(R.id.tvRecipeCategory);
        TextView tvCaloTime = findViewById(R.id.tvRecipeCaloTime);

        // Load recipe entity
        RecipeEntity entity = new RecipeDao(this).getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == recipeId)
                .findFirst()
                .orElse(null);
        if (entity != null) {
            tvName.setText(entity.getRecipeName());
            tvCategory.setText(entity.getCategory());
            Object source;
            String imgStr = entity.getImageThumb() != null ? entity.getImageThumb().trim() : "";
            if (imgStr.isEmpty()) {
                source = R.drawable.placeholder_banner_background;
            } else if (imgStr.startsWith("http")) {
                source = imgStr;
            } else {
                int resId = getResources().getIdentifier(imgStr, "drawable", getPackageName());
                source = resId != 0 ? resId : R.drawable.placeholder_banner_background;
            }
            Glide.with(this).load(source).placeholder(R.drawable.placeholder_banner_background).into(img);
        }

        RecipeDetail detail = new RecipeDetailDao(this).get(recipeId);
        if (detail != null) {
            String str = String.format("%d Kcal • %d Min", (int) detail.getCalo(), detail.getCookingTimeMinutes());
            tvCaloTime.setText(str);
        }

        // bottom nav
        setupBottomNavigationFragment(R.id.nav_recipes);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }
} 