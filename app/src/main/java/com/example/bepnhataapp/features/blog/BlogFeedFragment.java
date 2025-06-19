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
        addSampleBlogPosts(); // Add sample data

        blogAdapter = new BlogAdapter(blogList);
        recyclerViewBlog.setAdapter(blogAdapter);

        return view;
    }

    private void addSampleBlogPosts() {
        // Thêm dữ liệu mẫu giống hình minh họa
        blogList.add(new Blog(
                "Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh",
                "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!",
                "Mẹo hay - Nấu chuẩn", R.drawable.blog, false));

        blogList.add(new Blog(
                "Bí quyết làm thịt kho tàu chuẩn vị",
                "",
                "Mẹo hay - Nấu chuẩn", R.drawable.blog, false));

        blogList.add(new Blog(
                "Cách làm bánh flan mềm mịn không tanh",
                "",
                "Mẹo hay - Nấu chuẩn", R.drawable.blog, false));
    }
} 