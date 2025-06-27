package com.example.bepnhataapp.features.offline;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.DownloadedRecipeAdapter;
import com.example.bepnhataapp.common.models.DownloadedRecipe;
import com.example.bepnhataapp.common.dao.RecipeDownloadDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.RecipeDownload;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;
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
        for (RecipeDownload rd : downloadDao.getByCustomerID(customerId)) {
            RecipeEntity entity = recipeDao.getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == rd.getRecipeID())
                .findFirst().orElse(null);
            if (entity != null) {
                String cal = "700 cal";
                String time = "50 phút";
                list.add(new DownloadedRecipe(
                    entity.getRecipeID(),
                    entity.getRecipeName(),
                    cal,
                    "Giàu đạm",
                    time,
                    R.drawable.food_placeholder,
                    entity.getImageThumb()
                ));
            }
        }
        if (adapter == null) {
            adapter = new DownloadedRecipeAdapter(list, new DownloadedRecipeAdapter.OnRecipeActionListener() {
                @Override
                public void onView(DownloadedRecipe recipe) {
                    Toast.makeText(DownloadedContentActivity.this, "Xem: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onDelete(DownloadedRecipe recipe) {
                    list.remove(recipe);
                    adapter.notifyDataSetChanged();
                    refreshData();
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