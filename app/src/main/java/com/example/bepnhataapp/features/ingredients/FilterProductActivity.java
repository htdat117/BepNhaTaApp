package com.example.bepnhataapp.features.ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.databinding.ActivityFilterProductBinding;

public class FilterProductActivity extends AppCompatActivity {

    ActivityFilterProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toProduct();
    }

    private void toProduct() {
        binding.btnToProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterProductActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }
}