package com.example.bepnhataapp.features.products;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ProductAdapter;
import com.example.bepnhataapp.databinding.ActivityProductBinding;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.adapter.CategoryAdapter;
import com.example.bepnhataapp.common.model.Category;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.MyApplication;
import com.example.bepnhataapp.features.products.FilterProductBottomSheet;
import com.example.bepnhataapp.common.dao.ProductDetailDao;
import com.example.bepnhataapp.common.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.text.Normalizer;
import androidx.annotation.NonNull;

public class ProductActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    ActivityProductBinding binding;

    private final List<Product> allProducts = new ArrayList<>();
    private final List<Product> productList = new ArrayList<>();
    private ProductAdapter adapter;

    // hold current filter
    private FilterProductBottomSheet.FilterCriteria currentCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toFilter();
        
        // Setup the bottom navigation fragment
        setupBottomNavigationFragment(R.id.nav_ingredients);

        // Danh mục
        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Category> categoryList = new ArrayList<>();
        addCategory(categoryList, "Tất cả", "cate_product_all");
        addCategory(categoryList, "Khuyến mãi", "cate_product_sale");
        addCategory(categoryList, "Món nướng", "cate_product_grill");
        addCategory(categoryList, "Món kho", "cate_product_kho");
        addCategory(categoryList, "Món chay", "cate_product_vegetarian");
        addCategory(categoryList, "Món nước", "cate_product_noodle");
        addCategory(categoryList, "Món xào", "cate_product_stir");
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

        // Ẩn/hiện thanh danh mục khi cuộn
        final boolean[] isCategoryVisible = {true};
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Người dùng cuộn xuống → ẩn
                if (dy > 10 && isCategoryVisible[0]) {
                    isCategoryVisible[0] = false;
                    rvCategories.animate()
                            .translationY(-rvCategories.getHeight())
                            .alpha(0f)
                            .setInterpolator(new android.view.animation.AccelerateInterpolator())
                            .setDuration(250)
                            .withEndAction(() -> rvCategories.setVisibility(View.GONE));
                }

                // Người dùng cuộn lên → hiện
                if (dy < -10 && !isCategoryVisible[0]) {
                    isCategoryVisible[0] = true;
                    rvCategories.setVisibility(View.VISIBLE);
                    rvCategories.setAlpha(0f);
                    rvCategories.setTranslationY(-rvCategories.getHeight());
                    rvCategories.animate()
                            .translationY(0)
                            .alpha(1f)
                            .setInterpolator(new android.view.animation.DecelerateInterpolator())
                            .setDuration(250)
                            .start();
                }
            }
        });

        // Load asynchronously để tránh block UI
        new Thread(() -> {
            // Đợi DB sẵn sàng (như cách load recipe)
            while (!MyApplication.isDbReady) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            ProductDao dao = new ProductDao(this);
            allProducts.addAll(dao.getAll());
            productList.addAll(allProducts);

            runOnUiThread(adapter::notifyDataSetChanged);
        }).start();

        // Sắp xếp
        TextView tvSortOption = findViewById(R.id.tvSortOption);
        tvSortOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProductActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_sort, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.sort_price_high_low) {
                            tvSortOption.setText("Cao nhất - Thấp nhất");
                            Collections.sort(productList, (o1, o2) -> {
                                int price1 = o1.getProductPrice() * (100 - o1.getSalePercent()) / 100;
                                int price2 = o2.getProductPrice() * (100 - o2.getSalePercent()) / 100;
                                return price2 - price1;
                            });
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_price_low_high) {
                            tvSortOption.setText("Thấp nhất - Cao nhất");
                            Collections.sort(productList, (o1, o2) -> {
                                int price1 = o1.getProductPrice() * (100 - o1.getSalePercent()) / 100;
                                int price2 = o2.getProductPrice() * (100 - o2.getSalePercent()) / 100;
                                return price1 - price2;
                            });
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_name_az) {
                            tvSortOption.setText("Tên: A-Z");
                            Collections.sort(productList, (o1, o2) -> o1.getProductName().compareToIgnoreCase(o2.getProductName()));
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_name_za) {
                            tvSortOption.setText("Tên: Z-A");
                            Collections.sort(productList, (o1, o2) -> o2.getProductName().compareToIgnoreCase(o1.getProductName()));
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryAdapter.setOnCategoryClickListener(this::applyCategoryFilter);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void toFilter() {
        binding.btnFilterProduct.setOnClickListener(v -> {
            FilterProductBottomSheet bottomSheet = new FilterProductBottomSheet();
            bottomSheet.setInitialCriteria(currentCriteria);
            bottomSheet.setOnFilterAppliedListener(this::applyAdvancedFilter);
            bottomSheet.show(getSupportFragmentManager(), "FilterProductBottomSheet");
        });
    }

    // Hàm lấy giá trị số từ chuỗi giá (vd: "140.000đ" -> 140000)
    private int getPriceValue(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) return 0;
        String number = priceStr.replaceAll("[^0-9]", "");
        if (number.isEmpty()) return 0;
        return Integer.parseInt(number);
    }

    // Convert integer price (e.g., 150000) to string "150.000đ"
    private String formatPrice(int price) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        nf.setGroupingUsed(true);
        return nf.format(price) + "đ";
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void addCategory(List<Category> list, String name, String drawableName) {
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (resId == 0) resId = R.drawable.ic_launcher_background; // default
        list.add(new Category(resId, name));
    }

    private void applyCategoryFilter(String categoryName){
        // assumes fields allProducts and productList defined final effectively
        // clear then add matches
        productList.clear();
        if("Tất cả".equalsIgnoreCase(categoryName)){
            productList.addAll(allProducts);
        }else if("Khuyến mãi".equalsIgnoreCase(categoryName)){
            for(Product p: allProducts){
                if(p.getSalePercent()>0) productList.add(p);
            }
        }else{
            // Chuẩn hoá tên danh mục từ UI thành "slug" để khớp DB (loại bỏ tiền tố "Món ")
            String key = categoryName.toLowerCase().replace("món ", "").trim();
            for(Product p: allProducts){
                if(p.getCategory()!=null && p.getCategory().equalsIgnoreCase(key)){
                    productList.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Utility: remove Vietnamese accents & lowercase for insensitive compare
    private String unaccent(String str){
        if(str==null) return "";
        String n = Normalizer.normalize(str, Normalizer.Form.NFD);
        return n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    private void applyAdvancedFilter(FilterProductBottomSheet.FilterCriteria c){
        // save current criteria for next open
        this.currentCriteria = c;

        productList.clear();
        ProductDetailDao detailDao = new ProductDetailDao(this);
        for(Product p: allProducts){
            boolean ok=true;
            ProductDetail d=detailDao.getByProductId(p.getProductID());
            
            // Lọc theo nguyên liệu (foodTag)
            if(c.ingredients!=null && !c.ingredients.isEmpty()){
                boolean ingredientFound=false;
                if(d!=null && d.getFoodTag()!=null){
                    String foodTagNorm = unaccent(d.getFoodTag());
                    for(String ingredient:c.ingredients){
                        if(foodTagNorm.contains(unaccent(ingredient))){
                            ingredientFound=true;
                            break;
                        }
                    }
                }
                if(!ingredientFound) ok=false;
            }
            
            // Lọc theo địa phương (cuisine)
            if(ok && c.regions!=null && !c.regions.isEmpty()){
                boolean regionFound=false;
                if(d!=null && d.getCuisine()!=null){
                    String cuisineNorm = normalizeKeyword(d.getCuisine());
                    for(String region:c.regions){
                        String regionKey = normalizeKeyword(region);
                        if(cuisineNorm.contains(regionKey) || regionKey.contains(cuisineNorm)){
                            regionFound=true; break;
                        }
                    }
                }
                if(!regionFound) ok=false;
            }
            // Lọc kcal, time như cũ
            if(ok && d!=null){
                if(d.getCalo() > c.maxKcal) ok=false;
                if(d.getCookingTimeMinutes() > c.maxTime) ok=false;
            }
            if(ok && c.nutritions!=null && !c.nutritions.isEmpty()){
                boolean found = false;
                if(d!=null && d.getNutritionTag()!=null){
                    // Chia chuỗi nutritionTag trong DB thành các token riêng lẻ
                    java.util.Set<String> storedTags = splitTags(d.getNutritionTag());
                    for(String chipTag : c.nutritions){
                        String key = normalizeKeyword(chipTag);
                        for(String store : storedTags){
                            if(store.equals(key)) { found=true; break; }
                        }
                        if(found) break;
                    }
                }
                if(!found) ok=false;
            }
            if(ok) productList.add(p);
        }
        adapter.notifyDataSetChanged();
    }

    // Helper: normalize keyword/tags for matching (remove accents, lower case, strip prefixes/delimiters)
    private String normalizeKeyword(String str){
        if(str==null) return "";
        String s = unaccent(str);
        s = s.replace("-"," ").replace("_"," ");
        // Loại bỏ tiền tố "mien " hoặc "mon " nếu có
        s = s.replace("mien ","").replace("mon ","");
        return s.trim();
    }

    // Tách chuỗi tag thành set đã chuẩn hoá
    private java.util.Set<String> splitTags(String raw){
        java.util.Set<String> set = new java.util.HashSet<>();
        if(raw==null) return set;
        String[] parts = raw.split("[,;|/\\\\]"); // , ; | / \ 
        for(String p : parts){
            String k = normalizeKeyword(p);
            if(!k.isEmpty()) set.add(k);
        }
        return set;
    }
}
