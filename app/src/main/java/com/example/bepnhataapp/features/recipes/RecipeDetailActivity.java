package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.dao.RecipeDownloadDao;
import com.example.bepnhataapp.common.dao.RecipeIngredientDao;
import com.example.bepnhataapp.common.dao.IngredientDao;
import com.example.bepnhataapp.common.dao.InstructionRecipeDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.model.RecipeDownload;
import com.example.bepnhataapp.common.model.RecipeIngredient;
import com.example.bepnhataapp.common.model.Ingredient;
import com.example.bepnhataapp.common.model.InstructionRecipe;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecipeDetailActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        long recipeId = getIntent().hasExtra("recipeId") ? getIntent().getLongExtra("recipeId", -1) : getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        ImageView img = findViewById(R.id.imvRecipe);
        TextView tvName = findViewById(R.id.txtName);
        TextView tvCategory = findViewById(R.id.tvRecipeCategory);
        TextView tvCaloTime = findViewById(R.id.tvRecipeCaloTime);
        ImageView imvDownload = findViewById(R.id.imvDowload);

        // Load recipe entity
        RecipeEntity entity = new RecipeDao(this).getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == recipeId)
                .findFirst()
                .orElse(null);
        if (entity != null) {
            tvName.setText(entity.getRecipeName());
            tvCategory.setText(entity.getCategory());
            Object source;
            String imgStr = entity.getImageThumb() != null ? entity.getImageThumb().trim() : "";
            if (imgStr.isEmpty()) {
                source = R.drawable.placeholder_banner_background;
            } else if (imgStr.startsWith("http")) {
                source = imgStr;
            } else {
                int resId = getResources().getIdentifier(imgStr, "drawable", getPackageName());
                source = resId != 0 ? resId : R.drawable.placeholder_banner_background;
            }
            Glide.with(this).load(source).placeholder(R.drawable.placeholder_banner_background).into(img);
        }

        RecipeDetail detail = new RecipeDetailDao(this).get(recipeId);
        if (detail != null) {
            String str = String.format("%d Kcal • %d Min", (int) detail.getCalo(), detail.getCookingTimeMinutes());
            tvCaloTime.setText(str);
        }

        imvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy customerID động
                String phone = SessionManager.getPhone(RecipeDetailActivity.this);
                if (phone == null) {
                    Toast.makeText(RecipeDetailActivity.this, "Bạn cần đăng nhập để tải công thức!", Toast.LENGTH_SHORT).show();
                    return;
                }
                CustomerDao customerDao = new CustomerDao(RecipeDetailActivity.this);
                Customer customer = customerDao.findByPhone(phone);
                if (customer == null) {
                    Toast.makeText(RecipeDetailActivity.this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
                    return;
                }
                long customerId = customer.getCustomerID();
                RecipeDownloadDao dao = new RecipeDownloadDao(RecipeDetailActivity.this);
                // Kiểm tra đã tải chưa
                if (dao.get(customerId, recipeId) != null) {
                    Toast.makeText(RecipeDetailActivity.this, "Công thức đã được tải về trước đó!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RecipeDownload rd = new RecipeDownload();
                rd.setCustomerID(customerId);
                rd.setRecipeID(recipeId);
                String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                rd.setDownloadedAt(now);
                long result = dao.insert(rd);
                if (result != -1) {
                    Toast.makeText(RecipeDetailActivity.this, "Đã lưu công thức để xem offline!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Lỗi khi lưu công thức!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Hiển thị nguyên liệu
        LinearLayout layoutIngredients = findViewById(R.id.layoutIngredients);
        RecyclerView rcStepGuide = findViewById(R.id.rcStepGuide);
        MaterialButtonToggleGroup toggleGroupTab = findViewById(R.id.toggleGroupTab);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        RecipeDetailPagerAdapter pagerAdapter = new RecipeDetailPagerAdapter(this, recipeId);
        viewPager.setAdapter(pagerAdapter);
        // Đồng bộ tab và page
        toggleGroupTab.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTabIngredient) {
                    viewPager.setCurrentItem(0);
                } else if (checkedId == R.id.btnTabGuide) {
                    viewPager.setCurrentItem(1);
                }
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) toggleGroupTab.check(R.id.btnTabIngredient);
                else toggleGroupTab.check(R.id.btnTabGuide);
            }
        });

        // bottom nav
        setupBottomNavigationFragment(R.id.nav_recipes);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
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
} 
