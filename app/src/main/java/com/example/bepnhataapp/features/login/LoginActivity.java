package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class LoginActivity extends AppCompatActivity implements PhoneNumberFragment.OnPhoneNumberSubmittedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.view.View close = findViewById(R.id.closeButton);
        if (close != null) {
            close.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PhoneNumberFragment())
                    .commit();
        }
    }

    @Override
    public void onPhoneNumberSubmitted(String phoneNumber) {
        // Navigate to password screen
        PasswordFragment passwordFragment = PasswordFragment.newInstance(phoneNumber);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, passwordFragment)
                .addToBackStack(null)
                .commit();
    }
}