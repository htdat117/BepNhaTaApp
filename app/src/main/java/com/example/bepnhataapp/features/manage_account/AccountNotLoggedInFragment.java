package com.example.bepnhataapp.features.manage_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;

public class AccountNotLoggedInFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_not_logged_in, container, false);

        // Setup click listeners for Register and Login buttons
        android.view.View btnRegister = view.findViewById(R.id.btnRegister);
        android.view.View btnLogin = view.findViewById(R.id.btnLogin);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.login.RegistrationActivity.class);
                startActivity(intent);
            });
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.login.LoginActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }
} 
