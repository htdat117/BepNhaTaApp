package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

import java.util.ArrayList;
import java.util.List;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.common.models.Blog;
import com.example.bepnhataapp.common.dao.BlogDao;
import com.example.bepnhataapp.common.model.BlogEntity;
import com.example.bepnhataapp.MyApplication;

public class BlogFeedFragment extends Fragment {

    private RecyclerView recyclerViewBlog;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_feed, container, false);

        recyclerViewBlog = view.findViewById(R.id.recyclerViewBlog);
        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(getContext()));

        blogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(blogList);
        recyclerViewBlog.setAdapter(blogAdapter);
        
        loadBlogsAsync();

        return view;
    }

    private void loadBlogsAsync() {
        new Thread(() -> {
            // Đợi đến khi cờ hiệu isDbReady được giơ lên
            while (!MyApplication.isDbReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Khi DB đã sẵn sàng, tiến hành truy vấn
            BlogDao blogDao = new BlogDao(requireContext());
            List<BlogEntity> blogEntities = blogDao.getAll();
            List<Blog> newBlogs = new ArrayList<>();
            if (blogEntities != null) {
                for(BlogEntity entity : blogEntities) {
                    newBlogs.add(new Blog(
                            entity.getTitle(),
                            entity.getContent(),
                            entity.getTag(),
                            R.drawable.placeholder_banner_background,
                            false,
                            entity.getCreatedAt(),
                            0,
                            0
                    ));
                }
            }

            // Cập nhật giao diện trên luồng chính
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    blogList.clear();
                    blogList.addAll(newBlogs);
                    blogAdapter.notifyDataSetChanged();
                });
            }
        }).start();
    }
} 