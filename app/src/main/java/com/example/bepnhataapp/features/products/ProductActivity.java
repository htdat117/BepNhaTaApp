package com.example.bepnhataapp.features.products;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ProductAdapter;
import com.example.bepnhataapp.databinding.ActivityProductBinding;
import com.example.bepnhataapp.common.models.Product;
import com.example.bepnhataapp.common.adapter.CategoryAdapter;
import com.example.bepnhataapp.common.models.Category;
import com.example.bepnhataapp.common.base.BaseActivity;

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
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Tất cả"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Khuyến mãi"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món nướng"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món kho"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món mặn"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món chay"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món canh"));
        categoryList.add(new Category(R.drawable.ic_launcher_background, "Món xào"));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategories.setAdapter(categoryAdapter);

        // Sản phẩm
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(R.drawable.ic_launcher_background, "Bò xào rau củ", "500 Kcal", "Giàu chất xơ", "20 phút", "2 người", "4 người", "150.000đ", "140.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Bánh canh cua", "1560 Kcal", "Nhiều canxi", "60 phút", "2 người", "4 người", "", "90.000đ", 4.4f, 2804, true));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        productList.add(new Product(R.drawable.ic_launcher_background, "Thịt rang cháy cạnh", "700 Kcal", "Giàu đạm", "30 phút", "2 người", "4 người", "65.000đ", "50.000đ", 4.4f, 1604, false));
        // Thêm sản phẩm mẫu khác nếu muốn
        ProductAdapter adapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(adapter);

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
                            Collections.sort(productList, new Comparator<Product>() {
                                @Override
                                public int compare(Product o1, Product o2) {
                                    int price1 = getPriceValue(o1.price);
                                    int price2 = getPriceValue(o2.price);
                                    return price2 - price1;
                                }
                            });
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_price_low_high) {
                            tvSortOption.setText("Thấp nhất - Cao nhất");
                            Collections.sort(productList, new Comparator<Product>() {
                                @Override
                                public int compare(Product o1, Product o2) {
                                    int price1 = getPriceValue(o1.price);
                                    int price2 = getPriceValue(o2.price);
                                    return price1 - price2;
                                }
                            });
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_name_az) {
                            tvSortOption.setText("Tên: A-Z");
                            Collections.sort(productList, new Comparator<Product>() {
                                @Override
                                public int compare(Product o1, Product o2) {
                                    return o1.name.compareToIgnoreCase(o2.name);
                                }
                            });
                            adapter.notifyDataSetChanged();
                            return true;
                        } else if (id == R.id.sort_name_za) {
                            tvSortOption.setText("Tên: Z-A");
                            Collections.sort(productList, new Comparator<Product>() {
                                @Override
                                public int compare(Product o1, Product o2) {
                                    return o2.name.compareToIgnoreCase(o1.name);
                                }
                            });
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

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }
}