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

    private ProductDetail detail;
    private ProductDetailPagerAdapter pagerAdapter;
    private Product currentProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentProduct = null;
            
        // Ưu tiên lấy dữ liệu từ Intent
        if(getIntent().hasExtra("productName")){
            currentProduct = new Product();
            currentProduct.setProductID(getIntent().getLongExtra("productId", -1));
            currentProduct.setProductName(getIntent().getStringExtra("productName"));
            currentProduct.setProductPrice(getIntent().getIntExtra("productPrice", 0));
            currentProduct.setProductThumb(getIntent().getStringExtra("productThumb"));
            currentProduct.setProductDescription(getIntent().getStringExtra("productDescription"));
            currentProduct.setSalePercent(getIntent().getIntExtra("salePercent", 0));
        } else if(getIntent().hasExtra("productId")){
            long id = getIntent().getLongExtra("productId",-1);
            if(id!=-1){
                currentProduct = new ProductDao(this).getById(id);
            }

            final Product finalProduct = currentProduct;
            findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
                com.example.bepnhataapp.common.utils.CartHelper.addProduct(ProductDetailActivity.this, finalProduct, currentServingFactor);
                android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm giỏ hàng thành công", android.widget.Toast.LENGTH_SHORT).show();
            });
        }
        if (currentProduct != null) {
            String thumb = currentProduct.getProductThumb();
            if (thumb != null && !thumb.isEmpty()) {
                Glide.with(this).load(thumb).placeholder(R.drawable.sample_img).error(R.drawable.sample_img).into(binding.ivProduct);
            } else {
                binding.ivProduct.setImageResource(R.drawable.sample_img);
            }
            binding.tvProductName.setText(currentProduct.getProductName());
            binding.tvDescription.setText(currentProduct.getProductDescription());
            binding.tvTime.setText("");
            updatePriceText();
            // Additional binding like kcal, nutrition, etc. can be done here.

            // Load detail
            detail = new ProductDetailDao(this).getByProductId(currentProduct.getProductID());
            refreshMacroAndTime();

            // Setup ViewPager with two fragments
            pagerAdapter = new ProductDetailPagerAdapter(this, detail, currentProduct.getProductID(), currentServingFactor);
            binding.viewPager.setAdapter(pagerAdapter);
        } else {
            // still create adapter with null detail to avoid crash
            pagerAdapter = new ProductDetailPagerAdapter(this, null, -1, currentServingFactor);
            binding.viewPager.setAdapter(pagerAdapter);
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
                refreshMacroAndTime();
                if(pagerAdapter!=null) pagerAdapter.updateServingFactor(currentServingFactor);
            }
        });

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        // Demo reviews & suggestions data
        List<Review> reviewList = new ArrayList<>();
        if(currentProduct!=null){
            reviewList = new com.example.bepnhataapp.common.dao.ProductFeedbackDao(this).getReviewsByProductId(currentProduct.getProductID());
        }

        if(reviewList.isEmpty()){
            java.util.List<Integer> sampleImgsRes = new java.util.ArrayList<>();
            sampleImgsRes.add(R.drawable.food);
            reviewList.add(new Review(R.drawable.profile_placeholder, "Chưa có đánh giá", 0f, "", "Hãy là người đầu tiên đánh giá sản phẩm này", sampleImgsRes));
        }

        // Chỉ hiển thị tối đa 2 đánh giá gần nhất
        List<Review> displayedReviews = reviewList.size() > 2 ? reviewList.subList(0, 2) : reviewList;

        ReviewAdapter reviewAdapter = new ReviewAdapter(this, displayedReviews);
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);

        // update review header count
        android.widget.TextView tvHeaderRev = findViewById(R.id.tvReviewHeader);
        if(tvHeaderRev!=null){
            tvHeaderRev.setText("Đánh giá sản phẩm ("+reviewList.size()+")");
        }

        // Load suggestion products (3 random others)
        List<Product> allProducts = new ProductDao(this).getAll();
        if(currentProduct!=null){
            final long curId = currentProduct.getProductID();
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
            if(currentProduct != null){
                intent.putExtra("productId", currentProduct.getProductID());
            }
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
        if(currentProduct==null) return;

        int originalPricePerPack;
        int salePercent;
        if(currentServingFactor==1){
            originalPricePerPack = currentProduct.getProductPrice2();
            salePercent = currentProduct.getSalePercent2();
        } else {
            originalPricePerPack = currentProduct.getProductPrice4();
            salePercent = currentProduct.getSalePercent4();
        }

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));

        int discountedPricePerPack = originalPricePerPack * (100 - salePercent) / 100;

        int totalDiscounted = discountedPricePerPack * quantity;
        binding.tvPrice.setText(nf.format(totalDiscounted)+"đ");

        // Old price handling
        if(salePercent > 0){
            int totalOriginal = originalPricePerPack * quantity;
            binding.tvOldPrice.setVisibility(android.view.View.VISIBLE);
            binding.tvOldPrice.setText(nf.format(totalOriginal)+"đ");
            // strike through
            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.tvOldPrice.setVisibility(android.view.View.GONE);
        }
    }

    private void refreshMacroAndTime(){
        if(detail==null) return;
        int factor = currentServingFactor;
        double carbs = factor==1 ? (detail.getCarbs2()!=0?detail.getCarbs2():detail.getCarbs()) : (detail.getCarbs4()!=0?detail.getCarbs4():detail.getCarbs2()*2);
        double protein = factor==1 ? (detail.getProtein2()!=0?detail.getProtein2():detail.getProtein()) : (detail.getProtein4()!=0?detail.getProtein4():detail.getProtein2()*2);
        double fat = factor==1 ? (detail.getFat2()!=0?detail.getFat2():detail.getFat()) : (detail.getFat4()!=0?detail.getFat4():detail.getFat2()*2);
        double calo = factor==1 ? (detail.getCalo2()!=0?detail.getCalo2():detail.getCalo()) : (detail.getCalo4()!=0?detail.getCalo4():detail.getCalo2()*2);
        int time = factor==1 ? (detail.getCookingTimeMinutes2()!=0?detail.getCookingTimeMinutes2():detail.getCookingTimeMinutes()) : (detail.getCookingTimeMinutes4()!=0?detail.getCookingTimeMinutes4():detail.getCookingTimeMinutes2()*2);

        ((android.widget.TextView)findViewById(R.id.tvCarbs)).setText(formatMacro(carbs,"carbs"));
        ((android.widget.TextView)findViewById(R.id.tvProtein)).setText(formatMacro(protein,"proteins"));
        ((android.widget.TextView)findViewById(R.id.tvCalo)).setText(formatMacro(calo,"calo"));
        ((android.widget.TextView)findViewById(R.id.tvFat)).setText(formatMacro(fat,"fat"));
        binding.tvTime.setText(time+" phút");
    }
} 