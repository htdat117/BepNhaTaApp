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
import com.example.bepnhataapp.common.model.Review;
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
import androidx.core.content.ContextCompat;
import android.widget.TextView;
import android.widget.ImageView;

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

        // Ensure product image has 16dp rounded corners
        binding.ivProduct.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_rounded_16));
        binding.ivProduct.setClipToOutline(true);

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
                com.example.bepnhataapp.common.utils.CartHelper.addProduct(
                        ProductDetailActivity.this,
                        finalProduct,
                        currentServingFactor,
                        quantity
                );
                android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm "+quantity+" sản phẩm vào giỏ hàng", android.widget.Toast.LENGTH_SHORT).show();
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

            // Favorite icon logic
            android.widget.ImageView ivFavorite = findViewById(R.id.ivFavorite);
            if (ivFavorite != null) {
                boolean isFav = false;
                if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                    String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                    com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                    com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                    if (c != null) {
                        com.example.bepnhataapp.common.dao.FavouriteProductDao favDao = new com.example.bepnhataapp.common.dao.FavouriteProductDao(this);
                        isFav = favDao.isFavourite(currentProduct.getProductID(), c.getCustomerID());
                    }
                }
                ivFavorite.setImageResource(isFav ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
                final boolean[] favState = new boolean[]{isFav};
                ivFavorite.setOnClickListener(v -> {
                    if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ProductDetailActivity.this)) {
                        android.widget.Toast.makeText(ProductDetailActivity.this, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(ProductDetailActivity.this);
                    com.example.bepnhataapp.common.dao.CustomerDao cusDao = new com.example.bepnhataapp.common.dao.CustomerDao(ProductDetailActivity.this);
                    com.example.bepnhataapp.common.model.Customer cus = cusDao.findByPhone(phone);
                    if (cus == null) return;

                    com.example.bepnhataapp.common.dao.FavouriteProductDao favDao2 = new com.example.bepnhataapp.common.dao.FavouriteProductDao(ProductDetailActivity.this);
                    if (favState[0]) {
                        favDao2.delete(currentProduct.getProductID(), cus.getCustomerID());
                        android.widget.Toast.makeText(ProductDetailActivity.this, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        favDao2.insert(new com.example.bepnhataapp.common.model.FavouriteProduct(currentProduct.getProductID(), cus.getCustomerID(), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                        android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                    }
                    favState[0] = !favState[0];
                    ivFavorite.setImageResource(favState[0] ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
                });
            }

            updatePriceText();
            // Additional binding like kcal, nutrition, etc. can be done here.

            // Load detail
            detail = new ProductDetailDao(this).getByProductId(currentProduct.getProductID());
            refreshMacroAndTime();

            // Setup ViewPager with two fragments
            pagerAdapter = new ProductDetailPagerAdapter(this, detail, currentProduct, currentProduct.getProductID(), currentServingFactor);
            binding.viewPager.setAdapter(pagerAdapter);
        } else {
            // still create adapter with null detail to avoid crash
            pagerAdapter = new ProductDetailPagerAdapter(this, null, null, -1, currentServingFactor);
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

        // Xử lý header tài khoản
        TextView tvLogin = findViewById(R.id.tv_login);
        ImageView ivLogo = findViewById(R.id.iv_logo);
        if(tvLogin != null) {
            if(com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                if(c != null) tvLogin.setText(c.getFullName());
            } else {
                tvLogin.setText("Đăng nhập");
            }
        }
        // Back action handled later in header section; keep ivLogo fallback if needed
        if(ivLogo != null) {
            ivLogo.setOnClickListener(v -> finish()); // fallback if back header not present yet
        }

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

        // After setting adapter for pager (or at end of onCreate), add Buy Now button handler
        binding.btnBuyNow.setOnClickListener(v -> {
            if (currentProduct == null) return;
            int servingFactor = currentServingFactor; // 1 for 2-person, 2 for 4-person

            // Calculate price per 2-person pack so that CheckoutActivity total logic works
            int originalVariantPrice = (servingFactor == 1) ? currentProduct.getProductPrice2() : currentProduct.getProductPrice4();
            int salePercentVariant = (servingFactor == 1) ? currentProduct.getSalePercent2() : currentProduct.getSalePercent4();
            int discountedVariantPrice = originalVariantPrice * (100 - salePercentVariant) / 100;

            // Normalise to 2-person pack price because CartItem#getTotal multiplies by servingFactor
            int pricePer2Pack = discountedVariantPrice / servingFactor;
            int oldPricePer2Pack = originalVariantPrice / servingFactor;

            com.example.bepnhataapp.common.model.CartItem ci = new com.example.bepnhataapp.common.model.CartItem(
                    currentProduct.getProductID(),
                    currentProduct.getProductName(),
                    pricePer2Pack,
                    oldPricePer2Pack,
                    quantity,
                    currentProduct.getProductThumb()
            );
            ci.setServing(servingFactor == 2 ? "4 người" : "2 người");

            java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> selected = new java.util.ArrayList<>();
            selected.add(ci);

            android.content.Intent intent = new android.content.Intent(ProductDetailActivity.this, com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
            intent.putExtra("selected_items", selected);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Set header title đúng cho trang chi tiết sản phẩm
        android.widget.TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        if(tvHeaderTitle != null){
            tvHeaderTitle.setText("Chi tiết sản phẩm");
        }

        // Xử lý header phụ (back + tiêu đề)
        TextView tvBackHeader = findViewById(R.id.txtContent);
        if(tvBackHeader != null) tvBackHeader.setText("Chi tiết sản phẩm");
        ImageView btnBack = findViewById(R.id.btnBack);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Thêm sự kiện click cho nút 'Xem công thức'
        binding.tvViewRecipe.setOnClickListener(v -> {
            if (detail != null && detail.getRecipeID() != null) {
                Intent intent = new Intent(ProductDetailActivity.this, com.example.bepnhataapp.features.recipes.RecipeDetailActivity.class);
                intent.putExtra("recipeId", detail.getRecipeID());
                startActivity(intent);
            } else {
                android.widget.Toast.makeText(ProductDetailActivity.this, "Sản phẩm này chưa có công thức liên kết!", android.widget.Toast.LENGTH_SHORT).show();
            }
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

        // Hiển thị giá 1 gói (không nhân với số lượng)
        binding.tvPrice.setText(nf.format(discountedPricePerPack)+"đ");

        // Old price handling (cũng chỉ 1 gói)
        if(salePercent > 0){
            binding.tvOldPrice.setVisibility(android.view.View.VISIBLE);
            binding.tvOldPrice.setText(nf.format(originalPricePerPack)+"đ");
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
