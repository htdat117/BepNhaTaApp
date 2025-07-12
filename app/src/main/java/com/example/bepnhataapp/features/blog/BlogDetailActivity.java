package com.example.bepnhataapp.features.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.BlogEntity;
import com.example.bepnhataapp.common.model.Blog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.features.blog.Comment;
import com.example.bepnhataapp.features.blog.CommentAdapter;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.common.dao.BlogCommentDao;
import com.example.bepnhataapp.common.model.BlogComment;
import com.example.bepnhataapp.common.dao.BlogDao;
import java.util.Collections;
import android.widget.EditText;
import android.widget.ImageButton;

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

        com.example.bepnhataapp.common.model.Blog blog = (com.example.bepnhataapp.common.model.Blog) intent.getSerializableExtra(EXTRA_BLOG);
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
        long blogID = getIntent().getLongExtra("blogID", -1);
        BlogCommentDao dao = new BlogCommentDao(this);
        List<BlogComment> commentList = dao.getByBlog(blogID);
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);

        // Hiển thị số lượng bình luận thực
        TextView tvCommentCount = findViewById(R.id.tvCommentCount);
        if (tvCommentCount != null) {
            tvCommentCount.setText("Bình luận (" + commentList.size() + ")");
        }

        // Xử lý gửi bình luận
        EditText edtComment = findViewById(R.id.edtComment);
        ImageButton btnSend = findViewById(R.id.btnSendComment);
        btnSend.setOnClickListener(v -> {
            String content = edtComment.getText().toString().trim();
            if (!content.isEmpty()) {
                // Lấy user hiện tại (ví dụ: customerID = 1, cần thay bằng user thực tế)
                long customerID = 1;
                BlogComment newComment = new BlogComment();
                newComment.setBlogID(blogID);
                newComment.setCustomerID(customerID);
                newComment.setContent(content);
                newComment.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date()));
                newComment.setUsefulness(0);
                dao.insert(newComment);
                // Reload lại danh sách bình luận
                List<BlogComment> updatedList = dao.getByBlog(blogID);
                commentAdapter.updateData(updatedList);
                if (tvCommentCount != null) {
                    tvCommentCount.setText("Bình luận (" + updatedList.size() + ")");
                }
                edtComment.setText("");
            }
        });
    }

    private void setupSuggestionSection() {
        RecyclerView rvSuggest = findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new LinearLayoutManager(this));
        BlogDao blogDao = new BlogDao(this);
        List<BlogEntity> allBlogEntities = blogDao.getAll();
        List<Blog> allBlogs = new ArrayList<>();
        for (BlogEntity entity : allBlogEntities) {
            allBlogs.add(new Blog(
                entity.getBlogID(),
                entity.getTitle(),
                entity.getContent(), // description
                entity.getTag(),     // category
                entity.getImageThumb(),
                false, // isFavorite (chưa có trường này trong entity)
                entity.getCreatedAt(),
                0, // likes (chưa có trường này trong entity)
                0  // views (chưa có trường này trong entity)
            ));
        }
        Collections.shuffle(allBlogs);
        int maxSuggest = 5;
        List<Blog> suggestBlogList = allBlogs.size() > maxSuggest ? allBlogs.subList(0, maxSuggest) : allBlogs;
        com.example.bepnhataapp.common.adapter.BlogAdapter suggestBlogAdapter = new com.example.bepnhataapp.common.adapter.BlogAdapter(suggestBlogList);
        rvSuggest.setAdapter(suggestBlogAdapter);
    }
} 
