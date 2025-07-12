package com.example.bepnhataapp.features.cart;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CartFragment())
                .commit();
        }
    }
}
