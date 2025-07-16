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
import com.example.bepnhataapp.features.blog.BlogCommentsActivity;
import com.example.bepnhataapp.common.dao.FavouriteBlogDao;
import com.example.bepnhataapp.common.model.FavouriteBlog;
import com.example.bepnhataapp.common.model.Customer;

// Đổi extends AppCompatActivity thành BaseActivity và implement OnNavigationItemReselectedListener
public class BlogDetailActivity extends com.example.bepnhataapp.common.base.BaseActivity implements com.example.bepnhataapp.common.base.BaseActivity.OnNavigationItemReselectedListener {

    public static final String EXTRA_BLOG = "extra_blog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        // Gắn bottom navigation fragment, chọn tab Trang chủ sáng
        setupBottomNavigationFragment(R.id.nav_home);

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
        // Lấy số like, comment, trạng thái yêu thích từ DB
        BlogDao blogDao = new BlogDao(this);
        BlogEntity blogEntity = blogDao.get(blog.getBlogID());
        BlogCommentDao commentDao = new BlogCommentDao(this);
        int commentCount = commentDao.getByBlog(blog.getBlogID()).size();
        tvComment.setText(String.valueOf(commentCount));
        tvContent.setText(blog.getDescription());

        // Thêm sự kiện click cho nút 'Tất cả' bình luận
        TextView tvSeeAllComments = findViewById(R.id.tvSeeAllComments);
        if (tvSeeAllComments != null) {
            tvSeeAllComments.setOnClickListener(v -> {
                Intent intentSeeAll = new Intent(this, BlogCommentsActivity.class);
                intentSeeAll.putExtra("blogID", blog.getBlogID());
                startActivity(intentSeeAll);
            });
        }

        // The comment and suggestion sections are still using hardcoded data.
        // This can be changed later to fetch real data based on the blog ID.
        setupCommentSection();
        setupSuggestionSection();

        ImageView ivComment = findViewById(R.id.ivComment);
        ImageView ivFavorite = findViewById(R.id.ivFavorite);
        // Xử lý nút Comment: cuộn đến ô nhập bình luận
        if (ivComment != null) {
            ivComment.setOnClickListener(v -> {
                EditText edtComment = findViewById(R.id.edtComment);
                if (edtComment != null) {
                    edtComment.requestFocus();
                    android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.showSoftInput(edtComment, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
        // Xử lý nút Favorite (ivFavorite): chỉ thêm/xóa khỏi mục yêu thích
        if (ivFavorite != null) {
            final boolean[] isFav = {false};
            if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                if (c != null) {
                    FavouriteBlogDao favDao = new FavouriteBlogDao(this);
                    isFav[0] = favDao.isFavourite(blog.getBlogID(), c.getCustomerID());
                }
            }
            ivFavorite.setImageResource(isFav[0] ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
            ivFavorite.setOnClickListener(v -> {
                if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                    android.widget.Toast.makeText(this, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                com.example.bepnhataapp.common.dao.CustomerDao cusDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer cus = cusDao.findByPhone(phone);
                if (cus == null) return;
                FavouriteBlogDao favDao2 = new FavouriteBlogDao(this);
                if (isFav[0]) {
                    favDao2.delete(blog.getBlogID(), cus.getCustomerID());
                    ivFavorite.setImageResource(R.drawable.ic_favorite_unchecked);
                    android.widget.Toast.makeText(this, "Đã xóa khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    favDao2.insert(new com.example.bepnhataapp.common.model.FavouriteBlog(blog.getBlogID(), cus.getCustomerID(), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                    ivFavorite.setImageResource(R.drawable.ic_favorite_checked);
                    android.widget.Toast.makeText(this, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                }
                isFav[0] = !isFav[0];
            });
        }

        TextView tvLike = findViewById(R.id.tvLike);
        ImageView ivLike = findViewById(R.id.ivLike);
        // Sử dụng mảng để biến dùng được trong lambda
        final int[] likeCount = {blogEntity != null ? blogEntity.getLikes() : 0};
        tvLike.setText(String.valueOf(likeCount[0]));
        // Kiểm tra trạng thái đã like của user (dùng SharedPreferences đơn giản cho mỗi user/blog)
        String likeKey = "like_blog_" + blog.getBlogID() + "_user_" + com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
        android.content.SharedPreferences prefs = getSharedPreferences("blog_likes", MODE_PRIVATE);
        final boolean[] liked = {prefs.getBoolean(likeKey, false)};
        ivLike.setImageResource(liked[0] ? R.drawable.ic_like_checked : R.drawable.ic_like);
        ivLike.setOnClickListener(v -> {
            if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                android.widget.Toast.makeText(this, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if (liked[0]) {
                // Bỏ like
                likeCount[0] = Math.max(0, likeCount[0] - 1);
                liked[0] = false;
                ivLike.setImageResource(R.drawable.ic_like);
            } else {
                // Like
                likeCount[0]++;
                liked[0] = true;
                ivLike.setImageResource(R.drawable.ic_like_checked);
            }
            tvLike.setText(String.valueOf(likeCount[0]));
            // Lưu lại trạng thái like vào DB và SharedPreferences
            if (blogEntity != null) {
                blogEntity.setLikes(likeCount[0]);
                blogDao.update(blogEntity);
            }
            prefs.edit().putBoolean(likeKey, liked[0]).apply();
        });
        // Hiển thị đúng số bình luận
        commentCount = commentDao.getByBlog(blog.getBlogID()).size();
        tvComment.setText(String.valueOf(commentCount));
    }

    private void setupCommentSection() {
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        Blog blog = (Blog) getIntent().getSerializableExtra(EXTRA_BLOG);
        long blogID = blog != null ? blog.getBlogID() : -1;
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
                // Lấy user hiện tại từ session
                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                com.example.bepnhataapp.common.dao.CustomerDao customerDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer user = phone != null ? customerDao.findByPhone(phone) : null;
                if (user == null) {
                    android.widget.Toast.makeText(this, "Vui lòng đăng nhập để bình luận", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                BlogComment newComment = new BlogComment();
                newComment.setBlogID(blogID);
                newComment.setCustomerID(user.getCustomerID());
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

    @Override
    protected int getBottomNavigationContainerId() {
        // id này phải trùng với id của ViewGroup chứa menu trong layout (sửa layout nếu cần)
        return R.id.bottom_navigation;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }
} 
