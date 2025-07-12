package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.BlogCommentDao;
import com.example.bepnhataapp.common.model.BlogComment;

public class BlogCommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_comments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Bình luận");
        }

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        // Lấy blogID từ intent hoặc bundle
        long blogID = getIntent().getLongExtra("blogID", -1);
        BlogCommentDao dao = new BlogCommentDao(this);
        List<BlogComment> commentList = dao.getByBlog(blogID);
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        recyclerViewComments.setAdapter(commentAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
