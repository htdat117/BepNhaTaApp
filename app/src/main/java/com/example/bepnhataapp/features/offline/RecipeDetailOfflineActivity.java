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
        TextView tvCategory = findViewById(R.id.tvRecipeCategory);
        TextView tvCaloTime = findViewById(R.id.tvRecipeCaloTime);
        TextView tvDescription = findViewById(R.id.tvDescription);
        LinearLayout layoutNutrition = findViewById(R.id.layoutNutrition);
        LinearLayout layoutIngredients = findViewById(R.id.layoutIngredients);
        LinearLayout layoutInstructions = findViewById(R.id.layoutInstructions);

        // Lấy dữ liệu công thức
        RecipeDao recipeDao = new RecipeDao(this);
        RecipeEntity entity = recipeDao.getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == recipeId)
                .findFirst().orElse(null);
        if (entity != null) {
            txtName.setText(entity.getRecipeName());
            tvCategory.setText(entity.getCategory());
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
            tvCaloTime.setText((int)detail.getCalo() + " Kcal • " + detail.getCookingTimeMinutes() + " Min");
            layoutNutrition.removeAllViews();
            addNutrition(layoutNutrition, "Calo", (int)detail.getCalo() + " Kcal", R.drawable.ic_calo);
            addNutrition(layoutNutrition, "Protein", (int)detail.getProtein() + "g", R.drawable.ic_protein);
            addNutrition(layoutNutrition, "Carbs", (int)detail.getCarbs() + "g", R.drawable.ic_carb);
            addNutrition(layoutNutrition, "Fat", (int)detail.getFat() + "g", R.drawable.ic_fat);
        }

        // Nguyên liệu
        RecipeIngredientDao ingredientDao = new RecipeIngredientDao(this);
        List<RecipeIngredient> ingredients = ingredientDao.getByRecipeID(recipeId);
        IngredientDao ingDao = new IngredientDao(this);
        layoutIngredients.removeAllViews();
        for (RecipeIngredient ing : ingredients) {
            Ingredient ingEntity = ingDao.getById(ing.getIngredientID());
            addIngredient(layoutIngredients, ingEntity, ing.getQuantity());
        }

        // Hướng dẫn
        InstructionRecipeDao instructionDao = new InstructionRecipeDao(this);
        List<InstructionRecipe> instructions = instructionDao.getByRecipe(recipeId);
        layoutInstructions.removeAllViews();
        for (InstructionRecipe ins : instructions) {
            addInstruction(layoutInstructions, ins.getNumberSection(), ins.getTitle(), ins.getContent());
        }
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

    private void addIngredient(LinearLayout parent, Ingredient ingEntity, double quantity) {
        View v = getLayoutInflater().inflate(R.layout.item_ingredient_grid, parent, false);
        ImageView iv = v.findViewById(R.id.ivIngredient);
        TextView tvName = v.findViewById(R.id.tvIngredientName);
        TextView tvQty = v.findViewById(R.id.tvIngredientQuantity);
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
        tvQty.setText(String.valueOf(quantity));
        parent.addView(v);
    }

    private void addInstruction(LinearLayout parent, int step, String title, String content) {
        TextView tv = new TextView(this);
        tv.setText("Bước " + step + ": " + title + "\n" + content);
        tv.setTextSize(15);
        tv.setPadding(0, 8, 0, 8);
        parent.addView(tv);
    }
} 