package com.example.bepnhataapp.features.products;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ProductDetailPagerAdapter;
import com.example.bepnhataapp.databinding.ActivityProductDetailBinding;
import com.example.bepnhataapp.common.models.Product;
import com.example.bepnhataapp.common.models.Review;
import com.example.bepnhataapp.common.adapter.ReviewAdapter;
import com.example.bepnhataapp.common.adapter.SuggestionAdapter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            binding.ivProduct.setImageResource(product.imageResId);
            binding.tvProductName.setText(product.name);
            binding.tvTime.setText(product.time);
            binding.tvPrice.setText(product.price.replace("đ", "")); // show numeric price
            // Additional binding like kcal, nutrition, etc. can be done here.
        }

        // Setup ViewPager with two fragments
        ProductDetailPagerAdapter adapter = new ProductDetailPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        // Demo reviews & suggestions data
        List<Integer> sampleImgs = new ArrayList<>();
        sampleImgs.add(R.drawable.food);
        sampleImgs.add(R.drawable.food);
        sampleImgs.add(R.drawable.food);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review(R.drawable.profile_placeholder, "Đức Mạnh", 5f, "2 giờ trước", "Nguyên liệu được đóng gói rất chỉnh chu. Món ăn rất ngon! Cả gia đình tôi đều thích!", sampleImgs));
        reviewList.add(new Review(R.drawable.profile_placeholder, "Kiên Đoàn", 5f, "2 giờ trước", "Tôi đã nấu thử và rất thành công món bánh canh cua...", sampleImgs));

        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);

        List<Product> suggestList = new ArrayList<>();
        suggestList.add(new Product(R.drawable.food, "Sườn non kho tiêu", "700 cal", "Giàu đạm", "50 phút", "2 người", "4 người", "", "70.000đ", 4.5f, 100, false));
        suggestList.add(new Product(R.drawable.food, "Sườn nướng mật ong", "800 cal", "Giàu đạm", "60 phút", "2 người", "4 người", "", "80.000đ", 4.5f, 120, false));
        suggestList.add(new Product(R.drawable.food, "Bò lúc lắc", "650 cal", "Giàu đạm", "30 phút", "2 người", "4 người", "", "80.000đ", 4.5f, 140, false));

        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(this, suggestList);
        binding.rvSuggestions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSuggestions.setAdapter(suggestionAdapter);

        MaterialButtonToggleGroup toggleGroup = binding.toggleGroupTab;
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(isChecked) {
                if(checkedId == R.id.btnTabIngredient) {
                    binding.viewPager.setCurrentItem(0);
                } else if(checkedId == R.id.btnTabGuide) {
                    binding.viewPager.setCurrentItem(1);
                }
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                if(position==0) toggleGroup.check(R.id.btnTabIngredient);
                else toggleGroup.check(R.id.btnTabGuide);
            }
        });

        binding.tvAllReviews.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, AllReviewsActivity.class);
            startActivity(intent);
        });
    }
} 