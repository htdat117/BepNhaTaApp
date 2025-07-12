package com.example.bepnhataapp.features.offline;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.dao.RecipeIngredientDao;
import com.example.bepnhataapp.common.dao.InstructionRecipeDao;
import com.example.bepnhataapp.common.dao.IngredientDao;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.RecipeIngredient;
import com.example.bepnhataapp.common.model.InstructionRecipe;
import com.example.bepnhataapp.common.model.Ingredient;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.common.adapter.IngredientAdapter;
import com.example.bepnhataapp.features.recipes.RecipeStepGuideAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class RecipeDetailOfflineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_offline);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView imvRecipe = findViewById(R.id.imvRecipe);
        TextView txtName = findViewById(R.id.txtName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        LinearLayout layoutNutrition = findViewById(R.id.layoutNutrition);
        RecyclerView rvIngredients = findViewById(R.id.rvIngredients);
        RecyclerView rvInstructions = findViewById(R.id.rvInstructions);

        MaterialButtonToggleGroup toggleGroupTab = findViewById(R.id.toggleGroupTab);
        toggleGroupTab.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTabIngredient) {
                    rvIngredients.setVisibility(View.VISIBLE);
                    rvInstructions.setVisibility(View.GONE);
                } else if (checkedId == R.id.btnTabGuide) {
                    rvIngredients.setVisibility(View.GONE);
                    rvInstructions.setVisibility(View.VISIBLE);
                }
            }
        });

        // Lấy dữ liệu công thức
        RecipeDao recipeDao = new RecipeDao(this);
        RecipeEntity entity = recipeDao.getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == recipeId)
                .findFirst().orElse(null);
        if (entity != null) {
            txtName.setText(entity.getRecipeName());
            tvDescription.setText(entity.getDescription());
            String imgStr = entity.getImageThumb() != null ? entity.getImageThumb().trim() : "";
            if (imgStr.isEmpty()) {
                imvRecipe.setImageResource(R.drawable.food_placeholder);
            } else if (imgStr.startsWith("http")) {
                Glide.with(this).load(imgStr).placeholder(R.drawable.food_placeholder).into(imvRecipe);
            } else {
                int resId = getResources().getIdentifier(imgStr, "drawable", getPackageName());
                imvRecipe.setImageResource(resId != 0 ? resId : R.drawable.food_placeholder);
            }
        }

        // Dinh dưỡng
        RecipeDetailDao detailDao = new RecipeDetailDao(this);
        RecipeDetail detail = detailDao.get(recipeId);
        if (detail != null) {
            layoutNutrition.removeAllViews();
            addNutrition(layoutNutrition, "Carbs", (int)detail.getCarbs() + "g carbs", R.drawable.ic_carb);
            addNutrition(layoutNutrition, "Protein", (int)detail.getProtein() + "g proteins", R.drawable.ic_protein);
            addNutrition(layoutNutrition, "Calo", (int)detail.getCalo() + " Kcal", R.drawable.ic_calo);
            addNutrition(layoutNutrition, "Fat", (int)detail.getFat() + "g fat", R.drawable.ic_fat);
        }

        // Nguyên liệu
        RecipeIngredientDao ingredientDao = new RecipeIngredientDao(this);
        List<RecipeIngredient> ingredients = ingredientDao.getByRecipeID(recipeId);
        IngredientDao ingDao = new IngredientDao(this);
        rvIngredients.removeAllViews();
        // Chuẩn bị dữ liệu nguyên liệu
        List<Ingredient> ingredientEntities = new ArrayList<>();
        for (RecipeIngredient ing : ingredients) {
            Ingredient ingEntity = ingDao.getById(ing.getIngredientID());
            if (ingEntity != null) {
                // Gán số lượng vào thuộc tính quantity để adapter hiển thị
                ingEntity.quantity = ing.getQuantity() + "";
                ingredientEntities.add(ingEntity);
            }
        }
        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, ingredientEntities, R.layout.item_ingredient);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(ingredientAdapter);

        // Hướng dẫn
        InstructionRecipeDao instructionDao = new InstructionRecipeDao(this);
        List<InstructionRecipe> instructions = instructionDao.getByRecipe(recipeId);
        rvInstructions.removeAllViews();
        // Chuẩn bị dữ liệu các bước thực hiện
        RecipeStepGuideAdapter stepAdapter = new RecipeStepGuideAdapter(instructions);
        rvInstructions.setLayoutManager(new LinearLayoutManager(this));
        rvInstructions.setAdapter(stepAdapter);
    }

    private void addNutrition(LinearLayout parent, String name, String value, int iconRes) {
        View v = getLayoutInflater().inflate(R.layout.item_nutrition, parent, false);
        ImageView iv = v.findViewById(R.id.ivNutritionIcon);
        TextView tvName = v.findViewById(R.id.tvNutritionName);
        TextView tvValue = v.findViewById(R.id.tvNutritionValue);
        iv.setImageResource(iconRes);
        tvName.setText(name);
        tvValue.setText(value);
        parent.addView(v);
    }
} 
