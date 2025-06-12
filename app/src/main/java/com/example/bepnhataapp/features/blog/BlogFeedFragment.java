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

public class BlogFeedFragment extends Fragment {

    private RecyclerView recyclerViewBlog;
    private BlogFeedAdapter blogFeedAdapter;
    private List<BlogPostItem> blogPostList;
    private ImageView btnBack;
    private TextView tvHeaderTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_feed, container, false);

        recyclerViewBlog = view.findViewById(R.id.recyclerViewBlog);
        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(getContext()));

        blogPostList = new ArrayList<>();
        addSampleBlogPosts(); // Add sample data

        blogFeedAdapter = new BlogFeedAdapter(blogPostList);
        recyclerViewBlog.setAdapter(blogFeedAdapter);

        // Ánh xạ và xử lý header
        btnBack = view.findViewById(R.id.btnBack);
        tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);

        if (tvHeaderTitle != null) {
            tvHeaderTitle.setText("Blog"); // Đặt tiêu đề là "Blog"
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                // Logic quay lại, có thể là popBackStack nếu có fragment trước đó
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });
        }

        return view;
    }

    private void addSampleBlogPosts() {
        // Thêm dữ liệu mẫu
        blogPostList.add(new BlogPostItem(R.drawable.food_placeholder, "Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh",
                "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!",
                "Mẹo hay - Nấu chuẩn", "31/3/2025", 20, 15));
        blogPostList.add(new BlogPostItem(R.drawable.food_placeholder, "Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh",
                "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!",
                "Mẹo hay - Nấu chuẩn", "31/3/2025", 20, 15));
        blogPostList.add(new BlogPostItem(R.drawable.food_placeholder, "Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh",
                "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!",
                "Mẹo hay - Nấu chuẩn", "31/3/2025", 20, 15));
        blogPostList.add(new BlogPostItem(R.drawable.food_placeholder, "Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh",
                "Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!",
                "Mẹo hay - Nấu chuẩn", "31/3/2025", 20, 15));

    }
} 