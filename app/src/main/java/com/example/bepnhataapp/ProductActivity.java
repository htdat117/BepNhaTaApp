package com.example.bepnhataapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapters.ProductAdapter;
import com.example.models.Product;
import com.example.adapters.CategoryAdapter;
import com.example.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
    }
}