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
import android.widget.TextView;

import com.example.bepnhataapp.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.RecipeEntity;

public class RecipeListFragment extends Fragment {

    private java.util.List<RecipeEntity> allRecipeEntities = new java.util.ArrayList<>();
    private java.util.List<RecipeItem> recipeItems = new java.util.ArrayList<>();
    private RecipeAdapter adapter;
    private com.example.bepnhataapp.features.recipes.FilterRecipeBottomSheet.FilterCriteria currentCriteria;
    private boolean isSortAZ = true;

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
        allRecipeEntities = new RecipeDao(getContext()).getAllRecipes();
        recipeItems = new java.util.ArrayList<>();
        for (RecipeEntity entity : allRecipeEntities) {
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

        adapter = new RecipeAdapter(recipeItems, new RecipeAdapter.OnRecipeActionListener() {
            @Override
            public void onView(RecipeItem recipe) {
                // Tìm recipeId theo tên (nên tốt nhất là truyền id vào RecipeItem)
                RecipeEntity found = null;
                for (RecipeEntity entity : allRecipeEntities) {
                    if (entity.getRecipeName().equals(recipe.getName())) {
                        found = entity;
                        break;
                    }
                }
                if (found != null) {
                    Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                    intent.putExtra("recipeId", (long) found.getRecipeID());
                    startActivity(intent);
                }
            }
            @Override
            public void onDelete(RecipeItem recipe) {
                // Không hỗ trợ xóa ở đây
            }
        });
        rvRecipe.setAdapter(adapter);

        // Thêm sự kiện filter
        View btnFilter = view.findViewById(R.id.btnFilterRecipe);
        btnFilter.setOnClickListener(v -> {
            com.example.bepnhataapp.features.recipes.FilterRecipeBottomSheet bottomSheet = new com.example.bepnhataapp.features.recipes.FilterRecipeBottomSheet();
            bottomSheet.setInitialCriteria(currentCriteria);
            bottomSheet.setOnFilterAppliedListener(criteria -> {
                currentCriteria = criteria;
                applyRecipeFilter(criteria);
            });
            bottomSheet.show(getParentFragmentManager(), "FilterRecipeBottomSheet");
        });

        TextView txtSortOption = view.findViewById(R.id.txtSortOption);
        txtSortOption.setOnClickListener(v -> {
            isSortAZ = !isSortAZ;
            if (isSortAZ) {
                txtSortOption.setText("A - Z");
                java.util.Collections.sort(recipeItems, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            } else {
                txtSortOption.setText("Z - A");
                java.util.Collections.sort(recipeItems, (o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
            }
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    // Hàm so khớp lợi ích sức khỏe từng từ
    private boolean matchHealth(String field, java.util.Set<String> healths) {
        if (field == null) return false;
        String[] tokens = field.split("[,;\\s]+");
        for (String token : tokens) {
            for (String health : healths) {
                if (token.trim().equalsIgnoreCase(health.trim())) return true;
            }
        }
        return false;
    }

    // Hàm lọc công thức theo tiêu chí filter
    private void applyRecipeFilter(com.example.bepnhataapp.features.recipes.FilterRecipeBottomSheet.FilterCriteria c) {
        recipeItems.clear();
        for (RecipeEntity entity : allRecipeEntities) {
            boolean ok = true;
            String tag = entity.getTag() != null ? entity.getTag() : "";
            String desc = entity.getDescription() != null ? entity.getDescription() : "";
            String cat = entity.getCategory() != null ? entity.getCategory() : "";
            // Lọc theo Nguyên liệu
            if (c.ingredients != null && !c.ingredients.isEmpty()) {
                boolean found = false;
                for (String ing : c.ingredients) {
                    if (tag.toLowerCase().contains(ing.toLowerCase()) ||
                        desc.toLowerCase().contains(ing.toLowerCase()) ||
                        cat.toLowerCase().contains(ing.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
                if (!found) ok = false;
            }
            // Lọc theo Địa phương
            if (ok && c.regions != null && !c.regions.isEmpty()) {
                boolean found = false;
                for (String region : c.regions) {
                    if (tag.toLowerCase().contains(region.toLowerCase()) ||
                        desc.toLowerCase().contains(region.toLowerCase()) ||
                        cat.toLowerCase().contains(region.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
                if (!found) ok = false;
            }
            // Lọc theo Hương vị
            if (ok && c.flavors != null && !c.flavors.isEmpty()) {
                boolean found = false;
                for (String flavor : c.flavors) {
                    if (tag.toLowerCase().contains(flavor.toLowerCase()) ||
                        desc.toLowerCase().contains(flavor.toLowerCase()) ||
                        cat.toLowerCase().contains(flavor.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
                if (!found) ok = false;
            }
            // Lọc theo Lợi ích sức khỏe (so khớp từng từ)
            if (ok && c.healths != null && !c.healths.isEmpty()) {
                boolean found = false;
                if (matchHealth(tag, c.healths) || matchHealth(desc, c.healths) || matchHealth(cat, c.healths)) {
                    found = true;
                }
                if (!found) ok = false;
            }
            if (ok) {
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
        }
        adapter.notifyDataSetChanged();
    }
}