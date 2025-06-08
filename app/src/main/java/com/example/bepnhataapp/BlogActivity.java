package com.example.bepnhataapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.adapter.BlogAdapter;
import com.example.bepnhataapp.model.Blog;
import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBlog;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        recyclerViewBlog = findViewById(R.id.recyclerViewBlog);
        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(this));

        blogList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        blogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!", "Mẹo hay - Nấu chuẩn", R.drawable.sample_blog, false));
        blogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.sample_blog, false));
        blogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.sample_blog, false));
        blogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.sample_blog, false));
        // ... thêm các blog nhỏ khác nếu muốn

        blogAdapter = new BlogAdapter(blogList);
        recyclerViewBlog.setAdapter(blogAdapter);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
} 