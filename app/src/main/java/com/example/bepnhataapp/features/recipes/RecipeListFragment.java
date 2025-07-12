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
import android.widget.PopupMenu;
import android.view.MenuItem;

import com.example.bepnhataapp.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.adapter.CategoryAdapter;
import com.example.bepnhataapp.common.model.Category;
import androidx.annotation.NonNull;

public class RecipeListFragment extends Fragment {

    private java.util.List<RecipeEntity> allRecipeEntities = new java.util.ArrayList<>();
    private java.util.List<RecipeItem> recipeItems = new java.util.ArrayList<>();
    private RecipeAdapter adapter;
    private com.example.bepnhataapp.features.recipes.FilterRecipeBottomSheet.FilterCriteria currentCriteria;
    private boolean isSortAZ = true;
    private java.util.List<RecipeItem> allRecipeItems = new java.util.ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipe_list, container, false);
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
        // Remove automatic top inset padding to keep category bar flush with header
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom); // ignore top inset
            return insets;
        });
        // Khởi tạo RecyclerView và setAdapter
        RecyclerView rvRecipe = view.findViewById(R.id.rvRecipe);
        rvRecipe.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách công thức từ database
        allRecipeEntities = new RecipeDao(getContext()).getAllRecipes();
        recipeItems = new java.util.ArrayList<>();
        com.example.bepnhataapp.common.dao.RecipeDetailDao detailDao = new com.example.bepnhataapp.common.dao.RecipeDetailDao(getContext());

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

            // Lấy dữ liệu chi tiết để hiển thị thời gian, lợi ích và độ khó
            com.example.bepnhataapp.common.model.RecipeDetail detail = detailDao.get(entity.getRecipeID());
            String timeStr = detail != null ? detail.getCookingTimeMinutes() + " phút" : "";
            String benefitStr = detail != null ? detail.getBenefit() : "";
            String levelStr = detail != null ? detail.getLevel() : "";

            RecipeItem item = new RecipeItem(
                imageResId,
                imgStr,
                imgData,
                entity.getRecipeName(),
                "", // calories (not used)
                levelStr, // we temporarily store level in protein field for compatibility
                timeStr,
                entity.getCategory() != null ? entity.getCategory() : ""
            );
            item.setBenefit(benefitStr);
            item.setLevel(levelStr);
            item.setTime(timeStr);
            item.setLikeCount(entity.getLikeAmount());
            item.setCommentCount(entity.getCommentAmount());
            recipeItems.add(item);
        }
        // Keep a master copy for category filtering
        allRecipeItems = new java.util.ArrayList<>(recipeItems);

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

        // Khởi tạo RecyclerView danh mục dùng lại adapter của trang nguyên liệu
        RecyclerView rvCategories = view.findViewById(R.id.rvCategories);
        View categoryContainer = view.findViewById(R.id.layoutCategoriesContainer);
        if (rvCategories != null) {
            rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            java.util.Set<String> categorySet = new java.util.HashSet<>();
            for (RecipeEntity entity : allRecipeEntities) {
                String cat = entity.getCategory();
                if (cat != null && !cat.trim().isEmpty()) {
                    categorySet.add(cat.trim());
                }
            }
            java.util.List<Category> categories = new java.util.ArrayList<>();
            // Always have the "All" option first
            categories.add(new Category(R.drawable.cate_product_all, "Tất cả"));
            for (String cat : categorySet) {
                int iconRes = getCategoryIconRes(cat);
                String displayName = getDisplayCategoryName(cat);
                categories.add(new Category(iconRes, displayName));
            }
            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
            rvCategories.setAdapter(categoryAdapter);
            categoryAdapter.setOnCategoryClickListener(categoryName -> {
                // Lọc công thức theo danh mục
                java.util.List<RecipeItem> filtered = new java.util.ArrayList<>();
                if ("Tất cả".equalsIgnoreCase(categoryName)) {
                    filtered.addAll(allRecipeItems);
                } else {
                    String targetKey = getCategoryKey(categoryName);
                    for (RecipeItem item : allRecipeItems) {
                        if (item.getCategory() != null && getCategoryKey(item.getCategory()).equals(targetKey)) {
                            filtered.add(item);
                        }
                    }
                }
                recipeItems.clear();
                recipeItems.addAll(filtered);
                adapter.notifyDataSetChanged();
            });
        }

        // Ẩn/hiện thanh danh mục khi cuộn (tương tự ProductActivity)
        final boolean[] isCategoryVisible = {true};
        rvRecipe.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (categoryContainer == null) return;

                // Cuộn xuống: ẩn
                if (dy > 10 && isCategoryVisible[0]) {
                    isCategoryVisible[0] = false;
                    categoryContainer.animate()
                            .translationY(-categoryContainer.getHeight())
                            .alpha(0f)
                            .setInterpolator(new android.view.animation.AccelerateInterpolator())
                            .setDuration(250)
                            .withEndAction(() -> categoryContainer.setVisibility(View.GONE));
                }

                // Cuộn lên: hiện
                if (dy < -10 && !isCategoryVisible[0]) {
                    isCategoryVisible[0] = true;
                    categoryContainer.setVisibility(View.VISIBLE);
                    categoryContainer.setAlpha(0f);
                    categoryContainer.setTranslationY(-categoryContainer.getHeight());
                    categoryContainer.animate()
                            .translationY(0)
                            .alpha(1f)
                            .setInterpolator(new android.view.animation.DecelerateInterpolator())
                            .setDuration(250)
                            .start();
                }
            }
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
            com.example.bepnhataapp.common.model.RecipeDetail detail = new com.example.bepnhataapp.common.dao.RecipeDetailDao(getContext()).get(entity.getRecipeID());
            String flavorStr = detail != null ? detail.getFlavor() : "";
            String benefitStr = detail != null ? detail.getBenefit() : "";
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
            // Lọc theo Hương vị (so sánh trực tiếp flavor)
            if (ok && c.flavors != null && !c.flavors.isEmpty()) {
                boolean found = false;
                for (String flavor : c.flavors) {
                    if (flavorStr != null && flavorStr.toLowerCase().contains(flavor.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
                if (!found) ok = false;
            }
            // Lọc theo Lợi ích sức khỏe (so sánh trực tiếp benefit)
            if (ok && c.healths != null && !c.healths.isEmpty()) {
                boolean found = false;
                for (String health : c.healths) {
                    if (benefitStr != null && benefitStr.toLowerCase().contains(health.toLowerCase())) {
                        found = true;
                        break;
                    }
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
                String timeStr = detail != null ? detail.getCookingTimeMinutes() + " phút" : "";
                String levelStr = detail != null ? detail.getLevel() : "";

                RecipeItem item = new RecipeItem(
                    imageResId,
                    imgStr,
                    imgData,
                    entity.getRecipeName(),
                    "", // calories (not used)
                    levelStr, // we temporarily store level in protein field for compatibility
                    timeStr,
                    entity.getCategory() != null ? entity.getCategory() : ""
                );
                item.setBenefit(benefitStr);
                item.setLevel(levelStr);
                item.setTime(timeStr);
                item.setLikeCount(entity.getLikeAmount());
                item.setCommentCount(entity.getCommentAmount());
                recipeItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private int getCategoryIconRes(String categoryName) {
        String key = unaccent(categoryName);
        if (key.contains("nuong")) return R.drawable.cate_product_grill;
        if (key.contains("kho")) return R.drawable.cate_product_kho;
        if (key.contains("xao")) return R.drawable.cate_product_stir;
        if (key.contains("nuoc")) return R.drawable.cate_product_noodle;
        if (key.contains("chay")) return R.drawable.cate_product_vegetarian;
        // fallback
        return R.drawable.cate_product_all;
    }

    // Convert raw category slug from DB (e.g., "nuong") to display string "Món nướng"
    private String getDisplayCategoryName(String raw) {
        if (raw == null) return "";
        String norm = unaccent(raw).trim();
        // giữ lại accent nếu có sẵn
        String base = raw.trim();
        // Nếu đã có tiền tố "Món" thì trả nguyên
        if (normalize("mon").equals(normalize(base.split(" ")[0]))) {
            return base;
        }
        return "Món " + base;
    }

    // Lấy "slug" (không dấu, không tiền tố "món") để so sánh
    private String getCategoryKey(String name) {
        String key = unaccent(name).trim();
        if (key.startsWith("mon ")) {
            key = key.substring(4); // remove "mon "
        }
        return key.trim();
    }

    private String normalize(String str) {
        return unaccent(str).trim().toLowerCase();
    }

    // Utility to remove Vietnamese accents & lowercase for insensitive compare
    private String unaccent(String str) {
        if (str == null) return "";
        String n = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        return n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }
}
