package com.example.bepnhataapp.features.offline;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.DownloadedRecipeAdapter;
import com.example.bepnhataapp.common.model.DownloadedRecipe;
import com.example.bepnhataapp.common.dao.RecipeDownloadDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.RecipeDownload;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.model.RecipeDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DownloadedContentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DownloadedRecipeAdapter adapter;
    private List<DownloadedRecipe> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_content);

        recyclerView = findViewById(R.id.recyclerViewDownloaded);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        refreshData();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void refreshData() {
        list.clear();
        String phone = SessionManager.getPhone(this);
        if (phone == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để xem công thức đã tải!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.recyclerViewDownloaded).setVisibility(android.view.View.GONE);
            findViewById(R.id.layoutNoData).setVisibility(android.view.View.VISIBLE);
            return;
        }
        CustomerDao customerDao = new CustomerDao(this);
        Customer customer = customerDao.findByPhone(phone);
        if (customer == null) {
            Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.recyclerViewDownloaded).setVisibility(android.view.View.GONE);
            findViewById(R.id.layoutNoData).setVisibility(android.view.View.VISIBLE);
            return;
        }
        long customerId = customer.getCustomerID();
        RecipeDownloadDao downloadDao = new RecipeDownloadDao(this);
        RecipeDao recipeDao = new RecipeDao(this);
        RecipeDetailDao detailDao = new RecipeDetailDao(this);
        for (RecipeDownload rd : downloadDao.getByCustomerID(customerId)) {
            RecipeEntity entity = recipeDao.getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == rd.getRecipeID())
                .findFirst().orElse(null);
            RecipeDetail detail = detailDao.get(rd.getRecipeID());
            if (entity != null) {
                String cal = (detail != null && detail.getCalo() > 0) ? ((int)detail.getCalo()) + " cal" : "700 cal";
                String time = (detail != null && detail.getCookingTimeMinutes() > 0) ? detail.getCookingTimeMinutes() + " phút" : "20 phút";
                String benefit = (detail != null && detail.getBenefit() != null && !detail.getBenefit().isEmpty()) ? detail.getBenefit() : "Bổ máu";
                String level = (detail != null && detail.getLevel() != null && !detail.getLevel().isEmpty()) ? detail.getLevel() : "Trung bình";
                list.add(new DownloadedRecipe(
                    entity.getRecipeID(),
                    entity.getRecipeName(),
                    cal,
                    benefit,
                    time,
                    R.drawable.food_placeholder,
                    entity.getImageThumb(),
                    benefit,
                    level
                ));
            }
        }
        if (adapter == null) {
            adapter = new DownloadedRecipeAdapter(list, new DownloadedRecipeAdapter.OnRecipeActionListener() {
                @Override
                public void onView(DownloadedRecipe recipe) {
                    // Không cần Toast nữa, đã chuyển intent trong Adapter
                }
                @Override
                public void onDelete(DownloadedRecipe recipe) {
                    new androidx.appcompat.app.AlertDialog.Builder(DownloadedContentActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa công thức này khỏi danh sách offline?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            list.remove(recipe);
                            adapter.notifyDataSetChanged();
                            refreshData();
                            Toast.makeText(DownloadedContentActivity.this, "Đã xóa công thức offline thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }
        if (list.isEmpty()) {
            findViewById(R.id.recyclerViewDownloaded).setVisibility(android.view.View.GONE);
            findViewById(R.id.layoutNoData).setVisibility(android.view.View.VISIBLE);
        } else {
            findViewById(R.id.recyclerViewDownloaded).setVisibility(android.view.View.VISIBLE);
            findViewById(R.id.layoutNoData).setVisibility(android.view.View.GONE);
        }
    }
} 
