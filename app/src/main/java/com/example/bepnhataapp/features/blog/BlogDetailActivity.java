package com.example.bepnhataapp.features.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.Blog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.features.blog.Comment;
import com.example.bepnhataapp.features.blog.CommentAdapter;
import com.bumptech.glide.Glide;

public class BlogDetailActivity extends AppCompatActivity {

    public static final String EXTRA_BLOG = "extra_blog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Get blog data from intent
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_BLOG)) {
            finish(); // Finish if no data is provided
            return;
        }

        com.example.bepnhataapp.common.models.Blog blog = (com.example.bepnhataapp.common.models.Blog) intent.getSerializableExtra(EXTRA_BLOG);
        if (blog == null) {
            finish(); // Finish if blog data is null
            return;
        }

        // Bind real data to views
        ImageView imgMain = findViewById(R.id.imgMain);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvLike = findViewById(R.id.tvLike);
        TextView tvComment = findViewById(R.id.tvComment);
        TextView tvContent = findViewById(R.id.tvContent);

        Glide.with(this)
                .load(blog.getImageUrl())
                .placeholder(R.drawable.placeholder_banner_background)
                .error(R.drawable.placeholder_banner_background)
                .into(imgMain);

        tvCategory.setText(blog.getCategory());
        tvTitle.setText(blog.getTitle());
        tvDate.setText(blog.getDate());
        tvLike.setText(String.valueOf(blog.getLikes()));
        tvComment.setText(String.valueOf(blog.getViews()));
        tvContent.setText(blog.getDescription());

        // The comment and suggestion sections are still using hardcoded data.
        // This can be changed later to fetch real data based on the blog ID.
        setupCommentSection();
        setupSuggestionSection();
    }

    private void setupCommentSection() {
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment("Kiên Đoàn", "2 ngày trước", "Bài viết rất hữu ích! Cảm ơn Bếp Nhà Ta đã chia sẻ", 2));
        commentList.add(new Comment("Bếp Nhà Ta", "2 ngày trước", "Cảm ơn bạn đã chia sẻ. Bếp Nhà Ta sẽ tiếp tục chia sẻ những bài viết bổ ích trong thời gian sắp tới!", 0));
        commentList.add(new Comment("Đức Mạnh", "2 giờ trước", "Tôi đã áp dụng và thành công. Món ăn rất ngon, hợp với khẩu vị của gia đình. Con trai tôi ăn 3 chén cơm liền, cháu tôi Hưng Trần ăn rất ngon miệng, ăn xong vẫn thèm. Cảm ơn chia sẻ của Bếp Nhà Ta rất nhiều!!", 2));
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);
    }

    private void setupSuggestionSection() {
        RecyclerView rvSuggest = findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new LinearLayoutManager(this));
        List<com.example.bepnhataapp.common.models.Blog> suggestBlogList = new ArrayList<>();
        suggestBlogList.add(new com.example.bepnhataapp.common.models.Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", "", false, "31/3/2025", 20, 15));
        suggestBlogList.add(new com.example.bepnhataapp.common.models.Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", "", false, "31/3/2025", 20, 15));
        suggestBlogList.add(new com.example.bepnhataapp.common.models.Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", "", false, "31/3/2025", 20, 15));
        com.example.bepnhataapp.common.adapter.BlogAdapter suggestBlogAdapter = new com.example.bepnhataapp.common.adapter.BlogAdapter(suggestBlogList);
        rvSuggest.setAdapter(suggestBlogAdapter);
    }
} 