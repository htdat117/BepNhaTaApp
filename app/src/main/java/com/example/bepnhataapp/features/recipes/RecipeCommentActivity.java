package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.widget.ImageButton;
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

        // Kiểm tra đăng nhập và lấy user
        String phone = SessionManager.getPhone(this);
        CustomerDao dao = new CustomerDao(this);
        Customer user = dao.findByPhone(phone);
        TextView tvLogin = findViewById(R.id.tv_login);
        if (tvLogin != null && user != null) {
            tvLogin.setText(user.getFullName());
        }
        // (Có thể set avatar nếu muốn)

        // Load bình luận theo recipeId
        RecyclerView rcComment = findViewById(R.id.rcComment);
        if (rcComment != null && recipeId > 0) {
            List<RecipeComment> commentList = new RecipeCommentDao(this).getByRecipe(recipeId);
            RecipeCommentAdapter adapter = new RecipeCommentAdapter(commentList);
            rcComment.setLayoutManager(new LinearLayoutManager(this));
            rcComment.setAdapter(adapter);
        }

        // Xử lý nút back
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }
}