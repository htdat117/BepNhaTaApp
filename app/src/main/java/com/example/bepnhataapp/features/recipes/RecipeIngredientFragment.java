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
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.common.adapter.IngredientAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.button.MaterialButton;

public class RecipeIngredientFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";
    private long recipeId;
    private int servingFactor = 1; // 1 = 2 người, 2 = 4 người
    private IngredientAdapter adapter;

    public static RecipeIngredientFragment newInstance(long recipeId, int servingFactor) {
        RecipeIngredientFragment fragment = new RecipeIngredientFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putInt("serving_factor", servingFactor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);
        // Set paddingTop cho header nếu có
        View header = view.findViewById(R.id.custom_header);
        if (header != null) {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
            header.setPadding(0, statusBarHeight, 0, 0);
        }
        if (getArguments() != null) {
            recipeId = getArguments().getLong(ARG_RECIPE_ID);
            servingFactor = getArguments().getInt("serving_factor", 1);
        }

        // Lấy dữ liệu local
        RecipeIngredientDao ingredientDao = new RecipeIngredientDao(getContext());
        List<RecipeIngredient> ingredients = ingredientDao.getByRecipeID(recipeId);
        IngredientDao ingDao = new IngredientDao(getContext());
        java.util.List<Ingredient> ingredientList = new java.util.ArrayList<>();
        if (ingredients != null && !ingredients.isEmpty()) {
            for (RecipeIngredient ing : ingredients) {
                Ingredient ingEntity = ingDao.getById(ing.getIngredientID());
                double qty = ing.getQuantity() * servingFactor;
                String qtyText = (qty == (int)qty ? String.valueOf((int)qty) : String.valueOf(qty));
                qtyText += (ingEntity != null && ingEntity.getUnit() != null ? " " + ingEntity.getUnit() : "");
                if (ingEntity != null) {
                    if (ingEntity.getImageLink() != null && !ingEntity.getImageLink().isEmpty()) {
                        ingredientList.add(new Ingredient(ingEntity.getImageLink(), ingEntity.getIngredientName(), qtyText));
                    } else if (ingEntity.getImage() != null && ingEntity.getImage().length > 0) {
                        ingredientList.add(new Ingredient(ingEntity.getImage(), ingEntity.getIngredientName(), qtyText));
                    } else {
                        ingredientList.add(new Ingredient(R.drawable.food_placeholder, ingEntity.getIngredientName(), qtyText));
                    }
                } else {
                    ingredientList.add(new Ingredient(R.drawable.food_placeholder, "Không rõ", qtyText));
                }
            }
        } else {
            ingredientList.add(new Ingredient(R.drawable.food_placeholder, "Không có dữ liệu", ""));
        }

        // Setup RecyclerView dạng lưới 3 cột
        RecyclerView rvIngredients = view.findViewById(R.id.rvIngredients);
        rvIngredients.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new IngredientAdapter(getContext(), ingredientList);
        rvIngredients.setAdapter(adapter);

        // Xử lý logic chọn số người
        MaterialButton btnNumofPeople = view.findViewById(R.id.btnNumofPeople);
        if (btnNumofPeople != null) {
            btnNumofPeople.setOnClickListener(v -> {
                android.widget.PopupMenu popup = new android.widget.PopupMenu(getContext(), btnNumofPeople);
                popup.getMenu().add("2 người");
                popup.getMenu().add("4 người");
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("2 người")) {
                        servingFactor = 1;
                        btnNumofPeople.setText("2 người");
                    } else {
                        servingFactor = 2;
                        btnNumofPeople.setText("4 người");
                    }
                    setServingFactor(servingFactor);
                    if(getActivity() instanceof RecipeDetailActivity){
                        ((RecipeDetailActivity)getActivity()).updateNutritionDisplay(servingFactor);
                    }
                    return true;
                });
                popup.show();
            });
        }

        // Ensure nutrition shown correctly on first load based on servingFactor
        if(getActivity() instanceof RecipeDetailActivity){
            ((RecipeDetailActivity)getActivity()).updateNutritionDisplay(servingFactor);
        }

        return view;
    }

    public void setServingFactor(int factor) {
        this.servingFactor = factor;
        if (getArguments() != null) getArguments().putInt("serving_factor", factor);
        // Cập nhật lại danh sách nguyên liệu
        RecipeIngredientDao ingredientDao = new RecipeIngredientDao(getContext());
        List<RecipeIngredient> ingredients = ingredientDao.getByRecipeID(recipeId);
        IngredientDao ingDao = new IngredientDao(getContext());
        java.util.List<Ingredient> ingredientList = new java.util.ArrayList<>();
        if (ingredients != null && !ingredients.isEmpty()) {
            for (RecipeIngredient ing : ingredients) {
                Ingredient ingEntity = ingDao.getById(ing.getIngredientID());
                double qty = ing.getQuantity() * servingFactor;
                String qtyText = (qty == (int)qty ? String.valueOf((int)qty) : String.valueOf(qty));
                qtyText += (ingEntity != null && ingEntity.getUnit() != null ? " " + ingEntity.getUnit() : "");
                if (ingEntity != null) {
                    if (ingEntity.getImageLink() != null && !ingEntity.getImageLink().isEmpty()) {
                        ingredientList.add(new Ingredient(ingEntity.getImageLink(), ingEntity.getIngredientName(), qtyText));
                    } else if (ingEntity.getImage() != null && ingEntity.getImage().length > 0) {
                        ingredientList.add(new Ingredient(ingEntity.getImage(), ingEntity.getIngredientName(), qtyText));
                    } else {
                        ingredientList.add(new Ingredient(R.drawable.food_placeholder, ingEntity.getIngredientName(), qtyText));
                    }
                } else {
                    ingredientList.add(new Ingredient(R.drawable.food_placeholder, "Không rõ", qtyText));
                }
            }
        } else {
            ingredientList.add(new Ingredient(R.drawable.food_placeholder, "Không có dữ liệu", ""));
        }
        if (adapter != null) adapter.updateData(ingredientList);
    }
} 
