package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.common.models.Blog;
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

        // ... thêm các blog nhỏ khác nếu muốn

        blogAdapter = new BlogAdapter(blogList);
        recyclerViewBlog.setAdapter(blogAdapter);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
} 