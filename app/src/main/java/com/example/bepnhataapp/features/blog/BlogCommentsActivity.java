package com.example.bepnhataapp.features.blog;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.BlogCommentDao;
import com.example.bepnhataapp.common.model.BlogComment;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.widget.TextView;
import android.widget.LinearLayout;

public class BlogCommentsActivity extends AppCompatActivity {
    private CommentAdapter adapter;
    private java.util.List<BlogComment> commentList = new java.util.ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
    private String currentSortLabel = "Mới nhất";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_comments);

        // Nút back
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Lấy blogID từ intent
        long blogID = getIntent().getLongExtra("blogID", -1);

        RecyclerView rcComment = findViewById(R.id.rcComment);
        if (rcComment != null && blogID > 0) {
            commentList = new BlogCommentDao(this).getByBlog(blogID);
            adapter = new CommentAdapter(commentList);
            rcComment.setLayoutManager(new LinearLayoutManager(this));
            rcComment.setAdapter(adapter);
        }

        // Hiển thị avatar người dùng nếu đã đăng nhập
        de.hdodenhof.circleimageview.CircleImageView imvAvatarCmt = findViewById(R.id.imvAvatarCmt);
        String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
        com.example.bepnhataapp.common.dao.CustomerDao customerDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
        com.example.bepnhataapp.common.model.Customer user = phone!=null? customerDao.findByPhone(phone):null;
        if(user!=null && imvAvatarCmt!=null){
            byte[] avatar = user.getAvatar();
            if(avatar!=null && avatar.length>0){
                imvAvatarCmt.setImageBitmap(android.graphics.BitmapFactory.decodeByteArray(avatar,0,avatar.length));
            } else if(user.getAvatarLink()!=null){
                String link=user.getAvatarLink(); Object src=link;
                if(!link.startsWith("http")){
                    String res=link; int dot=res.lastIndexOf('.'); if(dot>0) res=res.substring(0,dot);
                    int resId=getResources().getIdentifier(res,"drawable",getPackageName()); if(resId!=0) src=resId;
                }
                com.bumptech.glide.Glide.with(this).load(src).placeholder(R.drawable.ic_avatar).into(imvAvatarCmt);
            }
        }

        // Gửi bình luận
        EditText edtComment = findViewById(R.id.edtComment);
        ImageButton btnSendComment = findViewById(R.id.btnSendComment);
        if(btnSendComment!=null){
            btnSendComment.setOnClickListener(v->{
                if(!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)){
                    Toast.makeText(this,"Vui lòng đăng nhập để bình luận",Toast.LENGTH_SHORT).show();
                    return;
                }
                String content= edtComment.getText().toString().trim();
                if(content.isEmpty()){
                    Toast.makeText(this,"Nội dung không được trống",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(user==null) return;

                BlogComment rc = new BlogComment();
                rc.setBlogID(blogID);
                rc.setCustomerID(user.getCustomerID());
                rc.setContent(content);
                rc.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date()));
                rc.setUsefulness(0);
                new BlogCommentDao(this).insert(rc);

                commentList = new BlogCommentDao(this).getByBlog(blogID);
                applySort(currentSortLabel);
                edtComment.setText("");
                rcComment.scrollToPosition(0);
            });
        }

        // Sắp xếp
        TextView tvSort = findViewById(R.id.tvSort);
        View sortRow = findViewById(R.id.layoutSortRow);
        View.OnClickListener sortClick = v->showSortDialog(tvSort);
        if(tvSort!=null) tvSort.setOnClickListener(sortClick);
        if(sortRow!=null) sortRow.setOnClickListener(sortClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Luôn đồng bộ lại comment mới nhất khi quay lại trang
        long blogID = getIntent().getLongExtra("blogID", -1);
        if (blogID > 0 && adapter != null) {
            commentList = new BlogCommentDao(this).getByBlog(blogID);
            applySort(currentSortLabel);
        }
    }

    private void showSortDialog(TextView anchor){
        String[] options={"Mới nhất","Lâu nhất","Hữu ích nhất","Ít hữu ích nhất"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo")
                .setItems(options,(d,i)->{
                    currentSortLabel=options[i];
                    applySort(currentSortLabel);
                    anchor.setText(options[i]);
                }).show();
    }

    private void applySort(String label){
        if(commentList==null || commentList.size()<=1) return;
        switch(label){
            case "Mới nhất":
                java.util.Collections.sort(commentList,(a,b)->compareDateDesc(a.getCreatedAt(),b.getCreatedAt()));
                break;
            case "Lâu nhất":
                java.util.Collections.sort(commentList,(a,b)->compareDateAsc(a.getCreatedAt(),b.getCreatedAt()));
                break;
            case "Hữu ích nhất":
                java.util.Collections.sort(commentList,(a,b)->Integer.compare(b.getUsefulness(),a.getUsefulness()));
                break;
            case "Ít hữu ích nhất":
                java.util.Collections.sort(commentList,(a,b)->Integer.compare(a.getUsefulness(),b.getUsefulness()));
                break;
        }
        if(adapter!=null) adapter.updateData(commentList);
    }

    private int compareDateDesc(String d1,String d2){
        java.util.Date date1=parseDate(d1); java.util.Date date2=parseDate(d2); return date2.compareTo(date1);
    }
    private int compareDateAsc(String d1,String d2){
        java.util.Date date1=parseDate(d1); java.util.Date date2=parseDate(d2); return date1.compareTo(date2);
    }
    private java.util.Date parseDate(String str){
        try{ return sdf.parse(str);}catch(ParseException e){ return new java.util.Date(0);} }
}

