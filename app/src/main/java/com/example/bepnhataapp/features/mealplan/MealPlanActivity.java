package com.example.bepnhataapp.features.mealplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.repository.LocalMealPlanRepository;
import java.time.LocalDate;

public class MealPlanActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initial login check removed to allow viewing onboarding screen

        // Nếu đã hoàn thành khảo sát hoặc tạo thực đơn, chuyển thẳng vào nội dung kế hoạch bữa ăn
        SharedPreferences prefs = getSharedPreferences("MealPlanPrefs", MODE_PRIVATE);

        // Nếu người dùng đã hoàn thành onboarding VÀ đang đăng nhập => chuyển thẳng tới nội dung kế hoạch.
        // Ngược lại (chưa đăng nhập) vẫn hiển thị màn hình giới thiệu để họ có thể cung cấp thông tin/click tạo thủ công.
        if (prefs.getBoolean("onboarding_completed", false) && SessionManager.isLoggedIn(this)) {
            startActivity(new Intent(this, MealPlanContentActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_meal_plan);

        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_meal_plan);

        // Setup the "Điền thông tin" button click listener with login requirement
        MaterialButton btnFillInfo = findViewById(R.id.btnFillInfo);
        btnFillInfo.setOnClickListener(v -> {
            if (!SessionManager.isLoggedIn(MealPlanActivity.this)) {
                startActivity(new Intent(MealPlanActivity.this, com.example.bepnhataapp.features.login.LoginActivity.class));
            } else {
                startActivity(new Intent(MealPlanActivity.this, MealPlanWizardActivity.class));
            }
        });

        // Setup "Tạo thực đơn ngay" button listener with login requirement
        Button btnCreateMenu = findViewById(R.id.btnCreateMenuNow);
        btnCreateMenu.setOnClickListener(v -> {
            if (!SessionManager.isLoggedIn(MealPlanActivity.this)) {
                startActivity(new Intent(MealPlanActivity.this, com.example.bepnhataapp.features.login.LoginActivity.class));
            } else {
                // Tạo thực đơn tự động cho ngày hiện tại và chuyển sang màn hình nội dung
                LocalMealPlanRepository repo = new LocalMealPlanRepository(MealPlanActivity.this);
                repo.generateWeekPlan(LocalDate.now());

                prefs.edit().putBoolean("onboarding_completed", true).apply();

                startActivity(new Intent(MealPlanActivity.this, MealPlanContentActivity.class));
            }
        });

        // ---------------- Popular recipes section ----------------
        android.widget.ImageView imgPop1 = findViewById(R.id.imgPopular1);
        android.widget.ImageView imgPop2 = findViewById(R.id.imgPopular2);
        android.widget.TextView tvTitle1 = findViewById(R.id.tvTitlePopular1);
        android.widget.TextView tvTitle2 = findViewById(R.id.tvTitlePopular2);
        android.widget.TextView tvKcal1 = findViewById(R.id.tvKcalPopular1);
        android.widget.TextView tvKcal2 = findViewById(R.id.tvKcalPopular2);
        android.widget.TextView tvTime1 = findViewById(R.id.tvTimePopular1);
        android.widget.TextView tvTime2 = findViewById(R.id.tvTimePopular2);

        new Thread(() -> {
            // wait db ready
            while(!com.example.bepnhataapp.MyApplication.isDbReady){
                try{Thread.sleep(100);}catch(InterruptedException e){Thread.currentThread().interrupt();}
            }
            com.example.bepnhataapp.common.dao.RecipeDao rDao = new com.example.bepnhataapp.common.dao.RecipeDao(this);
            java.util.List<com.example.bepnhataapp.common.model.RecipeEntity> recipes = rDao.getAllRecipes();
            // sort by likeAmount desc (popular)
            recipes.sort((a,b)->Integer.compare(b.getLikeAmount(), a.getLikeAmount()));
            if(recipes.size()>2) recipes = recipes.subList(0,2);
            com.example.bepnhataapp.common.dao.RecipeDetailDao detailDao = new com.example.bepnhataapp.common.dao.RecipeDetailDao(this);
            java.util.List<com.example.bepnhataapp.common.model.RecipeDetail> details = new java.util.ArrayList<>();
            for(com.example.bepnhataapp.common.model.RecipeEntity re: recipes){
                com.example.bepnhataapp.common.model.RecipeDetail d = detailDao.get(re.getRecipeID());
                details.add(d);
            }
            final java.util.List<com.example.bepnhataapp.common.model.RecipeEntity> recipesFinal = recipes;
            final java.util.List<com.example.bepnhataapp.common.model.RecipeDetail> detailsFinal = details;
            runOnUiThread(() -> {
                if(recipesFinal.size()>0){
                    com.bumptech.glide.Glide.with(this).load(recipesFinal.get(0).getImageThumb()).placeholder(R.drawable.placeholder_banner_background).into(imgPop1);
                    tvTitle1.setText(recipesFinal.get(0).getRecipeName());
                    if(detailsFinal.get(0)!=null){
                        tvKcal1.setText((int)detailsFinal.get(0).getCalo()+" Kcal");
                        tvTime1.setText(detailsFinal.get(0).getCookingTimeMinutes()+" Min");
                    }
                }
                if(recipesFinal.size()>1){
                    com.bumptech.glide.Glide.with(this).load(recipesFinal.get(1).getImageThumb()).placeholder(R.drawable.placeholder_banner_background).into(imgPop2);
                    tvTitle2.setText(recipesFinal.get(1).getRecipeName());
                    if(detailsFinal.get(1)!=null){
                        tvKcal2.setText((int)detailsFinal.get(1).getCalo()+" Kcal");
                        tvTime2.setText(detailsFinal.get(1).getCookingTimeMinutes()+" Min");
                    }
                }
            });
        }).start();

        android.widget.TextView tvSeeAllPopular = findViewById(R.id.tvSeeAllPopular);
        if(tvSeeAllPopular!=null){
            tvSeeAllPopular.setOnClickListener(v->{
                if(!SessionManager.isLoggedIn(MealPlanActivity.this)){
                    startActivity(new Intent(MealPlanActivity.this, com.example.bepnhataapp.features.login.LoginActivity.class));
                }else{
                    startActivity(new Intent(MealPlanActivity.this, com.example.bepnhataapp.features.recipes.RecipesActivity.class));
                }
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
}
