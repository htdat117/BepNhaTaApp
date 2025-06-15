package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.closeButton).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        android.widget.CheckBox cb = findViewById(R.id.checkboxTerms);
        android.widget.Button btnContinue = findViewById(R.id.button_continue);

        btnContinue.setOnClickListener(v -> {
            if (cb.isChecked()) {
                android.content.Intent intent = new android.content.Intent(this, AuthenticationActivity.class);
                intent.putExtra("flow", "register");
                startActivity(intent);
            } else {
                android.widget.Toast.makeText(this, "Vui lòng chấp nhận điều khoản để tiếp tục", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
} 