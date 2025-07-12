package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.RecipeCommentDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.RecipeComment;
import com.example.bepnhataapp.common.utils.SessionManager;
import java.util.List;

public class RecipeCommentActivity extends AppCompatActivity {

    private RecipeCommentAdapter adapter;
    private java.util.List<RecipeComment> commentList = new java.util.ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận recipeId từ Intent
        long recipeId = getIntent().getLongExtra("recipeId", -1);

        // Views
        ImageView btnBack = findViewById(R.id.btnBack);
        de.hdodenhof.circleimageview.CircleImageView imvAvatarCmt = findViewById(R.id.imvAvatarCmt);
        EditText edtComment = findViewById(R.id.edtComment);
        ImageButton btnSendComment = findViewById(R.id.btnSendComment);
        View inputBar = imvAvatarCmt!=null? (View) imvAvatarCmt.getParent():null;

        // Handle back
        if(btnBack!=null) btnBack.setOnClickListener(v->finish());

        // Kiểm tra đăng nhập và lấy user
        String phone = SessionManager.getPhone(this);
        CustomerDao customerDao = new CustomerDao(this);
        Customer user = phone!=null? customerDao.findByPhone(phone):null;

        if(!SessionManager.isLoggedIn(this)){
            // hide input bar
            if(inputBar!=null) inputBar.setVisibility(View.GONE);
        } else {
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
        }

        // Load bình luận theo recipeId
        RecyclerView rcComment = findViewById(R.id.rcComment);
        if (rcComment != null && recipeId > 0) {
            commentList = new RecipeCommentDao(this).getByRecipe(recipeId);
            adapter = new RecipeCommentAdapter(commentList);
            rcComment.setLayoutManager(new LinearLayoutManager(this));
            rcComment.setAdapter(adapter);
        }

        // Send comment action
        if(btnSendComment!=null){
            btnSendComment.setOnClickListener(v->{
                if(!SessionManager.isLoggedIn(this)){
                    Toast.makeText(this,"Vui lòng đăng nhập để bình luận",Toast.LENGTH_SHORT).show();
                    return;
                }
                String content= edtComment.getText().toString().trim();
                if(content.isEmpty()){
                    Toast.makeText(this,"Nội dung không được trống",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(user==null) return;

                RecipeComment rc = new RecipeComment();
                rc.setRecipeID(recipeId);
                rc.setCustomerID(user.getCustomerID());
                rc.setContent(content);
                rc.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                rc.setUsefulness(0);
                new RecipeCommentDao(this).insert(rc);

                commentList = new RecipeCommentDao(this).getByRecipe(recipeId);
                applySort(currentSortLabel);
                edtComment.setText("");
                rcComment.scrollToPosition(0);
            });
        }

        // Sorting
        TextView tvSort = findViewById(R.id.tvSort);
        View sortRow = findViewById(R.id.layoutSortRow);
        View.OnClickListener sortClick = v->showSortDialog(tvSort);
        if(tvSort!=null) tvSort.setOnClickListener(sortClick);
        if(sortRow!=null) sortRow.setOnClickListener(sortClick);
    }

    private String currentSortLabel="Mới nhất";

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
