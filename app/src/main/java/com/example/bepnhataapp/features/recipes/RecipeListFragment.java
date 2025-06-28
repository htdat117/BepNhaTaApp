package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bepnhataapp.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.RecipeEntity;

public class RecipeListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipe_list, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo RecyclerView và setAdapter
        RecyclerView rvRecipe = view.findViewById(R.id.rvRecipe);
        rvRecipe.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách công thức từ database
        java.util.List<RecipeEntity> recipeEntities = new RecipeDao(getContext()).getAllRecipes();
        java.util.List<RecipeItem> recipeItems = new ArrayList<>();
        for (RecipeEntity entity : recipeEntities) {
            String imgStr = entity.getImageThumb() != null ? entity.getImageThumb().trim() : "";
            byte[] imgData = null;
            try {
                java.lang.reflect.Method m = entity.getClass().getMethod("getImage");
                imgData = (byte[]) m.invoke(entity);
            } catch (Exception e) { /* ignore nếu không có */ }
            int imageResId = R.drawable.placeholder_banner_background;
            if (!imgStr.isEmpty()) {
                int resId = getResources().getIdentifier(imgStr, "drawable", getContext().getPackageName());
                if (resId != 0) imageResId = resId;
            }
            recipeItems.add(new RecipeItem(
                imageResId,
                imgStr,
                imgData,
                entity.getRecipeName(),
                "", // calories
                entity.getCategory() != null ? entity.getCategory() : "",
                "" // time
            ));
        }

        RecipeAdapter adapter = new RecipeAdapter(recipeItems, new RecipeAdapter.OnRecipeActionListener() {
            @Override
            public void onView(RecipeItem recipe) {
                // Tìm recipeId theo tên (nên tốt nhất là truyền id vào RecipeItem)
                RecipeEntity found = null;
                for (RecipeEntity entity : recipeEntities) {
                    if (entity.getRecipeName().equals(recipe.getName())) {
                        found = entity;
                        break;
                    }
                }
                if (found != null) {
                    Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                    intent.putExtra("recipeId", found.getRecipeID());
                    startActivity(intent);
                }
            }
            @Override
            public void onDelete(RecipeItem recipe) {
                // Không hỗ trợ xóa ở đây
            }
        });
        rvRecipe.setAdapter(adapter);
        return view;
    }
}