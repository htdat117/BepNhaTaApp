package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.IngredientDao;
import com.example.bepnhataapp.common.dao.RecipeIngredientDao;
import com.example.bepnhataapp.common.model.Ingredient;
import com.example.bepnhataapp.common.model.RecipeIngredient;
import java.util.List;
import android.util.Log;

public class RecipeIngredientFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";
    private long recipeId;

    public static RecipeIngredientFragment newInstance(long recipeId) {
        RecipeIngredientFragment fragment = new RecipeIngredientFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);
        if (getArguments() != null) recipeId = getArguments().getLong(ARG_RECIPE_ID);
        LinearLayout layoutIngredients = view.findViewById(R.id.layoutIngredients);
        RecipeIngredientDao ingredientDao = new RecipeIngredientDao(getContext());
        List<RecipeIngredient> ingredients = ingredientDao.getByRecipeID(recipeId);
        Log.d("RecipeIngredientFragment", "recipeId=" + recipeId + ", ingredients=" + ingredients.size());
        IngredientDao ingDao = new IngredientDao(getContext());
        layoutIngredients.removeAllViews();
        for (RecipeIngredient ing : ingredients) {
            Ingredient ingEntity = ingDao.getById(ing.getIngredientID());
            View v = inflater.inflate(R.layout.item_ingredient_grid, layoutIngredients, false);
            android.widget.ImageView iv = v.findViewById(R.id.ivIngredient);
            android.widget.TextView tvName = v.findViewById(R.id.tvIngredientName);
            android.widget.TextView tvQty = v.findViewById(R.id.tvIngredientQuantity);
            if (ingEntity != null) {
                if (ingEntity.getImageLink() != null && !ingEntity.getImageLink().isEmpty()) {
                    Glide.with(this).load(ingEntity.getImageLink()).placeholder(R.drawable.food_placeholder).into(iv);
                } else if (ingEntity.getImage() != null && ingEntity.getImage().length > 0) {
                    Glide.with(this).load(ingEntity.getImage()).placeholder(R.drawable.food_placeholder).into(iv);
                } else {
                    iv.setImageResource(R.drawable.food_placeholder);
                }
                tvName.setText(ingEntity.getIngredientName());
            } else {
                iv.setImageResource(R.drawable.food_placeholder);
                tvName.setText("Không rõ");
            }
            tvQty.setText(String.valueOf(ing.getQuantity()));
            layoutIngredients.addView(v);
        }
        return view;
    }
} 