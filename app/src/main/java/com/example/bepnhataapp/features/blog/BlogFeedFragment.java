package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

import java.util.ArrayList;
import java.util.List;

public class BlogFeedFragment extends Fragment {

    private RecyclerView recyclerViewBlog;
    private com.example.bepnhataapp.common.adapter.BlogAdapter blogAdapter;
    private List<com.example.bepnhataapp.common.model.Blog> blogList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_feed, container, false);

        recyclerViewBlog = view.findViewById(R.id.recyclerViewBlog);
        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(getContext()));

        blogList = new ArrayList<>();
        blogAdapter = new com.example.bepnhataapp.common.adapter.BlogAdapter(blogList);
        recyclerViewBlog.setAdapter(blogAdapter);

        loadBlogsAsync();

        return view;
    }

    private void loadBlogsAsync() {
        new Thread(() -> {
            // Wait for DB to be ready
            while (!com.example.bepnhataapp.MyApplication.isDbReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (getContext() == null) return;

            // Query data from DAO
            com.example.bepnhataapp.common.dao.BlogDao blogDao = new com.example.bepnhataapp.common.dao.BlogDao(getContext());
            List<com.example.bepnhataapp.common.model.BlogEntity> blogEntities = blogDao.getAll();
            final List<com.example.bepnhataapp.common.model.Blog> newBlogs = new ArrayList<>();
            if (blogEntities != null) {
                for (com.example.bepnhataapp.common.model.BlogEntity entity : blogEntities) {
                    newBlogs.add(new com.example.bepnhataapp.common.model.Blog(
                            entity.getBlogID(),
                            entity.getTitle(),
                            entity.getContent(),
                            entity.getTag(),
                            entity.getImageThumb(),
                            false,
                            entity.getCreatedAt(),
                            0,
                            0
                    ));
                }
            }

            // Update UI on the main thread
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
