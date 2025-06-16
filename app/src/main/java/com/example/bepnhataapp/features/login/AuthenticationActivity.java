package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class AuthenticationActivity extends AppCompatActivity {

    private String flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        flow = getIntent().getStringExtra("flow");

        findViewById(R.id.closeButton).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.confirmButton).setOnClickListener(v -> {
            // TODO: validate OTP here
            android.content.Intent next;
            if ("reset".equals(flow)) {
                next = new android.content.Intent(this, NewPasswordActivity.class);
            } else if ("register".equals(flow)) {
                next = new android.content.Intent(this, AccountInfoActivity.class);
            } else {
                // default just finish
                finish();
                return;
            }
            startActivity(next);
        });
    }
}