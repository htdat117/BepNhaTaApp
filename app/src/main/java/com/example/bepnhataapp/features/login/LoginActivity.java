package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class LoginActivity extends AppCompatActivity implements PhoneNumberFragment.OnPhoneNumberSubmittedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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