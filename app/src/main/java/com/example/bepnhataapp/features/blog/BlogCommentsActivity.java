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

        commentList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        commentList.add(new Comment("Kiên Đoàn", "2 ngày trước", "Bài viết rất hữu ích! Cảm ơn Bếp Nhà Ta đã chia sẻ", 2));
        commentList.add(new Comment("Bếp Nhà Ta", "2 ngày trước", "Cảm ơn bạn đã chia sẻ. Bếp Nhà Ta sẽ tiếp tục chia sẻ những bài viết bổ ích trong thời gian sắp tới!", 0));
        commentList.add(new Comment("Đức Mạnh", "2 giờ trước", "Tôi đã áp dụng và thành công. Món ăn rất ngon, hợp với khẩu vị của gia đình. Con trai tôi ăn 3 chén cơm liền, cháu tôi Hưng Trần ăn rất ngon miệng, ăn xong vẫn thèm. Cảm ơn chia sẻ của Bếp Nhà Ta rất nhiều!!", 2));
        commentList.add(new Comment("Bếp Nhà Ta", "2 giờ trước", "Cảm ơn bạn đã chia sẻ. Bếp Nhà Ta sẽ tiếp tục chia sẻ những bài viết bổ ích trong thời gian sắp tới!", 0));
        commentList.add(new Comment("Đức Mạnh", "2 giờ trước", "Tôi đã áp dụng và thành công. Món ăn rất ngon, hợp với khẩu vị của gia đình. Con trai tôi ăn 3 chén cơm liền, cháu tôi Hưng Trần ăn rất ngon miệng, ăn xong vẫn thèm. Cảm ơn chia sẻ của Bếp Nhà Ta rất nhiều!!", 0));
        commentList.add(new Comment("Bếp Nhà Ta", "2 giờ trước", "Cảm ơn bạn đã chia sẻ. Bếp Nhà Ta sẽ tiếp tục chia sẻ những bài viết bổ ích trong thời gian sắp tới!", 0));

        commentAdapter = new CommentAdapter(commentList);
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