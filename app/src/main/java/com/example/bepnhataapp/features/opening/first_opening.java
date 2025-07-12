package com.example.bepnhataapp.features.opening;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.R;

public class first_opening extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_opening);

        findViewById(R.id.btn_start).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, com.example.bepnhataapp.features.opening.second_opening.class));
            finish();
        });
    }
}
