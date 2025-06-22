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
import com.example.bepnhataapp.common.models.Blog;
import com.example.bepnhataapp.common.model.BlogEntity;
import com.example.bepnhataapp.MyApplication;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.models.Recipe;
import com.example.bepnhataapp.features.home.CookingTipAdapter;
import com.example.bepnhataapp.features.home.RecipeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private ViewPager2 bannerViewPager;
    private TabLayout bannerIndicator;
    private RecyclerView hotIngredientsRecyclerView;
    private RecyclerView recipesGridRecyclerView;
    private RecyclerView cookingTipsRecyclerView;
    private TextView currentSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_home);

        bannerViewPager = findViewById(R.id.banner_view_pager);
        bannerIndicator = findViewById(R.id.banner_indicator);

        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.placeholder_banner_background);
        bannerImages.add(R.drawable.placeholder_banner_background);
        bannerImages.add(R.drawable.placeholder_banner_background);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(bannerIndicator, bannerViewPager, (tab, position) -> {
            // You can set tab text here if needed, but for indicators, it's usually empty
        }).attach();

        // Setup Hot Ingredients RecyclerView
        hotIngredientsRecyclerView = findViewById(R.id.hot_ingredients_recycler_view);
        hotIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<HotIngredient> hotIngredientList = new ArrayList<>();
        hotIngredientList.add(new HotIngredient(R.drawable.placeholder_banner_background, "Bò xào rau củ", "50.000đ"));
        hotIngredientList.add(new HotIngredient(R.drawable.placeholder_banner_background, "Gà chiên giòn", "75.000đ"));
        hotIngredientList.add(new HotIngredient(R.drawable.placeholder_banner_background, "Canh chua cá", "60.000đ"));
        hotIngredientList.add(new HotIngredient(R.drawable.placeholder_banner_background, "Mì Ý sốt bò", "80.000đ"));

        HotIngredientAdapter hotIngredientAdapter = new HotIngredientAdapter(hotIngredientList);
        hotIngredientsRecyclerView.setAdapter(hotIngredientAdapter);

        // Setup Recipe Categories
        setupRecipeCategories();

        // Setup Recipes Grid RecyclerView
        recipesGridRecyclerView = findViewById(R.id.recipes_grid_recycler_view);
        recipesGridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recipesGridRecyclerView.setNestedScrollingEnabled(false);
        loadRecipesAsync();

        // Setup Cooking Tips RecyclerView
        cookingTipsRecyclerView = findViewById(R.id.cooking_tips_recycler_view);
        cookingTipsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cookingTipsRecyclerView.setNestedScrollingEnabled(false);
        loadCookingTipsAsync();

        // Thêm sự kiện click cho nút 'Xem tất cả' phần mẹo vặt bí quyết nấu ăn
        TextView xemTatCaCookingTips = findViewById(R.id.tvSeeAllCookingTips);
        if (xemTatCaCookingTips != null) {
            xemTatCaCookingTips.setOnClickListener(v -> {
                startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.blog.BlogActivity.class));
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure bottom navigation highlights Home when returning from other tabs
        setupBottomNavigationFragment(R.id.nav_home);
    }

    private void setupRecipeCategories() {
        TextView categoryMonNuong = findViewById(R.id.category_mon_nuong);
        TextView categoryMonKho = findViewById(R.id.category_mon_kho);
        TextView categoryMonXao = findViewById(R.id.category_mon_xao);
        TextView categoryMonNuoc = findViewById(R.id.category_mon_nuoc);
        TextView categoryMonChay = findViewById(R.id.category_mon_chay);

        List<TextView> categoryButtons = new ArrayList<>();
        categoryButtons.add(categoryMonNuong);
        categoryButtons.add(categoryMonKho);
        categoryButtons.add(categoryMonXao);
        categoryButtons.add(categoryMonNuoc);
        categoryButtons.add(categoryMonChay);

        // Set initial selected state
        categoryMonNuong.setSelected(true);
        currentSelectedCategory = categoryMonNuong;

        for (TextView button : categoryButtons) {
            button.setOnClickListener(v -> {
                if (currentSelectedCategory != null) {
                    currentSelectedCategory.setSelected(false);
                }
                v.setSelected(true);
                currentSelectedCategory = (TextView) v;
                // Here you would typically filter your recipes based on the selected category
                // For now, we just handle the UI change
            });
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

    private void loadRecipesAsync() {
        new Thread(() -> {
            // Wait until the DB is ready
            while (!MyApplication.isDbReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            RecipeDao recipeDao = new RecipeDao(this);
            List<RecipeEntity> recipeEntities = recipeDao.getAllRecipes();
            final List<Recipe> recipeList = new ArrayList<>();
            if (recipeEntities != null) {
                for (RecipeEntity entity : recipeEntities) {
                    recipeList.add(new Recipe(
                            entity.getRecipeName(),
                            entity.getCategory(),
                            entity.getImageThumb(), // This is now correct
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
                            entity.getTitle(),
                            entity.getContent(),
                            entity.getTag(),
                            entity.getImageThumb(), // Pass the image URL string
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
}