package com.example.bepnhataapp.features.products;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ProductAdapter;
import com.example.bepnhataapp.databinding.ActivityProductBinding;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.adapter.CategoryAdapter;
import com.example.bepnhataapp.common.models.Category;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.MyApplication;

import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;

public class ProductActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    ActivityProductBinding binding;

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
        addCategory(categoryList, "Món mặn", "cate_product_all"); // fallback
        addCategory(categoryList, "Món chay", "cate_product_vegetarian");
        addCategory(categoryList, "Món nước", "cate_product_noodle");
        addCategory(categoryList, "Món xào", "cate_product_stir");
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategories.setAdapter(categoryAdapter);

        // Sản phẩm - load từ DB
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        List<Product> productList = new ArrayList<>();
        ProductAdapter adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

        // Load asynchronously để tránh block UI
        new Thread(() -> {
            // Đợi DB sẵn sàng (như cách load recipe)
            while (!MyApplication.isDbReady) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            ProductDao dao = new ProductDao(this);
            productList.addAll(dao.getAll());

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
    }

    private void toFilter() {
        binding.btnFilterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, FilterProductActivity.class);
                startActivity(intent);
            }
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
}