package com.example.bepnhataapp.features.products;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.example.bepnhataapp.common.base.BaseActivity;
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
import com.example.bepnhataapp.common.adapter.SuggestionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import androidx.annotation.IdRes;

public class ProductDetailActivity extends BaseActivity {

    private ActivityProductDetailBinding binding;

    private int quantity = 1;
    private int basePrice = 0; // giá 1 gói 2 người sau khi trừ sale
    private int currentServingFactor = 1; // 1 cho 2 người, 2 cho 4 người (giá gấp đôi)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product product = null;
            
        // Ưu tiên lấy dữ liệu từ Intent
        if(getIntent().hasExtra("productName")){
            product = new Product();
            product.setProductID(getIntent().getLongExtra("productId", -1));
            product.setProductName(getIntent().getStringExtra("productName"));
            product.setProductPrice(getIntent().getIntExtra("productPrice", 0));
            product.setProductThumb(getIntent().getStringExtra("productThumb"));
            product.setProductDescription(getIntent().getStringExtra("productDescription"));
            product.setSalePercent(getIntent().getIntExtra("salePercent", 0));
        } else if(getIntent().hasExtra("productId")){
            long id = getIntent().getLongExtra("productId",-1);
            if(id!=-1){
                product = new ProductDao(this).getById(id);
            }

            final Product finalProduct = product;
            findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
                com.example.bepnhataapp.common.utils.CartHelper.addProduct(ProductDetailActivity.this, finalProduct, currentServingFactor);
                android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm giỏ hàng thành công", android.widget.Toast.LENGTH_SHORT).show();
            });
        }
        if (product != null) {
            String thumb = product.getProductThumb();
            if (thumb != null && !thumb.isEmpty()) {
                Glide.with(this).load(thumb).placeholder(R.drawable.sample_img).error(R.drawable.sample_img).into(binding.ivProduct);
            } else {
                binding.ivProduct.setImageResource(R.drawable.sample_img);
            }
            binding.tvProductName.setText(product.getProductName());
            binding.tvDescription.setText(product.getProductDescription());
            binding.tvTime.setText("");
            basePrice = product.getProductPrice() * (100 - product.getSalePercent()) / 100;
            updatePriceText();
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

            // Setup ViewPager with two fragments
            ProductDetailPagerAdapter adapter = new ProductDetailPagerAdapter(this, detail, product.getProductID());
            binding.viewPager.setAdapter(adapter);
        } else {
            // still create adapter with null detail to avoid crash
            ProductDetailPagerAdapter adapter = new ProductDetailPagerAdapter(this, null, -1);
            binding.viewPager.setAdapter(adapter);
        }

        // Quantity +/- logic
        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityText();
            updatePriceText();
        });

        binding.btnMinus.setOnClickListener(v -> {
            if(quantity > 1) {
                quantity--;
                updateQuantityText();
                updatePriceText();
            }
        });

        // Serving change logic
        binding.toggleGroupServing.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(isChecked) {
                if(checkedId == R.id.btnServing2) {
                    currentServingFactor = 1;
                } else if(checkedId == R.id.btnServing4) {
                    currentServingFactor = 2;
                }
                updatePriceText();
            }
        });

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

        // Load suggestion products (3 random others)
        List<Product> allProducts = new ProductDao(this).getAll();
        if(product!=null){
            final long curId = product.getProductID();
            allProducts.removeIf(p -> p.getProductID()==curId);
        }
        Collections.shuffle(allProducts);
        List<Product> suggestList = allProducts.size()>3 ? allProducts.subList(0,3) : allProducts;

        SuggestionAdapter suggestAdapter = new SuggestionAdapter(this, suggestList);
        binding.rvSuggestions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSuggestions.setAdapter(suggestAdapter);

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

    @Override
    protected int getBottomNavigationContainerId() {
        return 0; // no bottom navigation in this screen
    }

    private String formatMacro(double value,String label){
        if("calo".equals(label)){
            return (int)value+" Kcal";
        }
        return (int)value+"g "+label;
    }

    private void updateQuantityText() {
        binding.tvQuantity.setText(String.valueOf(quantity));
    }

    private void updatePriceText() {
        int total = basePrice * currentServingFactor * quantity;
        binding.tvPrice.setText(String.valueOf(total));
    }
} 