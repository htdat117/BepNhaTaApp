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
import java.util.ArrayList;
import java.util.List;

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


        adapter = new DownloadedRecipeAdapter(list, new DownloadedRecipeAdapter.OnRecipeActionListener() {
            @Override
            public void onView(DownloadedRecipe recipe) {
                Toast.makeText(DownloadedContentActivity.this, "Xem: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDelete(DownloadedRecipe recipe) {
                list.remove(recipe);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
} 