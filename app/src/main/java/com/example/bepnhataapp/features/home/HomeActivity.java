package com.example.bepnhataapp.features.home;

import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.TextView;
import android.view.View;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.common.dao.BlogDao;
import com.example.bepnhataapp.common.model.Blog;
import com.example.bepnhataapp.common.model.BlogEntity;
import com.example.bepnhataapp.MyApplication;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.Recipe;
import com.example.bepnhataapp.features.home.CookingTipAdapter;
import com.example.bepnhataapp.features.home.RecipeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.model.Product;
import android.widget.ImageView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private ViewPager2 bannerViewPager;
    private TabLayout bannerIndicator;
    private RecyclerView hotIngredientsRecyclerView;
    private RecyclerView recipesGridRecyclerView;
    private RecyclerView cookingTipsRecyclerView;
    private TextView currentSelectedCategory;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_home);

        bannerViewPager = findViewById(R.id.banner_view_pager);
        bannerIndicator = findViewById(R.id.banner_indicator);

        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner1);
        bannerImages.add(R.drawable.banner2);
        bannerImages.add(R.drawable.banner3);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(bannerIndicator, bannerViewPager, (tab, position) -> {
            // You can set tab text here if needed, but for indicators, it's usually empty
        }).attach();

        // Setup Hot Ingredients RecyclerView
        hotIngredientsRecyclerView = findViewById(R.id.hot_ingredients_recycler_view);
        hotIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        
        // Set empty adapter initially to prevent "No adapter attached" error
        HotIngredientAdapter emptyHotAdapter = new HotIngredientAdapter(new ArrayList<>());
        hotIngredientsRecyclerView.setAdapter(emptyHotAdapter);

        // Click "Xem tất cả" Hot Ingredients
        TextView tvSeeAllHot = findViewById(R.id.tvSeeAllHotIngredients);
        if(tvSeeAllHot!=null){
            tvSeeAllHot.setOnClickListener(v -> {
                // mở ProductActivity, có thể cuộn đến danh mục khuyến mãi/hot
                startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.products.ProductActivity.class));
            });
        }

        loadHotIngredientsAsync();

        // Setup Recipe Categories (dynamic)
        loadCategoriesAsync();

        // After category dynamic section, before recipe grid maybe create TextView; near where categories built we can set listener earlier in onCreate.
        TextView tvSeeAllRecipes = findViewById(R.id.tvSeeAllRecipes);
        if(tvSeeAllRecipes!=null){
            tvSeeAllRecipes.setOnClickListener(v -> {
                startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.recipes.RecipesActivity.class));
            });
        }

        // Setup Recipes Grid RecyclerView
        recipesGridRecyclerView = findViewById(R.id.recipes_grid_recycler_view);
        recipesGridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recipesGridRecyclerView.setNestedScrollingEnabled(false);
        
        // Set empty adapter initially to prevent "No adapter attached" error
        RecipeAdapter emptyRecipeAdapter = new RecipeAdapter(new ArrayList<>());
        recipesGridRecyclerView.setAdapter(emptyRecipeAdapter);
        
        // Recipes sẽ được load sau khi categories sẵn sàng.

        // Setup Cooking Tips RecyclerView
        cookingTipsRecyclerView = findViewById(R.id.cooking_tips_recycler_view);
        cookingTipsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cookingTipsRecyclerView.setNestedScrollingEnabled(false);
        
        // Set empty adapter initially to prevent "No adapter attached" error
        CookingTipAdapter emptyCookingAdapter = new CookingTipAdapter(this, new ArrayList<>());
        cookingTipsRecyclerView.setAdapter(emptyCookingAdapter);
        
        loadCookingTipsAsync();

        // Thêm sự kiện click cho nút 'Xem tất cả' phần mẹo vặt bí quyết nấu ăn
        TextView xemTatCaCookingTips = findViewById(R.id.tvSeeAllCookingTips);
        if (xemTatCaCookingTips != null) {
            xemTatCaCookingTips.setOnClickListener(v -> {
                startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.blog.BlogActivity.class));
            });
        }

        // Thêm sự kiện click cho banner lên thực đơn
        ImageView banner = findViewById(R.id.home_banner);
        if (banner != null) {
            banner.setOnClickListener(v -> {
                startActivity(new Intent(this, com.example.bepnhataapp.features.mealplan.MealPlanActivity.class));
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure bottom navigation highlights Home when returning from other tabs
        setupBottomNavigationFragment(R.id.nav_home);
    }

    /**
     * Tải danh sách category từ DB và hiển thị thành button động.
     */
    private void loadCategoriesAsync() {
        new Thread(() -> {
            while (!MyApplication.isDbReady) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            RecipeDao dao = new RecipeDao(this);
            List<String> categories = dao.getAllCategories();

            runOnUiThread(() -> buildCategoryButtons(categories));
        }).start();
    }

    private void buildCategoryButtons(List<String> categories) {
        LinearLayout container = findViewById(R.id.recipe_categories_container);
        if (container == null) return;

        container.removeAllViews();

        if (categories == null || categories.isEmpty()) return;

        LayoutInflater inflater = LayoutInflater.from(this);

        for (String cat : categories) {
            TextView tv = (TextView) inflater.inflate(R.layout.item_category_button, container, false);
            tv.setText(cat);

            tv.setOnClickListener(v -> {
                if (currentSelectedCategory != null) {
                    currentSelectedCategory.setSelected(false);
                }
                v.setSelected(true);
                currentSelectedCategory = (TextView) v;
                selectedCategory = tv.getText().toString();
                loadRecipesForCategory(selectedCategory);
            });

            container.addView(tv);

            if (currentSelectedCategory == null) {
                tv.setSelected(true);
                currentSelectedCategory = tv;
                selectedCategory = tv.getText().toString();
                loadRecipesForCategory(selectedCategory); // initial load
            }
        }
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void loadRecipesForCategory(String category) {
        new Thread(() -> {
            while (!MyApplication.isDbReady) {
                try { Thread.sleep(100);} catch (InterruptedException e){Thread.currentThread().interrupt();}
            }

            RecipeDao recipeDao = new RecipeDao(this);
            List<RecipeEntity> recipeEntities;
            if (category == null) {
                recipeEntities = recipeDao.getAllRecipes();
            } else {
                recipeEntities = recipeDao.getRecipesByCategory(category, 4);
            }
            final List<Recipe> recipeList = new ArrayList<>();
            if (recipeEntities != null) {
                for (RecipeEntity entity : recipeEntities) {
                    recipeList.add(new com.example.bepnhataapp.common.model.Recipe(
                            entity.getRecipeID(),
                            entity.getRecipeName(),
                            entity.getCategory(),
                            entity.getImageThumb() != null ? entity.getImageThumb().trim() : "",
                            false
                    ));
                }
            }

            runOnUiThread(() -> {
                if (!isFinishing()) {
                    RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList);
                    recipesGridRecyclerView.setAdapter(recipeAdapter);
                }
            });
        }).start();
    }

    private void loadCookingTipsAsync() {
        new Thread(() -> {
            // Wait until the DB is ready
            while (!MyApplication.isDbReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            BlogDao blogDao = new BlogDao(this);
            List<BlogEntity> blogEntities = blogDao.getRandomBlogs(3);
            final List<Blog> blogList = new ArrayList<>();
            if (blogEntities != null) {
                for (BlogEntity entity : blogEntities) {
                    blogList.add(new Blog(
                            entity.getBlogID(),
                            entity.getTitle(),
                            entity.getContent(),
                            entity.getTag(),
                            entity.getImageThumb() != null ? entity.getImageThumb().trim() : "",
                            false,
                            entity.getCreatedAt(),
                            0,
                            0
                    ));
                }
            }

            runOnUiThread(() -> {
                if (!isFinishing()) {
                    CookingTipAdapter cookingTipAdapter = new CookingTipAdapter(this, blogList);
                    cookingTipsRecyclerView.setAdapter(cookingTipAdapter);
                }
            });
        }).start();
    }

    private void loadHotIngredientsAsync() {
        new Thread(() -> {
            while (!MyApplication.isDbReady) {
                try { Thread.sleep(100);} catch (InterruptedException e){Thread.currentThread().interrupt();}
            }

            ProductDao productDao = new ProductDao(this);
            List<com.example.bepnhataapp.common.model.Product> products = productDao.getHotProducts(4);
            final List<Product> list = new ArrayList<>();
            for (com.example.bepnhataapp.common.model.Product p : products) {
                list.add(p);
            }

            runOnUiThread(() -> {
                if (!isFinishing()) {
                    HotIngredientAdapter hotIngredientAdapter = new HotIngredientAdapter(list);
                    hotIngredientsRecyclerView.setAdapter(hotIngredientAdapter);
                }
            });
        }).start();
    }
}
