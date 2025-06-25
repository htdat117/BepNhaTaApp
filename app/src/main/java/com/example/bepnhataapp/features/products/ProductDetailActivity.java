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
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.models.Review;
import com.example.bepnhataapp.common.adapter.ReviewAdapter;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.bumptech.glide.Glide;
import android.graphics.BitmapFactory;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.dao.ProductDetailDao;
import com.example.bepnhataapp.common.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product product = null;
        if(getIntent().hasExtra("productId")){
            long id = getIntent().getLongExtra("productId",-1);
            if(id!=-1){
                product = new ProductDao(this).getById(id);
            }
        }
        if (product != null) {
            String thumb = product.getProductThumb();
            if (thumb != null && !thumb.isEmpty()) {
                Glide.with(this).load(thumb).placeholder(R.drawable.sample_img).error(R.drawable.sample_img).into(binding.ivProduct);
            } else {
                binding.ivProduct.setImageResource(R.drawable.sample_img);
            }
            binding.tvProductName.setText(product.getProductName());
            binding.tvTime.setText("");
            int price = product.getProductPrice() * (100 - product.getSalePercent()) / 100;
            binding.tvPrice.setText(String.valueOf(price));
            // Additional binding like kcal, nutrition, etc. can be done here.

            // Load detail
            ProductDetail detail = new ProductDetailDao(this).getByProductId(product.getProductID());
            if(detail!=null){
                ((android.widget.TextView)findViewById(R.id.tvCarbs)).setText(formatMacro(detail.getCarbs(),"carbs"));
                ((android.widget.TextView)findViewById(R.id.tvProtein)).setText(formatMacro(detail.getProtein(),"proteins"));
                ((android.widget.TextView)findViewById(R.id.tvCalo)).setText(formatMacro(detail.getCalo(),"calo"));
                ((android.widget.TextView)findViewById(R.id.tvFat)).setText(formatMacro(detail.getFat(),"fat"));
                binding.tvTime.setText(detail.getCookingTimeMinutes()+" phút");
            }
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

        // TODO load suggestion products from DB if needed

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

    private String formatMacro(double value,String label){
        return (int)value+"g "+label;
    }
} 